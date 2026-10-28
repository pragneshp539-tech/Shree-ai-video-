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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.model.AspectRatio
import com.example.model.StringsHelper
import com.example.model.VideoItem
import com.example.ui.StudioTab
import com.example.ui.StudioViewModel
import com.example.ui.components.VideoPlayerView
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioCardBorder
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioDarkCard
import com.example.ui.theme.StudioViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun GalleryScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val videoList by viewModel.videoList.collectAsState()
    val activePlayingVideo by viewModel.activePlayingVideo.collectAsState()
    val isPlayingAudio by viewModel.isPlayingAudio.collectAsState()

    val language = userProfile.selectedLanguage
    var selectedFilter by remember { mutableStateOf("All") }

    val filteredList = videoList.filter {
        when (selectedFilter) {
            "Favorites" -> it.isFavorite
            "Text to Video" -> !it.isImageToVideo
            "Image to Video" -> it.isImageToVideo
            else -> true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Active Video Player if selected
        if (activePlayingVideo != null) {
            VideoPlayerView(
                video = activePlayingVideo!!,
                isPlayingAudio = isPlayingAudio,
                onToggleAudio = { viewModel.toggleAudioPlayback(activePlayingVideo!!) },
                onToggleFavorite = { viewModel.toggleFavorite(it) },
                onDownload = { viewModel.showSnackbar(StringsHelper.t("saved_to_gallery", language)) },
                onShare = { viewModel.showSnackbar("Sharing link generated for video") },
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        // Filter Chips row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            val filters = listOf("All", "Favorites", "Text to Video", "Image to Video")
            items(filters) { filter ->
                val isSelected = filter == selectedFilter
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedFilter = filter },
                    label = {
                        Text(
                            text = filter,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = StudioCyan,
                        selectedLabelColor = Color.Black,
                        containerColor = StudioDarkCard,
                        labelColor = TextSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = StudioCardBorder,
                        selectedBorderColor = StudioCyan
                    )
                )
            }
        }

        // Videos List
        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.VideoLibrary,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = StringsHelper.t("no_videos_yet", language),
                        color = TextSecondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.setTab(StudioTab.CREATE) },
                        colors = ButtonDefaults.buttonColors(containerColor = StudioCyan)
                    ) {
                        Text(
                            text = StringsHelper.t("generate_button", language),
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredList, key = { it.id }) { video ->
                    VideoCardItem(
                        video = video,
                        isPlaying = activePlayingVideo?.id == video.id,
                        onClick = { viewModel.openVideoPlayer(video) },
                        onToggleFavorite = { viewModel.toggleFavorite(video.id) },
                        onDelete = { viewModel.deleteVideo(video.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun VideoCardItem(
    video: VideoItem,
    isPlaying: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit
) {
    val styleAccent = Color(video.style.accentHex)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (isPlaying) Color(0xFF151C2C) else StudioDarkCard)
            .border(
                width = if (isPlaying) 1.5.dp else 1.dp,
                color = if (isPlaying) StudioCyan else StudioCardBorder,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(12.dp)
            .testTag("video_card_${video.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Video Thumbnail Box with aspect badge & play button
            Box(
                modifier = Modifier
                    .size(width = 90.dp, height = 70.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(styleAccent.copy(alpha = 0.5f), Color(0xFF090D16))
                        )
                    )
                    .border(1.dp, Color(0xFF283450), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Play icon pill
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Duration badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${video.durationSeconds}s",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Text Info & details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = video.style.titleEn,
                        color = styleAccent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = video.aspectRatio.label,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (video.isImageToVideo) "🖼️ Photo Animate" else "✍️ Text Prompt",
                        color = TextMuted,
                        fontSize = 10.sp
                    )

                    Row {
                        IconButton(
                            onClick = onToggleFavorite,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = if (video.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (video.isFavorite) Color(0xFFF43F5E) else TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
