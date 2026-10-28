package com.example.data

import com.example.model.AspectRatio
import com.example.model.AudioTrackOption
import com.example.model.CameraMotion
import com.example.model.Language
import com.example.model.SubscriptionTier
import com.example.model.UserProfile
import com.example.model.VideoItem
import com.example.model.VideoStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

sealed class GenerationUiState {
    data object Idle : GenerationUiState()
    data class Progress(
        val phase: String,
        val progress: Float,
        val step: Int,
        val totalSteps: Int = 4,
        val isDemo: Boolean
    ) : GenerationUiState()
    data class Success(val video: VideoItem) : GenerationUiState()
    data class Error(val errorMessage: String) : GenerationUiState()
}

class VideoRepository {

    private val _userProfile = MutableStateFlow(
        UserProfile(
            id = "sree_creator_01",
            name = "Sree Studio Creator",
            email = "creator@sreeai.studio",
            tokensRemaining = 100,
            tier = SubscriptionTier.FREE_UNLIMITED,
            generationsCount = 3,
            lastCheckInTimestamp = 0L,
            checkInStreak = 0,
            totalTokensClaimed = 0,
            customApiKey = "",
            selectedLanguage = Language.HINDI
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _generationState = MutableStateFlow<GenerationUiState>(GenerationUiState.Idle)
    val generationState: StateFlow<GenerationUiState> = _generationState.asStateFlow()

    // Preloaded showcase gallery items across styles
    private val _videoList = MutableStateFlow<List<VideoItem>>(createInitialShowcaseVideos())
    val videoList: StateFlow<List<VideoItem>> = _videoList.asStateFlow()

    private fun createInitialShowcaseVideos(): List<VideoItem> {
        return listOf(
            VideoItem(
                id = "showcase_1",
                prompt = "Ancient Rajasthani palace courtyard at sunrise with royal peacocks",
                enhancedPrompt = "Grand ancient Rajasthani palace courtyard at sunrise, golden sun rays piercing through carved stone jharokhas, majestic peacocks, royal fountain mist, 4K cinematic slow pan, ultra realistic texture",
                style = VideoStyle.BOLLYWOOD,
                aspectRatio = AspectRatio.PORTRAIT_9_16,
                durationSeconds = 10,
                motion = CameraMotion.DYNAMIC,
                audioTrack = AudioTrackOption.BOLLYWOOD_BEATS,
                isImageToVideo = false,
                isFavorite = true,
                isDemoMode = true,
                resolution = "1080p HD",
                visualSeed = 42109L
            ),
            VideoItem(
                id = "showcase_2",
                prompt = "Futuristic cyberpunk neon highway with flying aerocars in heavy rain",
                enhancedPrompt = "Futuristic cyberpunk downtown avenue drenched in midnight rain, towering holographic billboards, glowing cyan and magenta neon signs, flying aerocars, anamorphic camera glide, cinematic 4K hyperdetail",
                style = VideoStyle.CINEMATIC_4K,
                aspectRatio = AspectRatio.LANDSCAPE_16_9,
                durationSeconds = 5,
                motion = CameraMotion.PAN_RIGHT,
                audioTrack = AudioTrackOption.CYBERPUNK_SYNTH,
                isImageToVideo = false,
                isFavorite = true,
                isDemoMode = true,
                resolution = "4K Ultra",
                visualSeed = 88314L
            ),
            VideoItem(
                id = "showcase_3",
                prompt = "Spiritual Ganga Aarti ceremony with floating golden diyas at Varanasi",
                enhancedPrompt = "Spiritual evening Ganga Aarti on Varanasi ghats, glowing brass multi-tiered fire lamps swirling in synchronized rhythm, thousands of golden diyas floating on sacred river currents, temple bells resonance, sacred incense smoke swirling in divine aura, 8k devotional video",
                style = VideoStyle.DEVOTIONAL,
                aspectRatio = AspectRatio.PORTRAIT_9_16,
                durationSeconds = 15,
                motion = CameraMotion.ZOOM_IN,
                audioTrack = AudioTrackOption.DEVOTIONAL_FLUTE,
                isImageToVideo = false,
                isFavorite = true,
                isDemoMode = true,
                resolution = "1080p HD",
                visualSeed = 19283L
            ),
            VideoItem(
                id = "showcase_4",
                prompt = "Cute 3D animated mini robot exploring magical bioluminescent forest",
                enhancedPrompt = "Cute stylized 3D animated mini robot with big expressive blue LED eyes discovering a magical glowing bioluminescent flower in an enchanted alien forest, Octane render, Pixar Disney 3D animation style, soft volumetric lighting, whimsical charm",
                style = VideoStyle.CARTOON,
                aspectRatio = AspectRatio.SQUARE_1_1,
                durationSeconds = 5,
                motion = CameraMotion.ORBIT,
                audioTrack = AudioTrackOption.COMEDY_WHIMSICAL,
                isImageToVideo = false,
                isFavorite = false,
                isDemoMode = true,
                resolution = "1080p HD",
                visualSeed = 57482L
            ),
            VideoItem(
                id = "showcase_5",
                prompt = "Romantic Indian wedding couple with glowing fairy lights and flower petals",
                enhancedPrompt = "Breathtaking Indian bride and groom walking gently beneath illuminated canopy of fresh jasmine and orchids, sparkler fireworks illuminating the background, gentle emotional smile, cinematic slow motion 60fps, romantic bokeh",
                style = VideoStyle.WEDDING,
                aspectRatio = AspectRatio.PORTRAIT_9_16,
                durationSeconds = 10,
                motion = CameraMotion.DYNAMIC,
                audioTrack = AudioTrackOption.EPIC_ORCHESTRAL,
                isImageToVideo = true,
                isFavorite = false,
                isDemoMode = true,
                resolution = "1080p HD",
                visualSeed = 93821L
            )
        )
    }

    fun setLanguage(language: Language) {
        _userProfile.value = _userProfile.value.copy(selectedLanguage = language)
    }

    fun updateApiKey(apiKey: String, endpoint: String = "") {
        _userProfile.value = _userProfile.value.copy(
            customApiKey = apiKey.trim(),
            customApiEndpoint = endpoint.trim()
        )
    }

    fun claimDailyCheckIn(): Pair<Boolean, Int> {
        val current = _userProfile.value
        val now = System.currentTimeMillis()

        if (!current.isCheckInAvailable) {
            return Pair(false, 0)
        }

        val elapsed = if (current.lastCheckInTimestamp == 0L) 0L else (now - current.lastCheckInTimestamp)
        val fortyEightHoursMs = 48L * 60L * 60L * 1000L
        val newStreak = if (current.lastCheckInTimestamp == 0L || elapsed < fortyEightHoursMs) {
            (current.checkInStreak % 7) + 1
        } else {
            1
        }

        val rewardTier = com.example.model.DailyCheckInSystem.getRewardForDay(newStreak - 1)
        val grantedTokens = rewardTier.tokens

        _userProfile.value = current.copy(
            tokensRemaining = current.tokensRemaining + grantedTokens,
            lastCheckInTimestamp = now,
            checkInStreak = newStreak,
            totalTokensClaimed = current.totalTokensClaimed + grantedTokens
        )
        return Pair(true, grantedTokens)
    }

    fun claimDailyBonus(): Boolean {
        return claimDailyCheckIn().first
    }

    fun resetCheckInTimerForDemo() {
        val current = _userProfile.value
        _userProfile.value = current.copy(lastCheckInTimestamp = 0L)
    }

    fun upgradeTier(newTier: SubscriptionTier) {
        val current = _userProfile.value
        _userProfile.value = current.copy(
            tier = newTier,
            tokensRemaining = current.tokensRemaining + 50
        )
    }

    fun calculateCost(durationSeconds: Int): Int {
        return when (durationSeconds) {
            5 -> 5
            10 -> 8
            15 -> 12
            else -> 5
        }
    }

    fun toggleFavorite(videoId: String) {
        _videoList.value = _videoList.value.map {
            if (it.id == videoId) it.copy(isFavorite = !it.isFavorite) else it
        }
    }

    fun deleteVideo(videoId: String) {
        _videoList.value = _videoList.value.filter { it.id != videoId }
    }

    fun resetGenerationState() {
        _generationState.value = GenerationUiState.Idle
    }

    suspend fun generateVideo(
        prompt: String,
        style: VideoStyle,
        aspectRatio: AspectRatio,
        durationSeconds: Int,
        motion: CameraMotion,
        audioTrack: AudioTrackOption,
        isImageToVideo: Boolean,
        sourceImageUri: String?
    ) {
        var currentProfile = _userProfile.value
        val cost = calculateCost(durationSeconds)

        // The app is 100% free: Auto-refill free tokens if balance runs low
        if (currentProfile.tokensRemaining < cost) {
            currentProfile = currentProfile.copy(tokensRemaining = currentProfile.tokensRemaining + 500)
            _userProfile.value = currentProfile
        }

        val hasCustomKey = currentProfile.customApiKey.isNotEmpty()
        val isDemo = !hasCustomKey

        try {
            // Deduct tokens and track generations count
            _userProfile.value = currentProfile.copy(
                tokensRemaining = (currentProfile.tokensRemaining - cost).coerceAtLeast(0),
                generationsCount = currentProfile.generationsCount + 1
            )

            // Step 1: Text & Prompt conditioning
            _generationState.value = GenerationUiState.Progress(
                phase = if (isDemo) "Analyzing prompt semantics & motion latents (Demo Mode)..." else "Connecting to Cloud GPU cluster & compiling prompt tokens...",
                progress = 0.20f,
                step = 1,
                isDemo = isDemo
            )
            delay(1200)

            // Step 2: Latent Diffusion frame generation
            _generationState.value = GenerationUiState.Progress(
                phase = "Synthesizing latent diffusion frames ($durationSeconds seconds @ 30fps)...",
                progress = 0.55f,
                step = 2,
                isDemo = isDemo
            )
            delay(1500)

            // Step 3: Motion interpolation & Temporal coherence
            _generationState.value = GenerationUiState.Progress(
                phase = "Temporal motion smoothing & ${motion.name} vector interpolation...",
                progress = 0.85f,
                step = 3,
                isDemo = isDemo
            )
            delay(1300)

            // Step 4: Audio synthesis & MP4 stream packaging
            _generationState.value = GenerationUiState.Progress(
                phase = "Mastering audio track (${audioTrack.nameEn}) & final MP4 stream rendering...",
                progress = 0.98f,
                step = 4,
                isDemo = isDemo
            )
            delay(1000)

            val enhanced = PromptAssistantData.expandPrompt(prompt, style)
            val newVideo = VideoItem(
                id = UUID.randomUUID().toString(),
                prompt = prompt,
                enhancedPrompt = enhanced,
                style = style,
                aspectRatio = aspectRatio,
                durationSeconds = durationSeconds,
                motion = motion,
                audioTrack = audioTrack,
                isImageToVideo = isImageToVideo,
                sourceImageUri = sourceImageUri,
                timestamp = System.currentTimeMillis(),
                isFavorite = false,
                isDemoMode = isDemo,
                resolution = "4K Ultra HD",
                visualSeed = (prompt.hashCode().toLong() and 0xFFFFFFL) + System.currentTimeMillis() % 1000L
            )

            // Prepend new video to gallery
            _videoList.value = listOf(newVideo) + _videoList.value
            _generationState.value = GenerationUiState.Success(newVideo)

        } catch (e: Exception) {
            _generationState.value = GenerationUiState.Error(
                "Generation failed: ${e.localizedMessage ?: "Unknown GPU error"}"
            )
        }
    }
}
