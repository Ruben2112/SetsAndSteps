package com.heveamobile.setsandsteps.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.Res
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.tomorrow_regular
import org.jetbrains.compose.resources.Font

val Tomorrow
    @Composable get() = FontFamily(
        Font(
            resource = Res.font.tomorrow_regular,
            weight = FontWeight.Normal,
        ),
    )

val Typography: Typography
    @Composable get() {
        val defaultTypography = Typography()
        return Typography(
            titleLarge = defaultTypography.titleLarge.copy(fontFamily = Tomorrow),
            titleMedium = defaultTypography.titleMedium.copy(fontFamily = Tomorrow),
            titleSmall = defaultTypography.titleSmall.copy(fontFamily = Tomorrow),
            bodySmall = defaultTypography.bodySmall.copy(fontFamily = Tomorrow),
            bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = Tomorrow),
            bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = Tomorrow),
        )
    }