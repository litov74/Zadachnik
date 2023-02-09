package ru.baccasoft.zadachnik.icons.zadachnikicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.baccasoft.zadachnik.icons.ZadachnikIcons

public val ZadachnikIcons.DetailsClock: ImageVector
    get() {
        if (_detailsClock != null) {
            return _detailsClock!!
        }
        _detailsClock = Builder(
            name = "DetailsClock", defaultWidth = 24.0.dp, defaultHeight =
            24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF040404)),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Round,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(12.0f, 5.0f)
                verticalLineTo(12.0f)
                lineTo(16.0f, 16.0f)
                moveTo(9.0f, 21.0f)
                horizontalLineTo(15.0f)
                curveTo(18.3137f, 21.0f, 21.0f, 18.3137f, 21.0f, 15.0f)
                verticalLineTo(9.0f)
                curveTo(21.0f, 5.6863f, 18.3137f, 3.0f, 15.0f, 3.0f)
                horizontalLineTo(9.0f)
                curveTo(5.6863f, 3.0f, 3.0f, 5.6863f, 3.0f, 9.0f)
                verticalLineTo(15.0f)
                curveTo(3.0f, 18.3137f, 5.6863f, 21.0f, 9.0f, 21.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF040404)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(12.0f, 12.0f)
                moveToRelative(-1.0f, 0.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, true, true, 2.0f, 0.0f)
                arcToRelative(1.0f, 1.0f, 0.0f, true, true, -2.0f, 0.0f)
            }
        }
            .build()
        return _detailsClock!!
    }

private var _detailsClock: ImageVector? = null
