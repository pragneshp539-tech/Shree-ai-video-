package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Language
import com.example.model.StringsHelper
import com.example.model.UserProfile
import com.example.ui.theme.GradientStudio
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioDarkCard
import com.example.ui.theme.StudioViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TopStudioBar(
    userProfile: UserProfile,
    onLanguageSelected: (Language) -> Unit,
    onDailyCheckInClick: () -> Unit,
    onCreditsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLanguageMenu by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Title & Logo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.testTag("app_logo_title")
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(GradientStudio),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Videocam,
                    contentDescription = "Studio Logo",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Sree",
                        color = StudioCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "AI Studio",
                        color = TextPrimary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp
                    )
                }
                Text(
                    text = "Powered by Gemini AI • Firebase",
                    color = StudioCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Language Switcher, Daily Check-in & Tokens Badge
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Language selector button
            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(StudioDarkCard)
                        .border(1.dp, Color(0xFF283450), RoundedCornerShape(20.dp))
                        .clickable { showLanguageMenu = true }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("language_selector_button"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = userProfile.selectedLanguage.flag,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = userProfile.selectedLanguage.name.take(2),
                        color = TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                DropdownMenu(
                    expanded = showLanguageMenu,
                    onDismissRequest = { showLanguageMenu = false },
                    modifier = Modifier.background(Color(0xFF161F33))
                ) {
                    Language.entries.forEach { lang ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = lang.flag, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = lang.displayName,
                                        color = if (lang == userProfile.selectedLanguage) StudioCyan else TextPrimary,
                                        fontWeight = if (lang == userProfile.selectedLanguage) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            },
                            onClick = {
                                onLanguageSelected(lang)
                                showLanguageMenu = false
                            }
                        )
                    }
                }
            }

            // 24h Daily Check-In button
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (userProfile.isCheckInAvailable) Color(0xFF2E2210) else StudioDarkCard)
                    .border(
                        1.dp,
                        if (userProfile.isCheckInAvailable) StudioAmber else Color(0xFF283450),
                        RoundedCornerShape(20.dp)
                    )
                    .clickable { onDailyCheckInClick() }
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .testTag("daily_checkin_top_button"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🎁", fontSize = 13.sp)
                if (userProfile.isCheckInAvailable) {
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Claim",
                        color = StudioAmber,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Tokens pill badge
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF13222E))
                    .border(1.dp, StudioCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .clickable { onDailyCheckInClick() }
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .testTag("credits_badge"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Tokens",
                    tint = StudioCyan,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${userProfile.tokensRemaining}",
                    color = StudioCyan,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp
                )
            }
        }
    }
}
