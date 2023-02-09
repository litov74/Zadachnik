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

public val ZadachnikIcons.MenuAddTask: ImageVector
    get() {
        if (_menuAddTask != null) {
            return _menuAddTask!!
        }
        _menuAddTask = Builder(
            name = "MenuAddTask", defaultWidth = 32.0.dp, defaultHeight =
            32.0.dp, viewportWidth = 32.0f, viewportHeight = 32.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFDB3340)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(5.5f, 16.0f)
                curveTo(5.5f, 10.201f, 10.201f, 5.5f, 16.0f, 5.5f)
                curveTo(21.799f, 5.5f, 26.5f, 10.201f, 26.5f, 16.0f)
                curveTo(26.5f, 21.799f, 21.799f, 26.5f, 16.0f, 26.5f)
                curveTo(10.201f, 26.5f, 5.5f, 21.799f, 5.5f, 16.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, fillAlpha = 0.1f,
                strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(5.5f, 16.0f)
                curveTo(5.5f, 10.201f, 10.201f, 5.5f, 16.0f, 5.5f)
                curveTo(21.799f, 5.5f, 26.5f, 10.201f, 26.5f, 16.0f)
                curveTo(26.5f, 21.799f, 21.799f, 26.5f, 16.0f, 26.5f)
                curveTo(10.201f, 26.5f, 5.5f, 21.799f, 5.5f, 16.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFDB3340)),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(5.5f, 16.0f)
                curveTo(5.5f, 10.201f, 10.201f, 5.5f, 16.0f, 5.5f)
                curveTo(21.799f, 5.5f, 26.5f, 10.201f, 26.5f, 16.0f)
                curveTo(26.5f, 21.799f, 21.799f, 26.5f, 16.0f, 26.5f)
                curveTo(10.201f, 26.5f, 5.5f, 21.799f, 5.5f, 16.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF000000)),
                strokeAlpha = 0.1f, strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin
                = Miter, strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(5.5f, 16.0f)
                curveTo(5.5f, 10.201f, 10.201f, 5.5f, 16.0f, 5.5f)
                curveTo(21.799f, 5.5f, 26.5f, 10.201f, 26.5f, 16.0f)
                curveTo(26.5f, 21.799f, 21.799f, 26.5f, 16.0f, 26.5f)
                curveTo(10.201f, 26.5f, 5.5f, 21.799f, 5.5f, 16.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFffffff)),
                strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(12.0f, 16.0f)
                horizontalLineTo(16.0f)
                moveTo(16.0f, 16.0f)
                horizontalLineTo(20.0f)
                moveTo(16.0f, 16.0f)
                verticalLineTo(12.0f)
                moveTo(16.0f, 16.0f)
                verticalLineTo(20.0f)
            }
        }
            .build()
        return _menuAddTask!!
    }

private var _menuAddTask: ImageVector? = null
