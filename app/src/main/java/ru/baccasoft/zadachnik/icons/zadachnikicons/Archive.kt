package ru.baccasoft.zadachnik.icons.zadachnikicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.baccasoft.zadachnik.icons.ZadachnikIcons

public val ZadachnikIcons.Archive: ImageVector
    get() {
        if (_archive != null) {
            return _archive!!
        }
        _archive = Builder(
            name = "Archive", defaultWidth = 32.0.dp, defaultHeight = 32.0.dp,
            viewportWidth = 32.0f, viewportHeight = 32.0f
        ).apply {
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFffffff)),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(6.0f, 10.0f)
                verticalLineTo(22.0f)
                curveTo(6.0f, 23.8856f, 6.0f, 24.8284f, 6.5858f, 25.4142f)
                curveTo(7.1716f, 26.0f, 8.1144f, 26.0f, 10.0f, 26.0f)
                horizontalLineTo(22.0f)
                curveTo(23.8857f, 26.0f, 24.8285f, 26.0f, 25.4143f, 25.4142f)
                curveTo(26.0f, 24.8284f, 26.0f, 23.8856f, 26.0f, 22.0f)
                verticalLineTo(10.0f)
                moveTo(6.0f, 10.0f)
                horizontalLineTo(26.0f)
                moveTo(6.0f, 10.0f)
                horizontalLineTo(3.8334f)
                curveTo(3.5977f, 10.0f, 3.4798f, 10.0f, 3.4066f, 9.9268f)
                curveTo(3.3334f, 9.8535f, 3.3334f, 9.7357f, 3.3334f, 9.5f)
                verticalLineTo(9.0f)
                curveTo(3.3334f, 7.5858f, 3.3334f, 6.8787f, 3.7727f, 6.4393f)
                curveTo(4.212f, 6.0f, 4.9192f, 6.0f, 6.3334f, 6.0f)
                horizontalLineTo(25.6667f)
                curveTo(27.0809f, 6.0f, 27.788f, 6.0f, 28.2274f, 6.4393f)
                curveTo(28.6667f, 6.8787f, 28.6667f, 7.5858f, 28.6667f, 9.0f)
                verticalLineTo(9.5f)
                curveTo(28.6667f, 9.7357f, 28.6667f, 9.8535f, 28.5935f, 9.9268f)
                curveTo(28.5203f, 10.0f, 28.4024f, 10.0f, 28.1667f, 10.0f)
                horizontalLineTo(26.0f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFffffff)),
                strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(12.6666f, 19.333f)
                horizontalLineTo(19.3333f)
            }
        }
            .build()
        return _archive!!
    }

private var _archive: ImageVector? = null
