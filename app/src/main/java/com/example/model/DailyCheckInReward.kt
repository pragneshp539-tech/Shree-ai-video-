package com.example.model

data class DailyRewardTier(
    val dayNumber: Int,
    val tokens: Int,
    val bonusTitleEn: String,
    val bonusTitleHi: String,
    val bonusTitleGu: String,
    val isMegaReward: Boolean = false
) {
    fun getTitle(lang: Language): String {
        return when (lang) {
            Language.ENGLISH -> bonusTitleEn
            Language.HINDI -> bonusTitleHi
            Language.GUJARATI -> bonusTitleGu
        }
    }
}

object DailyCheckInSystem {
    val schedule = listOf(
        DailyRewardTier(1, 25, "Day 1 Starter", "दिन 1 शुरुआत", "દિવસ 1 શરૂઆત"),
        DailyRewardTier(2, 30, "Day 2 Streak", "दिन 2 स्ट्रीक", "દિવસ 2 સ્ટ્રીક"),
        DailyRewardTier(3, 35, "Day 3 Fuel", "दिन 3 फ्यूल", "દિવસ 3 ફ્યુઅલ"),
        DailyRewardTier(4, 40, "Day 4 Boost", "दिन 4 बूस्ट", "દિવસ 4 બૂસ્ટ"),
        DailyRewardTier(5, 50, "Day 5 High Roller", "दिन 5 सुपर", "દિવસ 5 સુપર"),
        DailyRewardTier(6, 65, "Day 6 Ultra Spark", "दिन 6 अल्ट्रा", "દિવસ 6 અલ્ટ્રા"),
        DailyRewardTier(7, 100, "Day 7 Mega Jackpot", "दिन 7 महा जैकपॉट", "દિવસ 7 મહા જેકપોટ", isMegaReward = true)
    )

    fun getRewardForDay(dayIndex: Int): DailyRewardTier {
        val normalizedIndex = (dayIndex % schedule.size).coerceIn(0, schedule.size - 1)
        return schedule[normalizedIndex]
    }
}
