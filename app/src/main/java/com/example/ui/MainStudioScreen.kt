package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.GenerationUiState
import com.example.model.StringsHelper
import com.example.ui.components.DailyCheckInDialog
import com.example.ui.components.ProgressDialog
import com.example.ui.components.TopStudioBar
import com.example.ui.screens.CreateScreen
import com.example.ui.screens.GalleryScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.PromptLabScreen
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioDarkBg
import com.example.ui.theme.StudioDarkCard
import com.example.ui.theme.StudioDarkSurface
import com.example.ui.theme.StudioViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun MainStudioScreen(
    viewModel: StudioViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val generationState by viewModel.generationState.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()
    val showDailyCheckInModal by viewModel.showDailyCheckInModal.collectAsState()

    val language = userProfile.selectedLanguage
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        containerColor = StudioDarkBg,
        topBar = {
            TopStudioBar(
                userProfile = userProfile,
                onLanguageSelected = { viewModel.setLanguage(it) },
                onDailyCheckInClick = { viewModel.openDailyCheckIn() },
                onCreditsClick = { viewModel.openDailyCheckIn() }
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .border(1.dp, Color(0xFF1E283E), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .testTag("bottom_navigation_bar"),
                containerColor = StudioDarkSurface,
                contentColor = StudioCyan
            ) {
                NavigationItemData(
                    tab = StudioTab.CREATE,
                    icon = Icons.Default.Videocam,
                    label = StringsHelper.t("tab_create", language),
                    tag = "nav_create"
                ).let { item ->
                    NavigationBarItem(
                        selected = currentTab == item.tab,
                        onClick = { viewModel.setTab(item.tab) },
                        icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                        label = { Text(text = item.label, fontSize = 11.sp, fontWeight = if (currentTab == item.tab) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = StudioCyan,
                            selectedTextColor = StudioCyan,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = Color(0xFF13283E)
                        ),
                        modifier = Modifier.testTag(item.tag)
                    )
                }

                NavigationItemData(
                    tab = StudioTab.GALLERY,
                    icon = Icons.Default.VideoLibrary,
                    label = StringsHelper.t("tab_gallery", language),
                    tag = "nav_gallery"
                ).let { item ->
                    NavigationBarItem(
                        selected = currentTab == item.tab,
                        onClick = { viewModel.setTab(item.tab) },
                        icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                        label = { Text(text = item.label, fontSize = 11.sp, fontWeight = if (currentTab == item.tab) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = StudioCyan,
                            selectedTextColor = StudioCyan,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = Color(0xFF13283E)
                        ),
                        modifier = Modifier.testTag(item.tag)
                    )
                }

                NavigationItemData(
                    tab = StudioTab.PROMPTS,
                    icon = Icons.Default.Psychology,
                    label = StringsHelper.t("tab_prompts", language),
                    tag = "nav_prompts"
                ).let { item ->
                    NavigationBarItem(
                        selected = currentTab == item.tab,
                        onClick = { viewModel.setTab(item.tab) },
                        icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                        label = { Text(text = item.label, fontSize = 11.sp, fontWeight = if (currentTab == item.tab) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = StudioCyan,
                            selectedTextColor = StudioCyan,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = Color(0xFF13283E)
                        ),
                        modifier = Modifier.testTag(item.tag)
                    )
                }

                NavigationItemData(
                    tab = StudioTab.PROFILE,
                    icon = Icons.Default.Person,
                    label = StringsHelper.t("tab_profile", language),
                    tag = "nav_profile"
                ).let { item ->
                    NavigationBarItem(
                        selected = currentTab == item.tab,
                        onClick = { viewModel.setTab(item.tab) },
                        icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                        label = { Text(text = item.label, fontSize = 11.sp, fontWeight = if (currentTab == item.tab) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = StudioCyan,
                            selectedTextColor = StudioCyan,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = Color(0xFF13283E)
                        ),
                        modifier = Modifier.testTag(item.tag)
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentTab) {
                StudioTab.CREATE -> CreateScreen(viewModel = viewModel)
                StudioTab.GALLERY -> GalleryScreen(viewModel = viewModel)
                StudioTab.PROMPTS -> PromptLabScreen(viewModel = viewModel)
                StudioTab.PROFILE -> ProfileScreen(viewModel = viewModel)
            }
        }
    }

    // Active Generation Dialog
    when (val state = generationState) {
        is GenerationUiState.Progress -> {
            ProgressDialog(
                state = state,
                language = language,
                onCancel = { viewModel.dismissGenerationState() }
            )
        }
        is GenerationUiState.Success -> {
            AlertDialog(
                onDismissRequest = { viewModel.dismissGenerationState() },
                containerColor = Color(0xFF0F1523),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = StudioCyan,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "🎉 Video Generation Complete!",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }
                },
                text = {
                    Text(
                        text = "Your AI video has been rendered and added to your gallery. Would you like to watch it now?",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.dismissGenerationState()
                            viewModel.openVideoPlayer(state.video)
                            viewModel.setTab(StudioTab.GALLERY)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                        modifier = Modifier.testTag("watch_video_confirm_button")
                    ) {
                        Text(text = "Watch in Player", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.dismissGenerationState() }) {
                        Text(text = "Close", color = TextSecondary)
                    }
                }
            )
        }
        is GenerationUiState.Error -> {
            AlertDialog(
                onDismissRequest = { viewModel.dismissGenerationState() },
                containerColor = Color(0xFF1E1014),
                title = {
                    Text(
                        text = "Notice",
                        color = Color(0xFFF43F5E),
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = state.errorMessage,
                        color = TextPrimary,
                        fontSize = 13.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.dismissGenerationState()
                            viewModel.setTab(StudioTab.PROFILE)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StudioAmber)
                    ) {
                        Text("Add Credits / Settings", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.dismissGenerationState() }) {
                        Text("Dismiss", color = TextSecondary)
                    }
                }
            )
        }
        is GenerationUiState.Idle -> {}
    }

    if (showDailyCheckInModal) {
        DailyCheckInDialog(
            userProfile = userProfile,
            onClaim = { viewModel.claimDailyCheckIn() },
            onResetDemo = { viewModel.resetDailyTimerDemo() },
            onDismiss = { viewModel.closeDailyCheckIn() }
        )
    }
}

private data class NavigationItemData(
    val tab: StudioTab,
    val icon: ImageVector,
    val label: String,
    val tag: String
)
