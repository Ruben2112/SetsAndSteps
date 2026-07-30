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
val ic_statistics: ImageVector
    get() {
        if (_statistics != null) {
            return _statistics!!
        }
        _statistics =
            ImageVector
                .Builder(
                    name = "statistics",
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
                          2f,
                          21f,
                        )
                        verticalLineTo(19f)
                        horizontalLineTo(22f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(2f)
                        close()
                        moveTo(
                          3f,
                          18f,
                        )
                        verticalLineTo(11f)
                        horizontalLineTo(6f)
                        verticalLineToRelative(7f)
                        horizontalLineTo(3f)
                        close()
                        moveToRelative(
                          5f,
                          0f,
                        )
                        verticalLineTo(6f)
                        horizontalLineToRelative(3f)
                        verticalLineTo(18f)
                        horizontalLineTo(8f)
                        close()
                        moveToRelative(
                          5f,
                          0f,
                        )
                        verticalLineTo(9f)
                        horizontalLineToRelative(3f)
                        verticalLineToRelative(9f)
                        horizontalLineTo(13f)
                        close()
                        moveToRelative(
                          5f,
                          0f,
                        )
                        verticalLineTo(3f)
                        horizontalLineToRelative(3f)
                        verticalLineTo(18f)
                        horizontalLineTo(18f)
                        close()
                    }
                }
                .build()
        return _statistics!!
    }

private var _statistics: ImageVector? = null
