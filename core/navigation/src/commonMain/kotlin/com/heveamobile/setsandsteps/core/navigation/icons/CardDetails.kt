package com.heveamobile.setsandsteps.core.navigation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
val ic_card_details: ImageVector
    get() {
        if (_cardDetails != null) {
            return _cardDetails!!
        }
        _cardDetails = ImageVector
            .Builder(
                name = "card_details",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
            .apply {
                path(
                    fill = null,
                    fillAlpha = 1f,
                    stroke = SolidColor(Color.Black),
                    strokeAlpha = 1f,
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero,
                ) {
                    moveTo(
                        7.8f,
                        2.2f,
                    )
                    horizontalLineTo(16.2f)
                    arcTo(
                        2.8f,
                        2.8f,
                        0f,
                        false,
                        true,
                        19f,
                        5f,
                    )
                    verticalLineTo(19f)
                    arcTo(
                        2.8f,
                        2.8f,
                        0f,
                        false,
                        true,
                        16.2f,
                        21.8f,
                    )
                    horizontalLineTo(7.8f)
                    arcTo(
                        2.8f,
                        2.8f,
                        0f,
                        false,
                        true,
                        5f,
                        19f,
                    )
                    verticalLineTo(5f)
                    arcTo(
                        2.8f,
                        2.8f,
                        0f,
                        false,
                        true,
                        7.8f,
                        2.2f,
                    )
                    close()
                    moveTo(
                        5f,
                        16.2f,
                    )
                    horizontalLineTo(19f)
                }
            }
            .build()
        return _cardDetails!!
    }

private var _cardDetails: ImageVector? = null
