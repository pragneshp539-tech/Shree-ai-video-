package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StringsHelper
import com.example.model.SubscriptionTier
import com.example.ui.StudioViewModel
import com.example.ui.theme.GradientGolden
import com.example.ui.theme.GradientStudio
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioCardBorder
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioDarkCard
import com.example.ui.theme.StudioPink
import com.example.ui.theme.StudioViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ProfileScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val videoList by viewModel.videoList.collectAsState()
    val language = userProfile.selectedLanguage

    var apiKeyInput by remember(userProfile.customApiKey) { mutableStateOf(userProfile.customApiKey) }
    var endpointInput by remember(userProfile.customApiEndpoint) { mutableStateOf(userProfile.customApiEndpoint) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(StudioDarkCard)
                    .border(1.dp, StudioCardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar with glowing border
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(GradientStudio)
                            .padding(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color(0xFF0C101A)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Avatar",
                                tint = StudioCyan,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = userProfile.name,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(StudioCyan.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = userProfile.tier.displayName,
                                    color = StudioCyan,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = userProfile.email,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "🎬 ${videoList.size} Videos Created • ${userProfile.generationsCount} Renders",
                            color = Color(0xFF64748B),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Credits & Daily Bonus Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF261D0C), Color(0xFF131A2A))
                        )
                    )
                    .border(1.dp, StudioAmber.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Free Video Tokens Balance",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🪙", fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${userProfile.tokensRemaining} Tokens",
                                    color = StudioAmber,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 24.sp
                                )
                            }
                        }

                        // Daily Check-In Button
                        Button(
                            onClick = { viewModel.openDailyCheckIn() },
                            colors = ButtonDefaults.buttonColors(containerColor = StudioAmber),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            modifier = Modifier.testTag("open_daily_checkin_button")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🎁", fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = StringsHelper.t("daily_bonus", language),
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Daily Streak & Status Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0F1522))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🔥 ${userProfile.checkInStreak} ${StringsHelper.t("streak_label", language)}",
                            color = StudioAmber,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )

                        Text(
                            text = if (userProfile.isCheckInAvailable) "🎁 Ready to Claim!" else "⏳ Next check-in in ${(userProfile.millisUntilNextCheckIn / (1000 * 60 * 60))}h",
                            color = if (userProfile.isCheckInAvailable) Color(0xFF34D399) else StudioCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "🎉 100% Free Forever: Check in every 24 hours to receive free video generation tokens!",
                        color = Color(0xFF38BDF8),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Real AI Video Cloud API Settings
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(StudioDarkCard)
                    .border(1.dp, StudioCardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Key,
                        contentDescription = null,
                        tint = StudioCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = StringsHelper.t("api_settings", language),
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Current Mode Status Badge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (userProfile.customApiKey.isNotBlank()) Color(0xFF0C2719) else Color(0xFF261D0C),
                            RoundedCornerShape(8.dp)
                        )
                        .border(
                            1.dp,
                            if (userProfile.customApiKey.isNotBlank()) Color(0xFF10B981) else StudioAmber.copy(alpha = 0.5f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "✨ Gemini 3.8 Flash AI Prompt Assistant Active • Firebase Cloud Backend Connected (gen-lang-client-0866160124)",
                        color = Color(0xFF38BDF8),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "API Key (Replicate / Fal.ai / Luma / RunPod):",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = apiKeyInput,
                    onValueChange = { apiKeyInput = it },
                    placeholder = {
                        Text(StringsHelper.t("api_key_placeholder", language), color = TextMuted, fontSize = 12.sp)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("api_key_input_field"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = StudioCyan,
                        unfocusedBorderColor = Color(0xFF283450),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = Color(0xFF0F1522),
                        unfocusedContainerColor = Color(0xFF0F1522)
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Custom Backend Endpoint (Optional):",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = endpointInput,
                    onValueChange = { endpointInput = it },
                    placeholder = {
                        Text("https://api.your-backend.com/v1/generate", color = TextMuted, fontSize = 12.sp)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_endpoint_input_field"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = StudioCyan,
                        unfocusedBorderColor = Color(0xFF283450),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = Color(0xFF0F1522),
                        unfocusedContainerColor = Color(0xFF0F1522)
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.saveApiKey(apiKeyInput, endpointInput) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(StudioCyan)
                        .testTag("save_api_key_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text(
                        text = StringsHelper.t("save", language),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Subscription Plans
        item {
            Text(
                text = StringsHelper.t("plans_title", language),
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        items(SubscriptionTier.entries.toList()) { tier ->
            PlanCard(
                tier = tier,
                isCurrent = userProfile.tier == tier,
                onUpgrade = { viewModel.upgradePlan(tier) }
            )
        }
    }
}

@Composable
fun PlanCard(
    tier: SubscriptionTier,
    isCurrent: Boolean,
    onUpgrade: () -> Unit
) {
    val borderColor = if (isCurrent) StudioCyan else StudioCardBorder
    val accent = when (tier) {
        SubscriptionTier.FREE_UNLIMITED -> StudioCyan
        SubscriptionTier.COMMUNITY_CREATOR -> StudioViolet
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(StudioDarkCard)
            .border(if (isCurrent) 1.5.dp else 1.dp, borderColor, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = tier.displayName,
                        color = accent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = tier.priceText,
                        color = TextPrimary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp
                    )
                }

                if (isCurrent) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(StudioCyan.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Active Plan",
                            color = StudioCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Button(
                        onClick = onUpgrade,
                        colors = ButtonDefaults.buttonColors(containerColor = accent),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(
                            text = "Upgrade",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            tier.features.forEach { feature ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = feature,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
