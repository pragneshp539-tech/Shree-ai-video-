package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.GenerationUiState
import com.example.model.Language
import com.example.model.StringsHelper
import com.example.ui.theme.GradientStudio
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioDarkCard
import com.example.ui.theme.StudioViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ProgressDialog(
    state: GenerationUiState.Progress,
    language: Language,
    onCancel: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Dialog(onDismissRequest = {}) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF0F1523))
                .border(1.5.dp, StudioCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                .padding(20.dp)
                .testTag("generation_progress_dialog")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header with cancel
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(GradientStudio),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = StringsHelper.t("generating_title", language),
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    IconButton(
                        onClick = onCancel,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancel",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Animated glowing spinner
                Box(
                    modifier = Modifier.size(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { state.progress },
                        modifier = Modifier
                            .size(90.dp)
                            .rotate(rotation),
                        color = StudioCyan,
                        trackColor = Color(0xFF1E283E),
                        strokeWidth = 6.dp
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${(state.progress * 100).toInt()}%",
                            color = StudioCyan,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Step ${state.step}/${state.totalSteps}",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Phase description
                Text(
                    text = state.phase,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Linear progress bar
                LinearProgressIndicator(
                    progress = { state.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = StudioViolet,
                    trackColor = Color(0xFF1E283E)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Mode notice
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (state.isDemo) Color(0xFF261D0C) else Color(0xFF0C2417),
                            RoundedCornerShape(8.dp)
                        )
                        .border(
                            1.dp,
                            if (state.isDemo) StudioAmber.copy(alpha = 0.4f) else Color(0xFF10B981).copy(alpha = 0.4f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (state.isDemo)
                            "⚡ Studio Simulation Mode • Real-time motion interpolation active"
                        else
                            "🚀 Cloud GPU Job Active • Rendering on high-speed cluster",
                        color = if (state.isDemo) StudioAmber else Color(0xFF34D399),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.testTag("cancel_generation_button")
                ) {
                    Text(text = StringsHelper.t("cancel", language), color = TextSecondary)
                }
            }
        }
    }
}
