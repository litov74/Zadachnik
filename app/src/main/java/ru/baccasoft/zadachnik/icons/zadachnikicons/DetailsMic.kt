package ru.baccasoft.zadachnik.icons.zadachnikicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.baccasoft.zadachnik.icons.ZadachnikIcons

public val ZadachnikIcons.DetailsMic: ImageVector
    get() {
        if (_detailsMic != null) {
            return _detailsMic!!
        }
        _detailsMic = Builder(
            name = "DetailsMic", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
            viewportWidth = 24.0f, viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF040404)),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Round,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(12.0f, 3.0f)
                lineTo(12.0f, 3.0f)
                arcTo(3.0f, 3.0f, 0.0f, false, true, 15.0f, 6.0f)
                lineTo(15.0f, 11.0f)
                arcTo(3.0f, 3.0f, 0.0f, false, true, 12.0f, 14.0f)
                lineTo(12.0f, 14.0f)
                arcTo(3.0f, 3.0f, 0.0f, false, true, 9.0f, 11.0f)
                lineTo(9.0f, 6.0f)
                arcTo(3.0f, 3.0f, 0.0f, false, true, 12.0f, 3.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF040404)),
                strokeLineWidth = 1.0f, strokeLineCap = StrokeCap.Companion.Round,
                strokeLineJoin = Round, strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(5.5f, 11.0f)
                curveTo(5.5f, 12.7239f, 6.1848f, 14.3772f, 7.4038f, 15.5962f)
                curveTo(8.6228f, 16.8152f, 10.2761f, 17.5f, 12.0f, 17.5f)
                curveTo(13.7239f, 17.5f, 15.3772f, 16.8152f, 16.5962f, 15.5962f)
                curveTo(17.8152f, 14.3772f, 18.5f, 12.7239f, 18.5f, 11.0f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF040404)),
                strokeLineWidth = 1.0f, strokeLineCap = StrokeCap.Companion.Round,
                strokeLineJoin = Round, strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(12.0f, 21.0f)
                verticalLineTo(19.0f)
            }
        }
            .build()
        return _detailsMic!!
    }

private var _detailsMic: ImageVector? = null
