package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import coil.compose.AsyncImage
import com.example.data.PromptAssistantData
import com.example.model.AspectRatio
import com.example.model.AudioTrackOption
import com.example.model.CameraMotion
import com.example.model.StringsHelper
import com.example.ui.StudioViewModel
import com.example.ui.components.StyleSelector
import com.example.ui.theme.GradientStudio
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioCardBorder
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioDarkCard
import com.example.ui.theme.StudioDarkSurface
import com.example.ui.theme.StudioPink
import com.example.ui.theme.StudioViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CreateScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val promptInput by viewModel.promptInput.collectAsState()
    val selectedStyle by viewModel.selectedStyle.collectAsState()
    val selectedAspectRatio by viewModel.selectedAspectRatio.collectAsState()
    val selectedDuration by viewModel.selectedDuration.collectAsState()
    val selectedMotion by viewModel.selectedMotion.collectAsState()
    val selectedAudioTrack by viewModel.selectedAudioTrack.collectAsState()
    val isImageToVideo by viewModel.isImageToVideoTab.collectAsState()
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()
    val motionIntensity by viewModel.motionIntensity.collectAsState()

    val language = userProfile.selectedLanguage
    val scrollState = rememberScrollState()

    // Photo picker launcher (standard Android zero-permission contract)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.selectedImageUri.value = uri.toString()
        }
    }

    val cost = when (selectedDuration) {
        5 -> 5
        10 -> 8
        15 -> 12
        else -> 5
    }

    var showMotionMenu by remember { mutableStateOf(false) }
    var showAudioMenu by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Mode Switcher Tabs: Text to Video | Image to Video
        TabRow(
            selectedTabIndex = if (isImageToVideo) 1 else 0,
            containerColor = StudioDarkCard,
            contentColor = StudioCyan,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[if (isImageToVideo) 1 else 0]),
                    color = StudioCyan,
                    height = 3.dp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, StudioCardBorder, RoundedCornerShape(12.dp))
        ) {
            Tab(
                selected = !isImageToVideo,
                onClick = { viewModel.isImageToVideoTab.value = false },
                modifier = Modifier.testTag("tab_text_to_video"),
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (!isImageToVideo) StudioCyan else TextSecondary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = StringsHelper.t("text_to_video", language),
                            fontWeight = if (!isImageToVideo) FontWeight.Bold else FontWeight.Normal,
                            color = if (!isImageToVideo) StudioCyan else TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            )
            Tab(
                selected = isImageToVideo,
                onClick = { viewModel.isImageToVideoTab.value = true },
                modifier = Modifier.testTag("tab_image_to_video"),
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Photo,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (isImageToVideo) StudioPink else TextSecondary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = StringsHelper.t("image_to_video", language),
                            fontWeight = if (isImageToVideo) FontWeight.Bold else FontWeight.Normal,
                            color = if (isImageToVideo) StudioPink else TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Demo Mode Banner
        if (userProfile.customApiKey.isBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E170A), RoundedCornerShape(10.dp))
                    .border(1.dp, StudioAmber.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "⚡", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = StringsHelper.t("demo_mode_active", language),
                            color = StudioAmber,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = StringsHelper.t("demo_mode_desc", language),
                            color = Color(0xFFD4A373),
                            fontSize = 10.sp,
                            lineHeight = 13.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // 24-Hour Daily Check-In Available Banner
        if (userProfile.isCheckInAvailable) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF2E2210), Color(0xFF1B2438))
                        )
                    )
                    .border(1.dp, StudioAmber, RoundedCornerShape(12.dp))
                    .clickable { viewModel.openDailyCheckIn() }
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .testTag("daily_checkin_banner")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🎁", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = StringsHelper.t("daily_checkin_title", language),
                                color = StudioAmber,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Daily tokens ready to claim • Tap to collect",
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(StudioAmber)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "Claim",
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Image to Video Upload Section
        if (isImageToVideo) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(StudioDarkCard)
                    .border(1.dp, StudioCardBorder, RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = StringsHelper.t("upload_photo", language),
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Upload Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF0F1522))
                        .border(1.dp, StudioViolet.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .clickable {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                        .testTag("upload_image_picker_box"),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected Source Image",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = "Pick Image",
                                tint = StudioViolet,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Tap to choose from Gallery",
                                color = StudioViolet,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "PNG, JPG up to 20MB",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Sample Photo Presets
                Text(
                    text = StringsHelper.t("use_sample_photo", language),
                    color = TextSecondary,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val presets = listOf(
                        "Bride" to "https://images.unsplash.com/photo-1583939003579-730e3918a45a?w=400",
                        "Palace" to "https://images.unsplash.com/photo-1599661046289-e31897846e41?w=400",
                        "Cyber Car" to "https://images.unsplash.com/photo-1503376780353-7e6692767b70?w=400",
                        "Temple" to "https://images.unsplash.com/photo-1544717305-2782549b5136?w=400",
                        "Puppy" to "https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?w=400"
                    )
                    presets.forEach { (label, sampleUrl) ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF202A40))
                                .clickable { viewModel.selectedImageUri.value = sampleUrl }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = label, color = StudioCyan, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Motion intensity slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = StringsHelper.t("motion_strength", language),
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "$motionIntensity / 10",
                        color = StudioCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Slider(
                    value = motionIntensity.toFloat(),
                    onValueChange = { viewModel.motionIntensity.value = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 8,
                    colors = SliderDefaults.colors(
                        thumbColor = StudioPink,
                        activeTrackColor = StudioPink,
                        inactiveTrackColor = Color(0xFF283450)
                    )
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Prompt Input Field & Magic Expand Button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(StudioDarkCard)
                .border(1.dp, StudioCardBorder, RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isImageToVideo) "Motion & Animation Description" else "Video Prompt",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                // Magic Expand Button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(GradientStudio)
                        .clickable { viewModel.magicExpandPrompt() }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .testTag("magic_expand_button"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Magic Expand",
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = StringsHelper.t("magic_expand", language),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = promptInput,
                onValueChange = { viewModel.promptInput.value = it },
                placeholder = {
                    Text(
                        text = StringsHelper.t("prompt_hint", language),
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .testTag("prompt_input_field"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = StudioCyan,
                    unfocusedBorderColor = Color(0xFF283450),
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = Color(0xFF0F1522),
                    unfocusedContainerColor = Color(0xFF0F1522)
                ),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Enhancer Tags
            Text(
                text = "Quick Enhancers:",
                color = TextSecondary,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                PromptAssistantData.quickEnhancers.forEach { enhancer ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF1B2438))
                            .border(1.dp, Color(0xFF2A3755), RoundedCornerShape(16.dp))
                            .clickable { viewModel.appendEnhancerTag(enhancer) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = enhancer,
                            color = StudioCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // AI Video Style Selector (8 styles)
        Text(
            text = StringsHelper.t("select_style", language),
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        StyleSelector(
            selectedStyle = selectedStyle,
            language = language,
            onStyleSelected = { viewModel.selectedStyle.value = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Aspect Ratio Selector
        Text(
            text = StringsHelper.t("aspect_ratio", language),
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AspectRatio.entries.forEach { ratio ->
                val isSelected = ratio == selectedAspectRatio
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) Color(0xFF1B253D) else StudioDarkCard)
                        .border(
                            1.dp,
                            if (isSelected) StudioCyan else StudioCardBorder,
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { viewModel.selectedAspectRatio.value = ratio }
                        .padding(vertical = 10.dp, horizontal = 4.dp)
                        .testTag("ratio_${ratio.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = ratio.label,
                            color = if (isSelected) StudioCyan else TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = ratio.sublabel,
                            color = TextSecondary,
                            fontSize = 9.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Duration Options (5s, 10s, 15s)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = StringsHelper.t("duration", language),
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = "100% Free (मुफ़्त)",
                color = StudioCyan,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(5, 10, 15).forEach { seconds ->
                val isSelected = seconds == selectedDuration
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) Color(0xFF251F33) else StudioDarkCard)
                        .border(
                            1.dp,
                            if (isSelected) StudioViolet else StudioCardBorder,
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { viewModel.selectedDuration.value = seconds }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${seconds}s",
                            color = if (isSelected) StudioViolet else TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Free",
                            color = StudioCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Camera Motion & Audio Settings Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Camera Motion Selector
            Box(modifier = Modifier.weight(1f)) {
                Column {
                    Text(
                        text = StringsHelper.t("camera_motion", language),
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(StudioDarkCard)
                            .border(1.dp, StudioCardBorder, RoundedCornerShape(10.dp))
                            .clickable { showMotionMenu = true }
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                            .testTag("camera_motion_selector")
                    ) {
                        Text(
                            text = selectedMotion.getLabel(language),
                            color = StudioCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                    }

                    DropdownMenu(
                        expanded = showMotionMenu,
                        onDismissRequest = { showMotionMenu = false },
                        modifier = Modifier.background(Color(0xFF161F33))
                    ) {
                        CameraMotion.entries.forEach { motion ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = motion.getLabel(language),
                                        color = if (motion == selectedMotion) StudioCyan else TextPrimary
                                    )
                                },
                                onClick = {
                                    viewModel.selectedMotion.value = motion
                                    showMotionMenu = false
                                }
                            )
                        }
                    }
                }
            }

            // Audio & Music Selector
            Box(modifier = Modifier.weight(1f)) {
                Column {
                    Text(
                        text = StringsHelper.t("audio_track", language),
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(StudioDarkCard)
                            .border(1.dp, StudioCardBorder, RoundedCornerShape(10.dp))
                            .clickable { showAudioMenu = true }
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                            .testTag("audio_track_selector")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = null,
                                tint = StudioAmber,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = selectedAudioTrack.getName(language),
                                color = StudioAmber,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showAudioMenu,
                        onDismissRequest = { showAudioMenu = false },
                        modifier = Modifier.background(Color(0xFF161F33))
                    ) {
                        AudioTrackOption.entries.forEach { track ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = track.getName(language),
                                        color = if (track == selectedAudioTrack) StudioAmber else TextPrimary
                                    )
                                },
                                onClick = {
                                    viewModel.selectedAudioTrack.value = track
                                    showAudioMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Main Generate Button
        Button(
            onClick = { viewModel.startGeneration() },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(GradientStudio)
                .testTag("generate_video_button"),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = StringsHelper.t("generate_button", language),
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}
