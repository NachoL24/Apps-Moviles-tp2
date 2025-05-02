package com.example.juego_adivinar_numero_aleatorio.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable


val LightColorScheme = lightColorScheme(
    primary = BlackText,
    secondary = BlackText,
    background = WhiteBackground,
    onBackground = BlackText,
    surface = WhiteBackground,
    onSurface = BlackText
)


val DarkColorScheme = darkColorScheme(
    primary = DarkPurple,
    secondary = DarkGrey,
    background = BlackBackground,
    onBackground = WhiteText,
    surface = BlackBackground,
    onSurface = WhiteText
)

@Composable
fun JuegoadivinarnumeroaleatorioTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
