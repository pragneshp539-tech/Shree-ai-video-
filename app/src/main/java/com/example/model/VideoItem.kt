package com.example.model

data class VideoItem(
    val id: String,
    val prompt: String,
    val enhancedPrompt: String,
    val style: VideoStyle,
    val aspectRatio: AspectRatio,
    val durationSeconds: Int, // 5, 10, 15
    val motion: CameraMotion,
    val audioTrack: AudioTrackOption,
    val isImageToVideo: Boolean,
    val sourceImageUri: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val isDemoMode: Boolean = true,
    val resolution: String = "4K / 1080p HD",
    val visualSeed: Long = System.currentTimeMillis() % 100000L
)

data class UserProfile(
    val id: String = "creator_sree",
    val name: String = "Free Studio Creator",
    val email: String = "free@sreeai.studio",
    val tokensRemaining: Int = 100, // Starting tokens balance for free video generation
    val tier: SubscriptionTier = SubscriptionTier.FREE_UNLIMITED,
    val generationsCount: Int = 0,
    val lastCheckInTimestamp: Long = 0L, // Timestamp of last 24-hour check-in
    val checkInStreak: Int = 0, // Current daily streak (0 to 7)
    val totalTokensClaimed: Int = 0,
    val customApiKey: String = "",
    val customApiEndpoint: String = "",
    val selectedLanguage: Language = Language.HINDI
) {
    // Backwards-compatible alias for existing components
    val creditsRemaining: Int get() = tokensRemaining

    // 24 hours in milliseconds
    private val twentyFourHoursMs: Long = 24L * 60L * 60L * 1000L

    // True if user can claim their daily check-in (never claimed or >= 24h passed)
    val isCheckInAvailable: Boolean
        get() {
            if (lastCheckInTimestamp == 0L) return true
            val elapsed = System.currentTimeMillis() - lastCheckInTimestamp
            return elapsed >= twentyFourHoursMs
        }

    // Remaining milliseconds until next 24-hour check-in
    val millisUntilNextCheckIn: Long
        get() {
            if (lastCheckInTimestamp == 0L) return 0L
            val elapsed = System.currentTimeMillis() - lastCheckInTimestamp
            val remaining = twentyFourHoursMs - elapsed
            return remaining.coerceAtLeast(0L)
        }
}

enum class SubscriptionTier(
    val displayName: String,
    val priceText: String,
    val monthlyCredits: Int,
    val maxResolution: String,
    val features: List<String>
) {
    FREE_UNLIMITED(
        displayName = "100% Free Lifetime Edition",
        priceText = "Free Forever (मुफ़्त / મફત)",
        monthlyCredits = 1000,
        maxResolution = "4K Ultra HD",
        features = listOf(
            "Daily 24-Hour Free Tokens Check-in",
            "Unlimited Free Video Generations",
            "All 8 AI Video Styles 100% Unlocked",
            "4K Ultra HD & 1080p Resolution",
            "Full Audio & Music Synthesis",
            "Zero Subscription / 100% Free Forever"
        )
    ),
    COMMUNITY_CREATOR(
        displayName = "Community Creator Edition",
        priceText = "Free ($0)",
        monthlyCredits = 1000,
        maxResolution = "1080p Full HD",
        features = listOf(
            "Text to Video & Image to Video",
            "Daily Token Streaks & Bonus Refills",
            "Full Camera Motion Controls",
            "Fast MP4 Video Export",
            "Commercial & Personal Use Allowed"
        )
    )
}
