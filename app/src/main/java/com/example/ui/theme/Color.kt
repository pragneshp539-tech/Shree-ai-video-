package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val StudioDarkBg = Color(0xFF090D16)
val StudioDarkSurface = Color(0xFF111726)
val StudioDarkCard = Color(0xFF182033)
val StudioCardBorder = Color(0xFF283450)

val StudioCyan = Color(0xFF00E5FF)
val StudioViolet = Color(0xFFA855F7)
val StudioPink = Color(0xFFEC4899)
val StudioAmber = Color(0xFFF59E0B)
val StudioEmerald = Color(0xFF10B981)

val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

val GradientStudio = Brush.horizontalGradient(
    colors = listOf(StudioCyan, StudioViolet, StudioPink)
)

val GradientGolden = Brush.horizontalGradient(
    colors = listOf(Color(0xFFF59E0B), Color(0xFFF97316), Color(0xFFE11D48))
)

val GradientCyanDark = Brush.verticalGradient(
    colors = listOf(Color(0xFF0F2B48), StudioDarkBg)
)
