package com.example.data

import com.example.model.VideoStyle

data class PromptInspiration(
    val title: String,
    val shortIdea: String,
    val fullPrompt: String,
    val recommendedStyle: VideoStyle,
    val category: String,
    val tags: List<String>
)

object PromptAssistantData {
    val quickEnhancers = listOf(
        "✨ Golden Hour Glow",
        "🎥 Anamorphic 35mm",
        "🌪️ Volumetric Fog",
        "⚡ 120fps Slow-Mo",
        "🛸 Drone Orbit Shot",
        "🌧️ Neon Rain Reflection",
        "🌸 Falling Rose Petals",
        "🔥 Ambient Fire Embers",
        "🌌 Ethereal Starlight",
        "🎬 Shallow Depth of Field"
    )

    val categories = listOf("All", "Cinematic", "Bollywood", "Wedding", "Devotional", "Reels", "Cartoon", "Comedy")

    val sampleInspirations = listOf(
        PromptInspiration(
            title = "Royal Indian Palace Awakening",
            shortIdea = "Palace in morning light with peacocks",
            fullPrompt = "Grand ancient Rajasthani palace courtyard at sunrise, golden sun rays piercing through intricately carved stone jharokhas, majestic peacocks spreading vibrant blue feathers, royal fountain mist glowing in golden hour, 4K cinematic slow pan, ultra realistic texture",
            recommendedStyle = VideoStyle.BOLLYWOOD,
            category = "Bollywood",
            tags = listOf("Palace", "Sunrise", "Royal")
        ),
        PromptInspiration(
            title = "Cyberpunk Neo-Tokyo Rain",
            shortIdea = "Cyberpunk street with neon reflections",
            fullPrompt = "Futuristic cyberpunk downtown avenue drenched in midnight rain, towering holographic billboards reflecting on wet asphalt, glowing cyan and magenta neon signs, flying aerocars gliding past skyscrapers, anamorphic camera glide, cinematic 4K hyperdetail",
            recommendedStyle = VideoStyle.CINEMATIC_4K,
            category = "Cinematic",
            tags = listOf("Cyberpunk", "Rain", "Neon")
        ),
        PromptInspiration(
            title = "Sacred Ganga Maha Aarti",
            shortIdea = "Spiritual Ganga Aarti ceremony with diyas",
            fullPrompt = "Spiritual evening Ganga Aarti on Varanasi ghats, glowing brass multi-tiered fire lamps swirling in synchronized rhythm, thousands of golden diyas floating on sacred river currents, temple bells resonance, sacred incense smoke swirling in divine aura, 8k devotional video",
            recommendedStyle = VideoStyle.DEVOTIONAL,
            category = "Devotional",
            tags = listOf("Spiritual", "Aarti", "Sacred")
        ),
        PromptInspiration(
            title = "Royal Bridal Grand Entry",
            shortIdea = "Bride walking under flower canopy",
            fullPrompt = "Breathtaking Indian bride wearing red and gold zardozi lehenga walking gently beneath an illuminated canopy of fresh jasmine and orchids, sparkler fireworks illuminating the background, gentle emotional smile, cinematic slow motion 60fps, romantic bokeh",
            recommendedStyle = VideoStyle.WEDDING,
            category = "Wedding",
            tags = listOf("Wedding", "Bride", "Romantic")
        ),
        PromptInspiration(
            title = "Playful Puppy in Sunflower Field",
            shortIdea = "Golden retriever running in sunflowers",
            fullPrompt = "Adorable golden retriever puppy bounding joyfully through a vibrant endless blooming sunflower field, yellow pollen floating in afternoon golden sun, cute floppy ears moving in joyful slow motion, bright vibrant animated colors, cinematic heartwarming lighting",
            recommendedStyle = VideoStyle.COMEDY,
            category = "Comedy",
            tags = listOf("Puppy", "Cute", "Sunflowers")
        ),
        PromptInspiration(
            title = "Cute 3D Little Robot Explorer",
            shortIdea = "Tiny robot discovering glowing plant",
            fullPrompt = "Cute stylized 3D animated mini robot with big expressive blue LED eyes discovering a magical glowing bioluminescent flower in an enchanted alien forest, Octane render, Pixar Disney 3D animation style, soft volumetric lighting, whimsical charm",
            recommendedStyle = VideoStyle.CARTOON,
            category = "Cartoon",
            tags = listOf("3D Animation", "Robot", "Magical")
        ),
        PromptInspiration(
            title = "High Energy Street Fashion Reel",
            shortIdea = "Model walking in urban city",
            fullPrompt = "Chic streetwear fashion model striding forward on bustling city crosswalk, rapid dynamic whip-pan camera transition, vibrant color contrast, dramatic lens flare, high-fashion magazine aesthetic, 9:16 vertical reel optimized, viral energy",
            recommendedStyle = VideoStyle.REELS_SHORTS,
            category = "Reels",
            tags = listOf("Fashion", "Urban", "Trendy")
        ),
        PromptInspiration(
            title = "Dramatic Black Panther in Jungle",
            shortIdea = "Black panther stalking in dark jungle",
            fullPrompt = "Majestic black panther stealthily stalking across a mossy ancient tree trunk in dense misty rainforest, piercing emerald eyes glowing in emerald dappled light, water droplets dripping from lush leaves, National Geographic wildlife documentary 4K, realistic muscle movement",
            recommendedStyle = VideoStyle.REALISTIC,
            category = "Cinematic",
            tags = listOf("Wildlife", "Panther", "Jungle")
        )
    )

