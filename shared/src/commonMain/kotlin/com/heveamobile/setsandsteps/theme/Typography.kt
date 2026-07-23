package com.heveamobile.setsandsteps.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import setsandsteps.shared.generated.resources.Res
import setsandsteps.shared.generated.resources.tomorrow_regular

val Tomorrow
    @Composable get() = FontFamily(
        Font(
            resource = Res.font.tomorrow_regular,
            weight = FontWeight.Normal,
        ),
    )

val Typography: Typography
    @Composable get() = Typography(
        titleLarge = TextStyle(
            fontFamily = Tomorrow,
            fontSize = 24.sp,
        ),
        titleMedium = TextStyle(
            fontFamily = Tomorrow,
            fontSize = 18.sp,
        ),
        titleSmall = TextStyle(
            fontFamily = Tomorrow,
            fontSize = 14.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = Tomorrow,
            fontSize = 12.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = Tomorrow,
            fontSize = 14.sp,
            lineHeight = 16.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = Tomorrow,
            fontSize = 20.sp,
        ),
    )