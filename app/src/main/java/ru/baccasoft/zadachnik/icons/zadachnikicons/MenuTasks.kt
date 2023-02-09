package ru.baccasoft.zadachnik.icons.zadachnikicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.baccasoft.zadachnik.icons.ZadachnikIcons

public val ZadachnikIcons.MenuTasks: ImageVector
    get() {
        if (_menuTasks != null) {
            return _menuTasks!!
        }
        _menuTasks = Builder(
            name = "MenuTasks", defaultWidth = 32.0.dp, defaultHeight = 32.0.dp,
            viewportWidth = 32.0f, viewportHeight = 32.0f
        ).apply {
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF040404)),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(26.6666f, 16.0f)
                curveTo(26.6666f, 10.109f, 21.891f, 5.3334f, 16.0f, 5.3334f)
                verticalLineTo(5.3334f)
                curveTo(10.1089f, 5.3334f, 5.3333f, 10.109f, 5.3333f, 16.0f)
                verticalLineTo(25.4118f)
                curveTo(5.3333f, 25.6487f, 5.3333f, 25.7671f, 5.3533f, 25.8656f)
                curveTo(5.4332f, 26.2592f, 5.7408f, 26.5669f, 6.1344f, 26.6467f)
                curveTo(6.2329f, 26.6667f, 6.3513f, 26.6667f, 6.5882f, 26.6667f)
                horizontalLineTo(16.0f)
                curveTo(21.891f, 26.6667f, 26.6666f, 21.8911f, 26.6666f, 16.0f)
                verticalLineTo(16.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF040404)),
                strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(12.0f, 13.3334f)
                lineTo(20.0f, 13.3334f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF040404)),
                strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(12.0f, 18.6666f)
                horizontalLineTo(16.0f)
            }
        }
            .build()
        return _menuTasks!!
    }

private var _menuTasks: ImageVector? = null
