package com.heveamobile.setsandsteps.core.navigation.icons


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

//https://fonts.gstatic.com/render/v1/Material+Symbols+Outlined/24dp/book_5.kt?var=opsz,wght,FILL,GRAD,ROND@24,400,0,0,50

@Suppress("CheckReturnValue")
val ic_cards: ImageVector
    get() {
        if (_cards != null) {
            return _cards!!
        }
        _cards = ImageVector
            .Builder(
                name = "cards",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
            .apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1f,
                    stroke = null,
                    strokeAlpha = 1f,
                    strokeLineWidth = 1f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Bevel,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero,
                ) {
                    moveTo(
                        6.75f,
                        22f,
                    )
                    quadTo(
                        5.63f,
                        22f,
                        4.81f,
                        21.24f,
                    )
                    reflectiveQuadTo(
                        4f,
                        19.35f,
                    )
                    verticalLineTo(5.4f)
                    quadTo(
                        4f,
                        4.45f,
                        4.59f,
                        3.7f,
                    )
                    reflectiveQuadTo(
                        6.13f,
                        2.75f,
                    )
                    lineTo(
                        16f,
                        0.8f,
                    )
                    verticalLineToRelative(16f)
                    lineTo(
                        6.53f,
                        18.7f,
                    )
                    quadTo(
                        6.3f,
                        18.75f,
                        6.15f,
                        18.94f,
                    )
                    reflectiveQuadTo(
                        6f,
                        19.35f,
                    )
                    quadToRelative(
                        0f,
                        0.27f,
                        0.23f,
                        0.46f,
                    )
                    reflectiveQuadTo(
                        6.75f,
                        20f,
                    )
                    horizontalLineTo(18f)
                    verticalLineTo(4f)
                    horizontalLineToRelative(2f)
                    verticalLineTo(22f)
                    horizontalLineTo(6.75f)
                    close()
                    moveTo(
                        9f,
                        16.18f,
                    )
                    lineTo(
                        14f,
                        15.2f,
                    )
                    verticalLineTo(3.25f)
                    lineTo(
                        9f,
                        4.22f,
                    )
                    verticalLineTo(16.18f)
                    close()
                    moveToRelative(
                        -2f,
                        0.4f,
                    )
                    verticalLineTo(4.63f)
                    lineTo(
                        6.63f,
                        4.7f,
                    )
                    quadTo(
                        6.35f,
                        4.75f,
                        6.18f,
                        4.94f,
                    )
                    reflectiveQuadTo(
                        6f,
                        5.4f,
                    )
                    verticalLineTo(16.83f)
                    quadTo(
                        6.13f,
                        16.77f,
                        6.26f,
                        16.74f,
                    )
                    reflectiveQuadTo(
                        6.53f,
                        16.68f,
                    )
                    lineTo(
                        7f,
                        16.58f,
                    )
                    close()
                    moveTo(
                        6f,
                        4.77f,
                    )
                    verticalLineTo(16.83f)
                    verticalLineTo(4.77f)
                    close()
                }
            }
            .build()
        return _cards!!
    }

private var _cards: ImageVector? = null
