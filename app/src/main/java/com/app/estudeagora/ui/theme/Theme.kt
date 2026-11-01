package com.app.estudeagora.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// O app usa sempre o tema escuro, independente do tema do sistema.
private val AppColorScheme = darkColorScheme(
    primary = Accent,
    onPrimary = OnAccent,
    secondary = Accent,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceHigh,
    onSurfaceVariant = TextSecondary,
    outline = Outline,
    outlineVariant = Outline
)

@Composable
fun EstudeAgoraTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}
