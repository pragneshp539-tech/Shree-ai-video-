package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AspectRatio
import com.example.model.AudioTrackOption
import com.example.model.CameraMotion
import com.example.model.VideoItem
import com.example.model.VideoStyle
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun VideoPlayerView(
    video: VideoItem,
    isPlayingAudio: Boolean,
    onToggleAudio: () -> Unit,
    onToggleFavorite: (String) -> Unit,
    onDownload: (VideoItem) -> Unit,
    onShare: (VideoItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(true) }
    var currentProgress by remember { mutableFloatStateOf(0f) }
    var isScrubbing by remember { mutableStateOf(false) }

    // Animation ticker for playback
    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(isPlaying, video.durationSeconds) {
        if (isPlaying) {
            while (true) {
                animProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = (video.durationSeconds * 1000 * (1f - animProgress.value)).toInt().coerceAtLeast(100),
                        easing = LinearEasing
                    )
                )
                // Loop
                animProgress.snapTo(0f)
            }
        }
    }

    LaunchedEffect(animProgress.value) {
        if (!isScrubbing) {
            currentProgress = animProgress.value
        }
    }

    val currentSeconds = (currentProgress * video.durationSeconds).toInt()
    val timeFormatted = String.format("%02d:%02d / %02d:%02d",
        currentSeconds / 60, currentSeconds % 60,
        video.durationSeconds / 60, video.durationSeconds % 60
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0C101A))
            .border(1.dp, Color(0xFF222C44), RoundedCornerShape(16.dp))
            .padding(12.dp)
            .testTag("video_player_container")
    ) {
        // Video Stage with correct Aspect Ratio container
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            val aspect = when (video.aspectRatio) {
                AspectRatio.PORTRAIT_9_16 -> 9f / 16f
                AspectRatio.LANDSCAPE_16_9 -> 16f / 9f
                AspectRatio.SQUARE_1_1 -> 1.0f
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(if (video.aspectRatio == AspectRatio.PORTRAIT_9_16) 0.65f else 1.0f)
                    .aspectRatio(aspect)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { isPlaying = !isPlaying }
            ) {
                // Realistic Canvas Video Rendering matching chosen Style & Motion
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val progress = animProgress.value
                    drawVideoScene(
                        video = video,
                        progress = progress
                    )
                }

                // Free trial watermark badge
                if (video.isDemoMode) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Sree AI Studio",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Aspect Ratio indicator tag
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${video.aspectRatio.label} • ${video.resolution}",
                        color = StudioCyan,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Overlay Play/Pause button on tap
                if (!isPlaying) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = StudioCyan.copy(alpha = 0.9f),
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    tint = Color.Black,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Progress scrubber
        Slider(
            value = currentProgress,
            onValueChange = {
                isScrubbing = true
                currentProgress = it
            },
            onValueChangeFinished = {
                isScrubbing = false
                // update animProgress to scrubbed location
            },
            colors = SliderDefaults.colors(
                thumbColor = StudioCyan,
                activeTrackColor = StudioCyan,
                inactiveTrackColor = Color(0xFF283450)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .testTag("video_scrubber_slider")
        )

        // Player controls & timing
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { isPlaying = !isPlaying },
                    modifier = Modifier.size(36.dp).testTag("play_pause_button")
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = StudioCyan
                    )
                }

                IconButton(
                    onClick = onToggleAudio,
                    modifier = Modifier.size(36.dp).testTag("audio_toggle_button")
                ) {
                    Icon(
                        imageVector = if (isPlayingAudio && video.audioTrack != AudioTrackOption.NONE)
                            Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                        contentDescription = "Audio Toggle",
                        tint = if (isPlayingAudio && video.audioTrack != AudioTrackOption.NONE) StudioAmber else TextSecondary
                    )
                }

                Text(
                    text = timeFormatted,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { onToggleFavorite(video.id) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (video.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (video.isFavorite) Color(0xFFF43F5E) else TextSecondary
                    )
                }

                IconButton(
                    onClick = { onDownload(video) },
                    modifier = Modifier.size(36.dp).testTag("export_mp4_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Export MP4",
                        tint = StudioCyan
                    )
                }

                IconButton(
                    onClick = { onShare(video) },
                    modifier = Modifier.size(36.dp).testTag("share_video_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Prompt description & tags
        Text(
            text = video.prompt,
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            PillTag(label = video.style.titleEn, color = Color(video.style.accentHex))
            PillTag(label = video.motion.labelEn, color = StudioViolet)
            if (video.audioTrack != AudioTrackOption.NONE) {
                PillTag(label = "🎵 ${video.audioTrack.nameEn}", color = StudioAmber)
            }
        }
    }
}

@Composable
fun PillTag(label: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = label,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Draws dynamic canvas scenes matching the video's AI style and camera motion
 */
private fun DrawScope.drawVideoScene(video: VideoItem, progress: Float) {
    val w = size.width
    val h = size.height

    // Motion offset simulation based on CameraMotion
    val panX = when (video.motion) {
        CameraMotion.PAN_RIGHT -> (progress - 0.5f) * w * 0.4f
        CameraMotion.PAN_LEFT -> (0.5f - progress) * w * 0.4f
        CameraMotion.ORBIT -> sin(progress * 2.0 * Math.PI).toFloat() * w * 0.2f
        CameraMotion.DYNAMIC -> sin(progress * Math.PI).toFloat() * w * 0.15f
        else -> 0f
    }

    val zoomFactor = when (video.motion) {
        CameraMotion.ZOOM_IN -> 1.0f + progress * 0.25f
        CameraMotion.DYNAMIC -> 1.0f + (1f - progress) * 0.2f
        else -> 1.05f
    }

    // 1. Scene Background Gradient based on Style
    val bgBrush = when (video.style) {
        VideoStyle.BOLLYWOOD -> Brush.radialGradient(
            colors = listOf(Color(0xFF8B2500), Color(0xFF380800), Color(0xFF140200)),
            center = Offset(w * 0.5f + panX, h * 0.4f),
            radius = w * zoomFactor
        )
        VideoStyle.CINEMATIC_4K -> Brush.linearGradient(
            colors = listOf(Color(0xFF031424), Color(0xFF0B253A), Color(0xFF02070D)),
            start = Offset(panX, 0f),
            end = Offset(w + panX, h)
        )
        VideoStyle.DEVOTIONAL -> Brush.radialGradient(
            colors = listOf(Color(0xFFFFD700), Color(0xFFD97706), Color(0xFF451A03), Color(0xFF170800)),
            center = Offset(w * 0.5f, h * 0.35f + panX * 0.2f),
            radius = w * 0.8f * zoomFactor
        )
        VideoStyle.WEDDING -> Brush.linearGradient(
            colors = listOf(Color(0xFF4C0519), Color(0xFF881337), Color(0xFF1F030B)),
            start = Offset(0f, 0f),
            end = Offset(w, h)
        )
        VideoStyle.COMEDY -> Brush.linearGradient(
            colors = listOf(Color(0xFF0284C7), Color(0xFF0284C7), Color(0xFF0F172A)),
            start = Offset(0f, panX),
            end = Offset(w, h)
        )
        VideoStyle.CARTOON -> Brush.radialGradient(
            colors = listOf(Color(0xFF7C3AED), Color(0xFF4C1D95), Color(0xFF1E1B4B)),
            center = Offset(w * 0.5f + panX, h * 0.5f),
            radius = w * zoomFactor
        )
        VideoStyle.REELS_SHORTS -> Brush.linearGradient(
            colors = listOf(Color(0xFF06B6D4), Color(0xFF4F46E5), Color(0xFF090D16)),
            start = Offset(0f, 0f),
            end = Offset(w, h)
        )
        VideoStyle.REALISTIC -> Brush.verticalGradient(
            colors = listOf(Color(0xFF0D1B2A), Color(0xFF1B263B), Color(0xFF0D1B2A))
        )
    }

    drawRect(brush = bgBrush, size = size)

    // 2. Horizon lines or perspective depth grid
    val gridColor = Color.White.copy(alpha = 0.08f)
    for (i in 1..5) {
        val yPos = h * (0.5f + i * 0.09f)
        drawLine(
            color = gridColor,
            start = Offset(0f, yPos),
            end = Offset(w, yPos),
            strokeWidth = 1.5f
        )
    }

    // 3. Style-specific dynamic atmospheric elements
    when (video.style) {
        VideoStyle.BOLLYWOOD, VideoStyle.WEDDING -> {
            // Floating golden flower petals & bokeh embers
            for (i in 0..18) {
                val seedOffset = (video.visualSeed + i * 37) % 1000
                val px = ((seedOffset.toFloat() / 1000f * w) + panX + (progress * 80f)) % w
                val py = (h * ((i * 0.055f + progress * 0.35f) % 1.0f))
                val radius = (3f + (i % 5) * 2f) * zoomFactor

                drawCircle(
                    color = if (i % 2 == 0) Color(0xFFFFD700).copy(alpha = 0.65f) else Color(0xFFFB7185).copy(alpha = 0.5f),
                    center = Offset(px, py),
                    radius = radius
                )
            }
            // Golden halo spotlight
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFFDF7D).copy(alpha = 0.35f), Color.Transparent),
                    center = Offset(w * 0.5f + panX, h * 0.45f),
                    radius = w * 0.45f
                ),
                center = Offset(w * 0.5f + panX, h * 0.45f),
                radius = w * 0.45f
            )
        }
        VideoStyle.DEVOTIONAL -> {
            // Sacred radiating light beams & Diya flames
            val centerGlow = Offset(w * 0.5f, h * 0.4f)
            for (deg in 0..11) {
                val angleRad = (deg * 30.0 + progress * 20.0) * Math.PI / 180.0
                val beamEnd = Offset(
                    centerGlow.x + (w * 0.8f * cos(angleRad)).toFloat(),
                    centerGlow.y + (h * 0.8f * sin(angleRad)).toFloat()
                )
                drawLine(
                    color = Color(0xFFFFE082).copy(alpha = 0.20f),
                    start = centerGlow,
                    end = beamEnd,
                    strokeWidth = 14f
                )
            }
            // Central sacred aura
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFFFBEB), Color(0xFFF59E0B).copy(alpha = 0.7f), Color.Transparent),
                    center = centerGlow,
                    radius = w * 0.35f
                ),
                center = centerGlow,
                radius = w * 0.35f
            )
        }
        VideoStyle.CINEMATIC_4K, VideoStyle.REELS_SHORTS -> {
            // Horizontal Anamorphic Lens Flare
            val flareY = h * 0.45f
            val flarePulse = 0.7f + 0.3f * sin(progress * 2.0 * Math.PI).toFloat()
            drawOval(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Transparent, Color(0xFF00E5FF).copy(alpha = 0.7f * flarePulse), Color.White, Color(0xFF00E5FF).copy(alpha = 0.7f * flarePulse), Color.Transparent)
                ),
                topLeft = Offset(panX, flareY - 4.dp.toPx()),
                size = Size(w, 8.dp.toPx())
            )
            // Cyber/City glowing vertical accents
            for (i in 0..7) {
                val lineX = (w * (i * 0.14f) + panX) % w
                val alpha = (0.2f + 0.3f * sin(i + progress * 4.0)).toFloat().coerceIn(0.1f, 0.5f)
                drawLine(
                    color = if (i % 2 == 0) StudioCyan.copy(alpha = alpha) else StudioViolet.copy(alpha = alpha),
                    start = Offset(lineX, h * 0.2f),
                    end = Offset(lineX, h * 0.85f),
                    strokeWidth = 3f
                )
            }
        }
        VideoStyle.CARTOON, VideoStyle.COMEDY -> {
            // Whimsical bouncy stars and bubbles
            for (i in 0..12) {
                val bx = (w * (0.1f + i * 0.08f) + panX) % w
                val bounce = sin(progress * 6.0 + i).toFloat() * 18.dp.toPx()
                val by = h * 0.5f + bounce
                drawCircle(
                    color = if (i % 3 == 0) StudioAmber else if (i % 3 == 1) StudioCyan else StudioViolet,
                    center = Offset(bx, by),
                    radius = 8.dp.toPx() * (0.8f + 0.2f * sin(i.toDouble()).toFloat())
                )
            }
        }
        VideoStyle.REALISTIC -> {
            // Natural cinematic contrast and focal circle
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White.copy(alpha = 0.15f), Color.Transparent),
                    center = Offset(w * 0.5f + panX, h * 0.5f),
                    radius = w * 0.45f
                ),
                center = Offset(w * 0.5f + panX, h * 0.5f),
                radius = w * 0.45f
            )
        }
    }
}
