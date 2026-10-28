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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PromptAssistantData
import com.example.data.PromptInspiration
import com.example.model.VideoStyle
import com.example.ui.StudioViewModel
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
fun PromptLabScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    var selectedCategory by remember { mutableStateOf("All") }
    var sandboxInput by remember { mutableStateOf("") }
    var expandedResult by remember { mutableStateOf("") }
    var sandboxStyle by remember { mutableStateOf(VideoStyle.CINEMATIC_4K) }

    val inspirations = PromptAssistantData.sampleInspirations.filter {
        selectedCategory == "All" || it.category.equals(selectedCategory, ignoreCase = true)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(GradientStudio)
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Prompt Assistant",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Turn simple words into award-winning cinematic director prompts with camera angles, lighting, motion, and color grading.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Live Sandbox Expander
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(StudioDarkCard)
                    .border(1.dp, StudioCardBorder, RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = "Prompt Expander Sandbox",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Enter a simple concept to see how AI expands it:",
                    color = TextSecondary,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = sandboxInput,
                    onValueChange = { sandboxInput = it },
                    placeholder = {
                        Text("e.g., Tiger walking through misty bamboo forest", color = TextMuted, fontSize = 12.sp)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .testTag("sandbox_prompt_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = StudioCyan,
                        unfocusedBorderColor = Color(0xFF283450),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = Color(0xFF0F1522),
                        unfocusedContainerColor = Color(0xFF0F1522)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        val idea = if (sandboxInput.isBlank()) "Tiger walking through misty bamboo forest" else sandboxInput
                        expandedResult = PromptAssistantData.expandPrompt(idea, sandboxStyle)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(GradientStudio)
                        .testTag("expand_sandbox_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Generate Director Prompt",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                if (expandedResult.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0C1322))
                            .border(1.dp, StudioCyan.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Enhanced Pro Prompt:",
                                    color = StudioCyan,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFF1E2840))
                                        .clickable {
                                            viewModel.applyInspiration(expandedResult, sandboxStyle)
                                        }
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Send to Studio →",
                                        color = StudioCyan,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = expandedResult,
                                color = TextPrimary,
                                fontSize = 12.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            Text(
                text = "Curated Inspirations",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(PromptAssistantData.categories) { cat ->
                    val isSelected = cat == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 12.sp) },
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
        }

        // Inspirations List
        items(inspirations) { item ->
            InspirationCard(
                inspiration = item,
                onUsePrompt = {
                    viewModel.applyInspiration(item.fullPrompt, item.recommendedStyle)
                }
            )
        }
    }
}

@Composable
fun InspirationCard(
    inspiration: PromptInspiration,
    onUsePrompt: () -> Unit
) {
    val accent = Color(inspiration.recommendedStyle.accentHex)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(StudioDarkCard)
            .border(1.dp, StudioCardBorder, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = inspiration.title,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Box(
                    modifier = Modifier
                        .background(accent.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = inspiration.recommendedStyle.titleEn,
                        color = accent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = inspiration.fullPrompt,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    inspiration.tags.forEach { tag ->
                        Text(
                            text = "#$tag",
                            color = Color(0xFF64748B),
                            fontSize = 10.sp
                        )
                    }
                }

                Button(
                    onClick = onUsePrompt,
                    colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(
                        text = "Use Prompt",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}
