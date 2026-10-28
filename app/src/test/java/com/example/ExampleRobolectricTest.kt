package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.PromptAssistantData
import com.example.data.VideoRepository
import com.example.model.VideoStyle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Sree AI Video", appName)
    }

    @Test
    fun `prompt expansion adds cinematic parameters`() {
        val basicPrompt = "Royal elephant with golden howdah"
        val expanded = PromptAssistantData.expandPrompt(basicPrompt, VideoStyle.BOLLYWOOD)
        assertTrue(expanded.contains("Royal elephant"))
        assertTrue(expanded.contains("bollywood", ignoreCase = true))
    }

    @Test
    fun `credit cost calculation test`() {
        val repo = VideoRepository()
        assertEquals(5, repo.calculateCost(5))
        assertEquals(8, repo.calculateCost(10))
        assertEquals(12, repo.calculateCost(15))
    }

    @Test
    fun `daily 24 hour check in system awards tokens and tracks streak`() {
        val repo = VideoRepository()
        val initialTokens = repo.userProfile.value.tokensRemaining
        assertTrue(repo.userProfile.value.isCheckInAvailable)

        // Day 1 Claim
        val (successDay1, tokensDay1) = repo.claimDailyCheckIn()
        assertTrue(successDay1)
        assertEquals(25, tokensDay1)
        assertEquals(initialTokens + 25, repo.userProfile.value.tokensRemaining)
        assertEquals(1, repo.userProfile.value.checkInStreak)
        assertFalse(repo.userProfile.value.isCheckInAvailable)

        // Same-day second claim should be blocked by 24h rule
        val (blocked, _) = repo.claimDailyCheckIn()
        assertFalse(blocked)

        // Simulate 24-hour passage with demo reset
        repo.resetCheckInTimerForDemo()
        assertTrue(repo.userProfile.value.isCheckInAvailable)

        // Day 2 Claim with streak progression
        val (successDay2, tokensDay2) = repo.claimDailyCheckIn()
        assertTrue(successDay2)
        assertEquals(30, tokensDay2)
        assertEquals(2, repo.userProfile.value.checkInStreak)
    }
}
