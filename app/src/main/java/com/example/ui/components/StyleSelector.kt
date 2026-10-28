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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Language
import com.example.model.VideoStyle
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioDarkCard
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun StyleSelector(
    selectedStyle: VideoStyle,
    language: Language,
    onStyleSelected: (VideoStyle) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(VideoStyle.entries) { style ->
            val isSelected = style == selectedStyle
            val accent = Color(style.accentHex)

            Box(
                modifier = Modifier
                    .width(136.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (isSelected) Color(0xFF192238) else StudioDarkCard
                    )
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) accent else Color(0xFF283450),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .clickable { onStyleSelected(style) }
                    .padding(10.dp)
                    .testTag("style_item_${style.id}")
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(accent.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = getStyleIcon(style.iconName),
                                contentDescription = style.titleEn,
                                tint = accent,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(accent)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = style.getTitle(language),
                        color = if (isSelected) TextPrimary else TextSecondary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = style.description,
                        color = Color(0xFF718096),
                        fontSize = 10.sp,
                        lineHeight = 12.sp,
                        maxLines = 2
                    )
                }
            }
        }
    }
}

private fun getStyleIcon(iconName: String): ImageVector {
    return when (iconName) {
        "Movie" -> Icons.Default.Movie
        "CameraAlt" -> Icons.Default.CameraAlt
        "Celebration" -> Icons.Default.Celebration
        "Favorite" -> Icons.Default.Favorite
        "LightMode" -> Icons.Default.LightMode
        "Mood" -> Icons.Default.Mood
        "Animation" -> Icons.Default.Animation
        "Share" -> Icons.Default.Share
        else -> Icons.Default.Movie
    }
}
