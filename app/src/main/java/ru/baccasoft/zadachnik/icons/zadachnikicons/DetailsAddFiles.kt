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

public val ZadachnikIcons.DetailsAddFiles: ImageVector
    get() {
        if (_detailsAddFiles != null) {
            return _detailsAddFiles!!
        }
        _detailsAddFiles = Builder(
            name = "DetailsAddFiles", defaultWidth = 24.0.dp, defaultHeight =
            24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFDB3340)),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(20.0f, 8.0f)
                verticalLineTo(8.0f)
                curveTo(20.0f, 7.8314f, 20.0f, 7.747f, 19.9934f, 7.6659f)
                curveTo(19.9594f, 7.2488f, 19.7954f, 6.8528f, 19.5244f, 6.5338f)
                curveTo(19.4718f, 6.4717f, 19.4121f, 6.4121f, 19.2929f, 6.2929f)
                lineTo(15.7071f, 2.7071f)
                curveTo(15.5879f, 2.5879f, 15.5282f, 2.5282f, 15.4662f, 2.4756f)
                curveTo(15.1472f, 2.2046f, 14.7512f, 2.0406f, 14.3341f, 2.0066f)
                curveTo(14.2529f, 2.0f, 14.1686f, 2.0f, 14.0f, 2.0f)
                verticalLineTo(2.0f)
                moveTo(20.0f, 8.0f)
                verticalLineTo(18.8f)
                curveTo(20.0f, 19.9201f, 20.0f, 20.4802f, 19.782f, 20.908f)
                curveTo(19.5903f, 21.2843f, 19.2843f, 21.5903f, 18.908f, 21.782f)
                curveTo(18.4802f, 22.0f, 17.9201f, 22.0f, 16.8f, 22.0f)
                horizontalLineTo(7.2f)
                curveTo(6.0799f, 22.0f, 5.5198f, 22.0f, 5.092f, 21.782f)
                curveTo(4.7157f, 21.5903f, 4.4097f, 21.2843f, 4.218f, 20.908f)
                curveTo(4.0f, 20.4802f, 4.0f, 19.9201f, 4.0f, 18.8f)
                verticalLineTo(5.2f)
                curveTo(4.0f, 4.0799f, 4.0f, 3.5198f, 4.218f, 3.092f)
                curveTo(4.4097f, 2.7157f, 4.7157f, 2.4097f, 5.092f, 2.218f)
                curveTo(5.5198f, 2.0f, 6.0799f, 2.0f, 7.2f, 2.0f)
                horizontalLineTo(14.0f)
                moveTo(20.0f, 8.0f)
                horizontalLineTo(17.2f)
                curveTo(16.0799f, 8.0f, 15.5198f, 8.0f, 15.092f, 7.782f)
                curveTo(14.7157f, 7.5903f, 14.4097f, 7.2843f, 14.218f, 6.908f)
                curveTo(14.0f, 6.4802f, 14.0f, 5.9201f, 14.0f, 4.8f)
                verticalLineTo(2.0f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFDB3340)),
                strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(12.0f, 10.0f)
                verticalLineTo(18.0f)
                moveTo(8.0f, 14.0f)
                horizontalLineTo(16.0f)
            }
        }
            .build()
        return _detailsAddFiles!!
    }

private var _detailsAddFiles: ImageVector? = null
