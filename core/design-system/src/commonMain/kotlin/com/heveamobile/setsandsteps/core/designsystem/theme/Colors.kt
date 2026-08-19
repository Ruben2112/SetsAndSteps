package com.heveamobile.setsandsteps.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import com.heveamobile.setsandsteps.core.domain.model.Rarity

// https://coolors.co/palette/f0ead2-dde5b6-adc178-a98467-6c584c
private val VanillaCream = Color(color = 0xFFF0EAD2)

// Updated from original to be between MutedOlive and VanillaCream
private val Cream = Color(color = 0xFFDDE5B6)
private val MutedOlive = Color(color = 0xFFADC178)
private val FadedCopper = Color(color = 0xFFA98467)
private val AshBrown = Color(color = 0xFF6C584C)
private val CoffeeBean = Color(color = 0xFF362C26) // 50% #000000 (Black), 50% AshBrown

private val Gray0 = Color(color = 0xFF000000)
private val Gray10 = Color(color = 0xFF333333)
private val Gray20 = Color(color = 0xFF4C4C4C)
private val Gray30 = Color(color = 0xFF666666)
private val Gray40 = Color(color = 0xFF808080)
private val Gray50 = Color(color = 0xFF999999)
private val Gray60 = Color(color = 0xFFB3B3B3)
private val Gray70 = Color(color = 0xFFCCCCCC)
private val Gray80 = Color(color = 0xFFE6E6E6)
private val Gray90 = Color(color = 0xFFF2F2F2)
private val Gray100 = Color(color = 0xFFFFFFFF)

val lightScheme = lightColorScheme(
    primary = MutedOlive,
    onPrimary = AshBrown,
    primaryContainer = Cream,
    onPrimaryContainer = AshBrown,
    secondary = Cream,
    onSecondary = AshBrown,
    secondaryContainer = MutedOlive,
    onSecondaryContainer = AshBrown,
    tertiary = AshBrown,
    onTertiary = Cream,
    tertiaryContainer = Color.Transparent,
    onTertiaryContainer = AshBrown,
    error = lerp(
        Cream,
        Color(color = 0xFFFF0000),
        0.2F,
    ),
    onError = lerp(
        AshBrown,
        Color(color = 0xFFFF0000),
        0.5F,
    ),
    errorContainer = lerp(
        Cream,
        Color(color = 0xFFFF0000),
        0.2F,
    ),
    onErrorContainer = lerp(
        AshBrown,
        Color(color = 0xFFFF0000),
        0.5F,
    ),
    background = VanillaCream,
    onBackground = AshBrown,
    surface = VanillaCream,
    onSurface = AshBrown,
    surfaceVariant = FadedCopper,
    onSurfaceVariant = AshBrown,
    outline = MutedOlive,
    outlineVariant = AshBrown,
    scrim = Gray0,
    inverseSurface = AshBrown,
    inverseOnSurface = VanillaCream,
    inversePrimary = VanillaCream,
    surfaceDim = MutedOlive,
    surfaceBright = Gray0,
    surfaceContainerLowest = VanillaCream,
    surfaceContainerLow = VanillaCream,
    surfaceContainer = Cream,
    surfaceContainerHigh = MutedOlive,
    surfaceContainerHighest = MutedOlive,
)

val darkScheme = darkColorScheme(
    primary = AshBrown,
    onPrimary = VanillaCream,
    primaryContainer = CoffeeBean,
    onPrimaryContainer = MutedOlive,
    secondary = MutedOlive,
    onSecondary = AshBrown,
    secondaryContainer = MutedOlive,
    onSecondaryContainer = AshBrown,
    tertiary = MutedOlive,
    onTertiary = AshBrown,
    tertiaryContainer = Color.Transparent,
    onTertiaryContainer = AshBrown,
    error = lerp(
        FadedCopper,
        Color(color = 0xFFFF0000),
        0.2F,
    ),
    onError = lerp(
        CoffeeBean,
        Color(color = 0xFFFF0000),
        0.5F,
    ),
    errorContainer = lerp(
        FadedCopper,
        Color(color = 0xFFFF0000),
        0.2F,
    ),
    onErrorContainer = lerp(
        CoffeeBean,
        Color(color = 0xFFFF0000),
        0.5F,
    ),
    background = Gray0,
    onBackground = FadedCopper,
    surface = Gray0,
    onSurface = MutedOlive,
    surfaceVariant = Gray10,
    onSurfaceVariant = Cream,
    outline = AshBrown,
    outlineVariant = AshBrown,
    scrim = Gray0,
    inverseSurface = VanillaCream,
    inverseOnSurface = AshBrown,
    inversePrimary = MutedOlive,
    surfaceDim = Gray0,
    surfaceBright = Gray10,
    surfaceContainerLowest = MutedOlive,
    surfaceContainerLow = MutedOlive,
    surfaceContainer = CoffeeBean,
    surfaceContainerHigh = MutedOlive,
    surfaceContainerHighest = VanillaCream,
)

fun Rarity.color(onSurfaceColor: Color): Color {
    return when (this) {
        Rarity.Common -> onSurfaceColor
        Rarity.Uncommon -> Color(0xFF00FF00)
        Rarity.Rare -> Color(0xFF0000FF)
        Rarity.Epic -> Color(0xFF800080)
        Rarity.Legendary -> Color(0xFFFF8000)
    }
}

//@Composable
//fun switchColors(): SwitchColors {
//    return SwitchDefaults
//        .colors()
//        .copy(
//            uncheckedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
//            uncheckedThumbColor = MaterialTheme.colorScheme.onPrimaryContainer,
//            uncheckedTrackColor = MaterialTheme.colorScheme.primaryContainer,
//            checkedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
//            checkedThumbColor = MaterialTheme.colorScheme.primaryContainer,
//            checkedTrackColor = MaterialTheme.colorScheme.onPrimaryContainer,
//        )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun timePickerColors(): TimePickerColors {
//    return TimePickerDefaults
//        .colors()
//        .copy(
//            clockDialColor = MaterialTheme.colorScheme.primaryContainer,
//            selectorColor = MaterialTheme.colorScheme.secondaryContainer,
//            clockDialSelectedContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
//            clockDialUnselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
//            periodSelectorBorderColor = MaterialTheme.colorScheme.primaryContainer,
//            periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
//            periodSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surface,
//            periodSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
//            periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
//            timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
//            timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surface,
//            timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
//            timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
//        )
//}
//
//@Composable
//fun sliderColors(): SliderColors {
//    return SliderDefaults
//        .colors()
//        .copy(
//            activeTrackColor = MaterialTheme.colorScheme.onPrimaryContainer,
//            inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
//            activeTickColor = Color.Transparent,
//            inactiveTickColor = Color.Transparent,
//        )
//}