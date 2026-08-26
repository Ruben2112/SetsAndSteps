package com.heveamobile.setsandsteps.core.designsystem.theme

import androidx.compose.ui.graphics.Color
import com.heveamobile.setsandsteps.core.domain.model.Rarity

fun Rarity.color(onSurfaceColor: Color): Color {
    return when (this) {
        Rarity.Common -> onSurfaceColor
        Rarity.Uncommon -> Color(0xFF00FF00)
        Rarity.Rare -> Color(0xFF0000FF)
        Rarity.Epic -> Color(0xFF800080)
        Rarity.Legendary -> Color(0xFFFF8000)
    }
}