package com.example.model

enum class AspectRatio(
    val id: String,
    val label: String,
    val sublabel: String,
    val ratio: Float,
    val widthFactor: Float,
    val heightFactor: Float
) {
    PORTRAIT_9_16(
        id = "9:16",
        label = "9:16",
        sublabel = "Reels / Shorts",
        ratio = 9f / 16f,
        widthFactor = 0.5625f,
        heightFactor = 1.0f
    ),
    LANDSCAPE_16_9(
        id = "16:9",
        label = "16:9",
        sublabel = "YouTube / Cinema",
        ratio = 16f / 9f,
        widthFactor = 1.0f,
        heightFactor = 0.5625f
    ),
    SQUARE_1_1(
        id = "1:1",
        label = "1:1",
        sublabel = "Square / Feed",
        ratio = 1.0f,
        widthFactor = 1.0f,
        heightFactor = 1.0f
    )
}

enum class CameraMotion(
    val id: String,
    val labelEn: String,
    val labelHi: String,
    val labelGu: String,
    val promptTag: String
) {
    DYNAMIC(
        id = "dynamic",
        labelEn = "Dynamic Drone",
        labelHi = "डायनामिक ड्रोन",
        labelGu = "ડાયનેમિક ડ્રોન",
        promptTag = "dynamic soaring drone camera movement, cinematic forward sweep"
    ),
    PAN_RIGHT(
        id = "pan_right",
        labelEn = "Pan Right",
        labelHi = "पैन राइट",
        labelGu = "પાન રાઇટ",
        promptTag = "smooth cinematic camera pan to the right"
    ),
    PAN_LEFT(
        id = "pan_left",
        labelEn = "Pan Left",
        labelHi = "पैन लेफ्ट",
        labelGu = "પાન લેફ્ટ",
        promptTag = "smooth cinematic camera pan to the left"
    ),
    ZOOM_IN(
        id = "zoom_in",
        labelEn = "Slow Zoom In",
        labelHi = "धीमा ज़ूम इन",
        labelGu = "ધીમો ઝૂમ ઇન",
        promptTag = "slow dramatic push-in zoom into subject"
    ),
    ORBIT(
        id = "orbit",
        labelEn = "360 Orbit",
        labelHi = "360 ऑर्बिट",
        labelGu = "360 ઓર્બિટ",
        promptTag = "360 degree smooth orbiting arc shot around subject"
    ),
    STATIC(
        id = "static",
        labelEn = "Tripod Steady",
        labelHi = "स्थिर ट्राइपॉड",
        labelGu = "સ્થિર ટ્રાયપોડ",
        promptTag = "locked-off steady tripod shot with organic subject movement"
    );

    fun getLabel(lang: Language): String {
        return when (lang) {
            Language.ENGLISH -> labelEn
            Language.HINDI -> labelHi
            Language.GUJARATI -> labelGu
        }
    }
}

enum class AudioTrackOption(
    val id: String,
    val nameEn: String,
    val nameHi: String,
    val nameGu: String,
    val genre: String,
    val bpm: Int,
    val soundFxEnabled: Boolean
) {
    EPIC_ORCHESTRAL(
        id = "epic_orchestral",
        nameEn = "Epic Orchestral",
        nameHi = "भव्य ऑर्केस्ट्रा",
        nameGu = "ભવ્ય ઓર્કેસ્ટ્રા",
        genre = "Cinematic Strings & Brass",
        bpm = 95,
        soundFxEnabled = true
    ),
    BOLLYWOOD_BEATS(
        id = "bollywood_beats",
        nameEn = "Bollywood Beats",
        nameHi = "बॉलीवुड बीट्स",
        nameGu = "બોલિવૂડ બીટ્સ",
        genre = "Dhol, Shehnai & Festive Rhythms",
        bpm = 124,
        soundFxEnabled = true
    ),
    DEVOTIONAL_FLUTE(
        id = "devotional_flute",
        nameEn = "Devotional Sitar & Flute",
        nameHi = "भक्ति सितार और बांसुरी",
        nameGu = "ભક્તિ સિતાર અને વાંસળી",
        genre = "Spiritual Ambient & Temple Bells",
        bpm = 68,
        soundFxEnabled = false
    ),
    LOFI_CHILL(
        id = "lofi_chill",
        nameEn = "Lo-Fi Midnight Chill",
        nameHi = "लो-फाई चिल",
        nameGu = "લો-ફાઈ ચિલ",
        genre = "Mellow Vinyl Electric Keys",
        bpm = 84,
        soundFxEnabled = false
    ),
    CYBERPUNK_SYNTH(
        id = "cyberpunk_synth",
        nameEn = "Cyberpunk Synthwave",
        nameHi = "साइबरपंक सिंथ",
        nameGu = "સાયબરપંક સિન્થ",
        genre = "Futuristic Analog Bass & Arps",
        bpm = 120,
        soundFxEnabled = true
    ),
    COMEDY_WHIMSICAL(
        id = "comedy_whimsical",
        nameEn = "Comedy Whimsical",
        nameHi = "कॉमेडी मस्ती",
        nameGu = "કોમેડી મસ્તી",
        genre = "Playful Pizzicato & Marimba",
        bpm = 112,
        soundFxEnabled = true
    ),
    NONE(
        id = "none",
        nameEn = "No Audio (Mute)",
        nameHi = "मूक (कोई ऑडियो नहीं)",
        nameGu = "શાંત (ઓડિયો વગર)",
        genre = "Silent Video Track",
        bpm = 0,
        soundFxEnabled = false
    );

    fun getName(lang: Language): String {
        return when (lang) {
            Language.ENGLISH -> nameEn
            Language.HINDI -> nameHi
            Language.GUJARATI -> nameGu
        }
    }
}
