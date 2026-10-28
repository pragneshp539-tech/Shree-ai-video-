package com.example.model

enum class VideoStyle(
    val id: String,
    val titleEn: String,
    val titleHi: String,
    val titleGu: String,
    val description: String,
    val promptBooster: String,
    val iconName: String,
    val accentHex: Long
) {
    CINEMATIC_4K(
        id = "cinematic_4k",
        titleEn = "Cinematic 4K",
        titleHi = "सिनेमैटिक 4K",
        titleGu = "સિનેમેટિક 4K",
        description = "Anamorphic widescreen, high dynamic range, theatrical color grading",
        promptBooster = "cinematic 4k, anamorphic lens flare, shallow depth of field, blockbuster movie aesthetic, unreal engine 5 render, award winning cinematography",
        iconName = "Movie",
        accentHex = 0xFF00E5FF
    ),
    REALISTIC(
        id = "realistic",
        titleEn = "Realistic",
        titleHi = "यथार्थवादी",
        titleGu = "વાસ્તવિક",
        description = "Lifelike textures, natural ambient light, authentic physical presence",
        promptBooster = "hyperrealistic, photoreal 8k, natural daylight, shot on 35mm Arri Alexa, pristine skin textures, organic motion blur",
        iconName = "CameraAlt",
        accentHex = 0xFF10B981
    ),
    BOLLYWOOD(
        id = "bollywood",
        titleEn = "Bollywood Style",
        titleHi = "बॉलीवुड स्टाइल",
        titleGu = "બોલિવૂડ સ્ટાઇલ",
        description = "Grand festive sets, slow-motion dupatta breeze, rich royal vibrant colors",
        promptBooster = "grand bollywood cinema style, dramatic slow motion, vibrant royal colors, flowing fabric, glittering festival lights, extravagant musical set, warm golden hue",
        iconName = "Celebration",
        accentHex = 0xFFF59E0B
    ),
    WEDDING(
        id = "wedding",
        titleEn = "Wedding Cinematic",
        titleHi = "वेडिंग सिनेमैटिक",
        titleGu = "લગ્ન સિનેમેટિક",
        description = "Romantic golden hour, sparkling fairy lights, royal bridal splendor",
        promptBooster = "dreamy wedding cinematography, golden hour sunset glow, royal lehenga details, flower petals gently falling, soft bokeh, emotional romantic atmosphere",
        iconName = "Favorite",
        accentHex = 0xFFEC4899
    ),
    DEVOTIONAL(
        id = "devotional",
        titleEn = "Devotional Videos",
        titleHi = "भक्ति वीडियो",
        titleGu = "ભક્તિ વિડીયો",
        description = "Divine radiant aura, sacred temple diya lights, tranquil spiritual ambiance",
        promptBooster = "divine spiritual video, radiant golden aura, holy temple diya flames, gentle incense smoke, ethereal clouds, peaceful sacred blessing, transcendent light rays",
        iconName = "LightMode",
        accentHex = 0xFFFBBF24
    ),
    COMEDY(
        id = "comedy",
        titleEn = "Comedy Videos",
        titleHi = "कॉमेडी वीडियो",
        titleGu = "કોમેડી વિડીયો",
        description = "Playful exaggerated timing, humorous expressions, punchy upbeat scene",
        promptBooster = "humorous comedy scene, dynamic expressive character reactions, quirky funny timing, bright colorful lighting, hilarious slapstick moments",
        iconName = "Mood",
        accentHex = 0xFFF97316
    ),
    CARTOON(
        id = "cartoon",
        titleEn = "Cartoon & Animation",
        titleHi = "कार्टून और एनिमेशन",
        titleGu = "કાર્ટૂન અને એનિમેશન",
        description = "3D Pixar-style charm, expressive stylized characters, radiant colors",
        promptBooster = "3D animated cartoon style, stylized cute proportions, octane render, pixar disney aesthetic, vibrant whimsical lighting, highly expressive",
        iconName = "Animation",
        accentHex = 0xFFA855F7
    ),
    REELS_SHORTS(
        id = "reels_shorts",
        titleEn = "Instagram Reels & Shorts",
        titleHi = "इंस्टाग्राम रील्स और शॉर्ट्स",
        titleGu = "ઇન્સ્ટાગ્રામ રીલ્સ",
        description = "Fast-paced viral aesthetic, high energy dynamic motion, mobile native",
        promptBooster = "trending instagram reel aesthetic, dynamic fast camera zoom, vibrant neon punch, high visual hook, crisp vertical framing, viral look",
        iconName = "Share",
        accentHex = 0xFF38BDF8
    );

    fun getTitle(lang: Language): String {
        return when (lang) {
            Language.ENGLISH -> titleEn
            Language.HINDI -> titleHi
            Language.GUJARATI -> titleGu
        }
    }
}
