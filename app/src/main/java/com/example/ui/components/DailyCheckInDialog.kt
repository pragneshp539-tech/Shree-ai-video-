package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Token
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.DailyCheckInSystem
import com.example.model.Language
import com.example.model.StringsHelper
import com.example.model.UserProfile
import com.example.ui.theme.GradientGolden
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioDarkCard
import com.example.ui.theme.StudioDarkSurface
import com.example.ui.theme.StudioViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun DailyCheckInDialog(
    userProfile: UserProfile,
    onClaim: () -> Unit,
    onResetDemo: () -> Unit,
    onDismiss: () -> Unit
) {
    val language = userProfile.selectedLanguage
    val isAvailable = userProfile.isCheckInAvailable

    // Pulse animation for today's reward
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    // Current next day index (0-based)
    val currentDayIndex = if (isAvailable) {
        userProfile.checkInStreak % 7
    } else {
        ((userProfile.checkInStreak - 1).coerceAtLeast(0)) % 7
    }

    val todayReward = DailyCheckInSystem.getRewardForDay(currentDayIndex)

    // Format remaining hours/minutes until next 24h claim
    val remainingMs = userProfile.millisUntilNextCheckIn
    val remainingHours = remainingMs / (1000 * 60 * 60)
    val remainingMinutes = (remainingMs % (1000 * 60 * 60)) / (1000 * 60)
    val timeFormatted = "${remainingHours}h ${remainingMinutes}m"

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(Color(0xFF0F1524))
                .border(1.5.dp, StudioAmber.copy(alpha = 0.6f), RoundedCornerShape(22.dp))
                .padding(18.dp)
                .testTag("daily_checkin_modal")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header with streak badge & close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(GradientGolden),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = StringsHelper.t("daily_checkin_title", language),
                                color = TextPrimary,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Every 24 Hours • 100% Free",
                                color = StudioAmber,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Streak Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E1A12))
                        .border(1.dp, StudioAmber.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🔥", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${userProfile.checkInStreak} ${StringsHelper.t("streak_label", language)}",
                                color = StudioAmber,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        Text(
                            text = "Total Claimed: ${userProfile.totalTokensClaimed} 🪙",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 7-Day Rewards Grid (Row 1: Days 1-4, Row 2: Days 5-7)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Days 1 to 4
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        for (i in 0..3) {
                            DayRewardCard(
                                dayIndex = i,
                                currentDayIndex = currentDayIndex,
                                isAvailable = isAvailable,
                                streak = userProfile.checkInStreak,
                                language = language,
                                pulseScale = pulseScale,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Days 5 to 7 (Day 7 is Mega Bonus!)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        for (i in 4..6) {
                            DayRewardCard(
                                dayIndex = i,
                                currentDayIndex = currentDayIndex,
                                isAvailable = isAvailable,
                                streak = userProfile.checkInStreak,
                                language = language,
                                pulseScale = pulseScale,
                                modifier = Modifier.weight(if (i == 6) 1.4f else 1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Explanatory note
                Text(
                    text = StringsHelper.t("daily_checkin_desc", language),
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Main Action Button
                if (isAvailable) {
                    Button(
                        onClick = onClaim,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(GradientGolden)
                            .testTag("claim_daily_checkin_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${StringsHelper.t("checkin_now", language)} (+${todayReward.tokens} 🪙)",
                                color = Color.Black,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF141F32))
                                .border(1.dp, Color(0xFF283B5A), RoundedCornerShape(12.dp))
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "⏳", fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${StringsHelper.t("next_checkin_timer", language)}: $timeFormatted",
                                    color = StudioCyan,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Test / Demo Reset button
                        Row(
                            modifier = Modifier
                                .clickable { onResetDemo() }
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                tint = StudioAmber,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = StringsHelper.t("instant_demo_reset", language),
                                color = StudioAmber,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DayRewardCard(
    dayIndex: Int,
    currentDayIndex: Int,
    isAvailable: Boolean,
    streak: Int,
    language: Language,
    pulseScale: Float,
    modifier: Modifier = Modifier
) {
    val reward = DailyCheckInSystem.schedule[dayIndex]
    val isPastClaimed = if (isAvailable) {
        dayIndex < currentDayIndex
    } else {
        dayIndex <= currentDayIndex
    }
    val isTodayClaimable = isAvailable && dayIndex == currentDayIndex

    val bg = when {
        isTodayClaimable -> Color(0xFF2E2210)
        isPastClaimed -> Color(0xFF13221C)
        else -> StudioDarkCard
    }

    val border = when {
        isTodayClaimable -> StudioAmber
        isPastClaimed -> Color(0xFF10B981)
        else -> Color(0xFF283450)
    }

    Box(
        modifier = modifier
            .scale(if (isTodayClaimable) pulseScale else 1f)
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .border(
                width = if (isTodayClaimable) 1.5.dp else 1.dp,
                color = border,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(vertical = 8.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Day ${reward.dayNumber}",
                color = if (isTodayClaimable) StudioAmber else TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (isPastClaimed) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF10B981)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Claimed",
                        tint = Color.Black,
                        modifier = Modifier.size(14.dp)
                    )
                }
            } else {
                Text(
                    text = if (reward.isMegaReward) "🏆" else "🪙",
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "+${reward.tokens}",
                color = if (reward.isMegaReward) StudioAmber else TextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