    fun expandPrompt(basicIdea: String, style: VideoStyle): String {
        val trimmed = basicIdea.trim()
        if (trimmed.isEmpty()) return ""

        val lighting = when (style) {
            VideoStyle.CINEMATIC_4K -> "anamorphic lens flare, dramatic volumetric lighting with teal and orange contrast"
            VideoStyle.REALISTIC -> "natural ambient daylight, soft realistic shadows, pristine true-to-life color reproduction"
            VideoStyle.BOLLYWOOD -> "warm golden festive luminescence, extravagant palace chandelier glow, sparkling dust motes"
            VideoStyle.WEDDING -> "romantic golden hour backlighting, soft dreamy bokeh, warm fairy light sparkles"
            VideoStyle.DEVOTIONAL -> "sacred celestial aura, divine golden light beams descending, ethereal holy temple glow"
            VideoStyle.COMEDY -> "bright vibrant cheerful lighting, punchy saturated colors, warm approachable ambiance"
            VideoStyle.CARTOON -> "3D stylized lighting, soft subsurface scattering, whimsical radiant storybook colors"
            VideoStyle.REELS_SHORTS -> "high contrast neon punch, dynamic strobe accents, crisp modern mobile lighting"
        }

        val cameraMotion = when (style) {
            VideoStyle.CINEMATIC_4K -> "slow cinematic push-in on 35mm Arri lens, shallow depth of field"
            VideoStyle.REALISTIC -> "smooth handheld steadicam tracking shot with natural micro-movements"
            VideoStyle.BOLLYWOOD -> "dramatic sweeping crane shot gliding down in majestic slow motion"
            VideoStyle.WEDDING -> "gentle gliding orbit around the subject, elegant slow motion 60fps"
            VideoStyle.DEVOTIONAL -> "serene upward tilt revealing transcendent temple spires and divine atmosphere"
            VideoStyle.COMEDY -> "playful dynamic bounce camera timing with expressive focal shift"
            VideoStyle.CARTOON -> "dynamic Disney-Pixar animation camera sweep with expressive perspective"
            VideoStyle.REELS_SHORTS -> "fast viral snap zoom and whip-pan transition, vertical framing"
        }

        return "$trimmed, $cameraMotion, illuminated by $lighting, rendered in ${style.promptBooster}, 8k resolution master quality"
    }
}
