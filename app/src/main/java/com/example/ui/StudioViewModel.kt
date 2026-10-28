package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AudioSynthEngine
import com.example.data.GeminiPromptAssistantService
import com.example.data.GenerationUiState
import com.example.data.PromptAssistantData
import com.example.data.VideoRepository
import com.example.model.AspectRatio
import com.example.model.AudioTrackOption
import com.example.model.CameraMotion
import com.example.model.Language
import com.example.model.SubscriptionTier
import com.example.model.UserProfile
import com.example.model.VideoItem
import com.example.model.VideoStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class StudioTab {
    CREATE,
    GALLERY,
    PROMPTS,
    PROFILE
}

class StudioViewModel(
    private val repository: VideoRepository = VideoRepository(),
    private val audioEngine: AudioSynthEngine = AudioSynthEngine(),
    private val geminiService: GeminiPromptAssistantService = GeminiPromptAssistantService()
) : ViewModel() {

    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val generationState: StateFlow<GenerationUiState> = repository.generationState
    val videoList: StateFlow<List<VideoItem>> = repository.videoList

    private val _currentTab = MutableStateFlow(StudioTab.CREATE)
    val currentTab: StateFlow<StudioTab> = _currentTab.asStateFlow()

    // Active video being played in detail modal
    private val _activePlayingVideo = MutableStateFlow<VideoItem?>(null)
    val activePlayingVideo: StateFlow<VideoItem?> = _activePlayingVideo.asStateFlow()

    private val _isPlayingAudio = MutableStateFlow(false)
    val isPlayingAudio: StateFlow<Boolean> = _isPlayingAudio.asStateFlow()

    // Prompt input state
    val promptInput = MutableStateFlow("")
    val selectedStyle = MutableStateFlow(VideoStyle.CINEMATIC_4K)
    val selectedAspectRatio = MutableStateFlow(AspectRatio.PORTRAIT_9_16)
    val selectedDuration = MutableStateFlow(5) // 5, 10, 15
    val selectedMotion = MutableStateFlow(CameraMotion.DYNAMIC)
    val selectedAudioTrack = MutableStateFlow(AudioTrackOption.BOLLYWOOD_BEATS)
    val isImageToVideoTab = MutableStateFlow(false)
    val selectedImageUri = MutableStateFlow<String?>(null)
    val motionIntensity = MutableStateFlow(7) // 1 - 10

    // Snackbar notification message
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    fun setTab(tab: StudioTab) {
        _currentTab.value = tab
    }

    fun setLanguage(language: Language) {
        repository.setLanguage(language)
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    val isExpandingPrompt = MutableStateFlow(false)

    fun magicExpandPrompt() {
        val currentText = promptInput.value
        val style = selectedStyle.value
        val motion = selectedMotion.value

        viewModelScope.launch {
            isExpandingPrompt.value = true
            showSnackbar("✨ Gemini 3.8 Flash is crafting your cinematic prompt...")
            val result = geminiService.enhanceVideoPrompt(
                rawIdea = if (currentText.isBlank()) "Cinematic visual scene with realistic lighting" else currentText,
                style = style,
                motion = motion
            )
            isExpandingPrompt.value = false
            result.onSuccess { enhanced ->
                promptInput.value = enhanced
                showSnackbar("✨ Prompt enhanced with Gemini AI & cinematic parameters!")
            }.onFailure {
                val fallback = PromptAssistantData.expandPrompt(currentText, style)
                promptInput.value = fallback
                showSnackbar("✨ Prompt enhanced with cinematic parameters!")
            }
        }
    }

    fun applyInspiration(inspirationPrompt: String, style: VideoStyle) {
        promptInput.value = inspirationPrompt
        selectedStyle.value = style
        _currentTab.value = StudioTab.CREATE
        showSnackbar("Prompt loaded into studio!")
    }

    fun appendEnhancerTag(tag: String) {
        val current = promptInput.value
        val cleanedTag = tag.substringAfter(" ")
        promptInput.value = if (current.isBlank()) cleanedTag else "$current, $cleanedTag"
    }

    fun startGeneration() {
        val prompt = promptInput.value.trim()
        if (prompt.isEmpty() && !isImageToVideoTab.value) {
            showSnackbar("Please enter a video prompt description")
            return
        }

        viewModelScope.launch {
            repository.generateVideo(
                prompt = if (prompt.isEmpty()) "Animated photo with dynamic cinematography motion" else prompt,
                style = selectedStyle.value,
                aspectRatio = selectedAspectRatio.value,
                durationSeconds = selectedDuration.value,
                motion = selectedMotion.value,
                audioTrack = selectedAudioTrack.value,
                isImageToVideo = isImageToVideoTab.value,
                sourceImageUri = selectedImageUri.value
            )
        }
    }

    fun dismissGenerationState() {
        repository.resetGenerationState()
    }

    fun openVideoPlayer(video: VideoItem) {
        _activePlayingVideo.value = video
        playAudioForVideo(video)
    }

    fun closeVideoPlayer() {
        _activePlayingVideo.value = null
        stopAudio()
    }

    fun toggleAudioPlayback(video: VideoItem) {
        if (_isPlayingAudio.value) {
            stopAudio()
        } else {
            playAudioForVideo(video)
        }
    }

    private fun playAudioForVideo(video: VideoItem) {
        if (video.audioTrack != AudioTrackOption.NONE) {
            _isPlayingAudio.value = true
            audioEngine.playTrack(video.audioTrack, video.durationSeconds)
        }
    }

    private fun stopAudio() {
        _isPlayingAudio.value = false
        audioEngine.stop()
    }

    fun toggleFavorite(videoId: String) {
        repository.toggleFavorite(videoId)
        showSnackbar("Favorite updated")
    }

    fun deleteVideo(videoId: String) {
        if (_activePlayingVideo.value?.id == videoId) {
            closeVideoPlayer()
        }
        repository.deleteVideo(videoId)
        showSnackbar("Video removed from gallery")
    }

    val showDailyCheckInModal = MutableStateFlow(false)

    fun openDailyCheckIn() {
        showDailyCheckInModal.value = true
    }

    fun closeDailyCheckIn() {
        showDailyCheckInModal.value = false
    }

    fun claimDailyCheckIn() {
        val (success, tokens) = repository.claimDailyCheckIn()
        if (success) {
            val lang = userProfile.value.selectedLanguage
            val msg = when (lang) {
                Language.HINDI -> "🎉 दैनिक 24h चेक-इन सफल! +$tokens मुफ़्त वीडियो टोकन मिले!"
                Language.GUJARATI -> "🎉 દૈનિક 24h ચેક-ઇન સફળ! +$tokens મફત વિડીયો ટોકન મળ્યા!"
                Language.ENGLISH -> "🎉 Daily 24h Check-in Complete! +$tokens Free Tokens Added!"
            }
            showSnackbar(msg)
        } else {
            val remainingHours = (userProfile.value.millisUntilNextCheckIn / (1000 * 60 * 60)) + 1
            showSnackbar("⏳ Already claimed today! Next check-in available in ~${remainingHours}h.")
        }
    }

    fun resetDailyTimerDemo() {
        repository.resetCheckInTimerForDemo()
        showSnackbar("⚡ 24h Timer reset for Demo! Claim again now.")
    }

    fun claimDailyBonus() {
        claimDailyCheckIn()
    }

    fun upgradePlan(tier: SubscriptionTier) {
        repository.upgradeTier(tier)
        showSnackbar("🚀 100% Free Lifetime tier active! Tokens added.")
    }

    fun saveApiKey(key: String, endpoint: String) {
        repository.updateApiKey(key, endpoint)
        if (key.isNotBlank()) {
            showSnackbar("Cloud API Key configured successfully! Real Cloud GPU enabled.")
        } else {
            showSnackbar("Reverted to Demo / Studio Simulator mode.")
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioEngine.stop()
    }
}
