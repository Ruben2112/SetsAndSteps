package com.example.test

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
val ic_sets: ImageVector
    get() {
        if (_sets != null) {
            return _sets!!
        }
        _sets =
            ImageVector
                .Builder(
                    name = "browse",
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
                          3f,
                          13f,
                        )
                        verticalLineTo(5f)
                        quadTo(
                          3f,
                          4.17f,
                          3.59f,
                          3.59f,
                        )
                        reflectiveQuadTo(
                          5f,
                          3f,
                        )
                        horizontalLineToRelative(6f)
                        verticalLineTo(13f)
                        horizontalLineTo(3f)
                        close()
                        moveTo(
                          9f,
                          11f,
                        )
                        close()
                        moveTo(
                          13f,
                          3f,
                        )
                        horizontalLineToRelative(6f)
                        quadToRelative(
                          0.83f,
                          0f,
                          1.41f,
                          0.59f,
                        )
                        reflectiveQuadTo(
                          21f,
                          5f,
                        )
                        verticalLineTo(9f)
                        horizontalLineTo(13f)
                        verticalLineTo(3f)
                        close()
                        moveToRelative(
                          0f,
                          18f,
                        )
                        verticalLineTo(11f)
                        horizontalLineToRelative(8f)
                        verticalLineToRelative(8f)
                        quadToRelative(
                          0f,
                          0.82f,
                          -0.59f,
                          1.41f,
                        )
                        reflectiveQuadTo(
                          19f,
                          21f,
                        )
                        horizontalLineTo(13f)
                        close()
                        moveTo(
                          3f,
                          15f,
                        )
                        horizontalLineToRelative(8f)
                        verticalLineToRelative(6f)
                        horizontalLineTo(5f)
                        quadTo(
                          4.18f,
                          21f,
                          3.59f,
                          20.41f,
                        )
                        reflectiveQuadTo(
                          3f,
                          19f,
                        )
                        verticalLineTo(15f)
                        close()
                        moveToRelative(
                          6f,
                          2f,
                        )
                        close()
                        moveTo(
                          15f,
                          7f,
                        )
                        close()
                        moveToRelative(
                          0f,
                          6f,
                        )
                        close()
                        moveTo(
                          5f,
                          11f,
                        )
                        horizontalLineTo(9f)
                        verticalLineTo(5f)
                        horizontalLineTo(5f)
                        verticalLineToRelative(6f)
                        close()
                        moveTo(
                          15f,
                          7f,
                        )
                        horizontalLineToRelative(4f)
                        verticalLineTo(5f)
                        horizontalLineTo(15f)
                        verticalLineTo(7f)
                        close()
                        moveToRelative(
                          0f,
                          6f,
                        )
                        verticalLineToRelative(6f)
                        horizontalLineToRelative(4f)
                        verticalLineTo(13f)
                        horizontalLineTo(15f)
                        close()
                        moveTo(
                          5f,
                          17f,
                        )
                        verticalLineToRelative(2f)
                        horizontalLineTo(9f)
                        verticalLineTo(17f)
                        horizontalLineTo(5f)
                        close()
                    }
                }
                .build()
        return _sets!!
    }

private var _sets: ImageVector? = null
