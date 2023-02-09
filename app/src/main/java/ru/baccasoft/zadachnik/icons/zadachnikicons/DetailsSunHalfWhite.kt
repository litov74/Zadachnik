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

public val ZadachnikIcons.DetailsSunHalfWhite: ImageVector
    get() {
        if (_detailsSunHalf != null) {
            return _detailsSunHalf!!
        }
        _detailsSunHalf = Builder(
            name = "DetailsSunHalf", defaultWidth = 24.0.dp, defaultHeight =
            24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFffffff)),
                strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(17.5f, 14.5f)
                lineTo(19.5f, 12.5f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFffffff)),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(4.0f, 20.0f)
                horizontalLineTo(20.0f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFffffff)),
                strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(4.5f, 12.5f)
                lineTo(6.5f, 14.5f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFffffff)),
                strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(15.0f, 7.0f)
                lineTo(12.0f, 4.0f)
                moveTo(12.0f, 4.0f)
                lineTo(9.0f, 7.0f)
                moveTo(12.0f, 4.0f)
                lineTo(12.0f, 12.5f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFffffff)),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(17.0f, 20.0f)
                curveTo(17.0f, 17.2386f, 14.7614f, 15.0f, 12.0f, 15.0f)
                curveTo(9.2386f, 15.0f, 7.0f, 17.2386f, 7.0f, 20.0f)
            }
        }
            .build()
        return _detailsSunHalf!!
    }

private var _detailsSunHalf: ImageVector? = null
