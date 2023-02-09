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

public val ZadachnikIcons.MenuArchive: ImageVector
    get() {
        if (_menuArchive != null) {
            return _menuArchive!!
        }
        _menuArchive = Builder(
            name = "MenuArchive", defaultWidth = 32.0.dp, defaultHeight =
            32.0.dp, viewportWidth = 32.0f, viewportHeight = 32.0f
        ).apply {
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF040404)),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(6.0f, 10.0f)
                verticalLineTo(22.0f)
                curveTo(6.0f, 23.8856f, 6.0f, 24.8284f, 6.5858f, 25.4142f)
                curveTo(7.1715f, 26.0f, 8.1144f, 26.0f, 10.0f, 26.0f)
                horizontalLineTo(22.0f)
                curveTo(23.8856f, 26.0f, 24.8284f, 26.0f, 25.4142f, 25.4142f)
                curveTo(26.0f, 24.8284f, 26.0f, 23.8856f, 26.0f, 22.0f)
                verticalLineTo(10.0f)
                moveTo(6.0f, 10.0f)
                horizontalLineTo(26.0f)
                moveTo(6.0f, 10.0f)
                horizontalLineTo(3.8333f)
                curveTo(3.5976f, 10.0f, 3.4798f, 10.0f, 3.4065f, 9.9268f)
                curveTo(3.3333f, 9.8535f, 3.3333f, 9.7357f, 3.3333f, 9.5f)
                verticalLineTo(9.0f)
                curveTo(3.3333f, 7.5858f, 3.3333f, 6.8787f, 3.7727f, 6.4393f)
                curveTo(4.212f, 6.0f, 4.9191f, 6.0f, 6.3333f, 6.0f)
                horizontalLineTo(25.6666f)
                curveTo(27.0809f, 6.0f, 27.788f, 6.0f, 28.2273f, 6.4393f)
                curveTo(28.6666f, 6.8787f, 28.6666f, 7.5858f, 28.6666f, 9.0f)
                verticalLineTo(9.5f)
                curveTo(28.6666f, 9.7357f, 28.6666f, 9.8535f, 28.5934f, 9.9268f)
                curveTo(28.5202f, 10.0f, 28.4023f, 10.0f, 28.1666f, 10.0f)
                horizontalLineTo(26.0f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF040404)),
                strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(12.6667f, 19.3334f)
                horizontalLineTo(19.3334f)
            }
        }
            .build()
        return _menuArchive!!
    }

private var _menuArchive: ImageVector? = null
