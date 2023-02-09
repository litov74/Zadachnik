package ru.baccasoft.zadachnik.icons.zadachnikicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.baccasoft.zadachnik.icons.ZadachnikIcons

public val ZadachnikIcons.DetailsAudio: ImageVector
    get() {
        if (_detailsAudio != null) {
            return _detailsAudio!!
        }
        _detailsAudio = Builder(
            name = "DetailsAudio", defaultWidth = 37.0.dp, defaultHeight =
            47.0.dp, viewportWidth = 37.0f, viewportHeight = 47.0f
        ).apply {
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFD7D7D7)),
                strokeLineWidth = 2.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(1.0f, 4.2f)
                curveTo(1.0f, 3.0799f, 1.0f, 2.5198f, 1.218f, 2.092f)
                curveTo(1.4097f, 1.7157f, 1.7157f, 1.4097f, 2.092f, 1.218f)
                curveTo(2.5198f, 1.0f, 3.0799f, 1.0f, 4.2f, 1.0f)
                horizontalLineTo(21.2288f)
                curveTo(21.7123f, 1.0f, 21.9541f, 1.0f, 22.1819f, 1.0541f)
                curveTo(22.3839f, 1.102f, 22.5772f, 1.1811f, 22.7548f, 1.2886f)
                curveTo(22.9552f, 1.4097f, 23.1276f, 1.5792f, 23.4723f, 1.9182f)
                lineTo(35.0435f, 13.2949f)
                curveTo(35.3964f, 13.6418f, 35.5728f, 13.8153f, 35.6991f, 14.0186f)
                curveTo(35.811f, 14.1988f, 35.8935f, 14.3957f, 35.9435f, 14.6018f)
                curveTo(36.0f, 14.8344f, 36.0f, 15.0818f, 36.0f, 15.5767f)
                verticalLineTo(42.8f)
                curveTo(36.0f, 43.9201f, 36.0f, 44.4802f, 35.782f, 44.908f)
                curveTo(35.5903f, 45.2843f, 35.2843f, 45.5903f, 34.908f, 45.782f)
                curveTo(34.4802f, 46.0f, 33.9201f, 46.0f, 32.8f, 46.0f)
                horizontalLineTo(4.2f)
                curveTo(3.0799f, 46.0f, 2.5198f, 46.0f, 2.092f, 45.782f)
                curveTo(1.7157f, 45.5903f, 1.4097f, 45.2843f, 1.218f, 44.908f)
                curveTo(1.0f, 44.4802f, 1.0f, 43.9201f, 1.0f, 42.8f)
                verticalLineTo(4.2f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFD7D7D7)),
                strokeLineWidth = 2.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(22.0f, 1.0f)
                verticalLineTo(12.6f)
                curveTo(22.0f, 13.4401f, 22.0f, 13.8601f, 22.1635f, 14.181f)
                curveTo(22.3073f, 14.4632f, 22.5368f, 14.6927f, 22.819f, 14.8365f)
                curveTo(23.1399f, 15.0f, 23.5599f, 15.0f, 24.4f, 15.0f)
                horizontalLineTo(36.0f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFD7D7D7)),
                strokeLineWidth = 2.0f, strokeLineCap = Butt, strokeLineJoin = Round,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(18.0f, 20.0f)
                lineTo(18.0f, 20.0f)
                arcTo(3.0f, 3.0f, 0.0f, false, true, 21.0f, 23.0f)
                lineTo(21.0f, 28.0f)
                arcTo(3.0f, 3.0f, 0.0f, false, true, 18.0f, 31.0f)
                lineTo(18.0f, 31.0f)
                arcTo(3.0f, 3.0f, 0.0f, false, true, 15.0f, 28.0f)
                lineTo(15.0f, 23.0f)
                arcTo(3.0f, 3.0f, 0.0f, false, true, 18.0f, 20.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFD7D7D7)),
                strokeLineWidth = 2.0f, strokeLineCap = StrokeCap.Companion.Round,
                strokeLineJoin = Round, strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(11.0f, 28.0f)
                curveTo(11.0f, 29.8565f, 11.7375f, 31.637f, 13.0503f, 32.9497f)
                curveTo(14.363f, 34.2625f, 16.1435f, 35.0f, 18.0f, 35.0f)
                curveTo(19.8565f, 35.0f, 21.637f, 34.2625f, 22.9497f, 32.9497f)
                curveTo(24.2625f, 31.637f, 25.0f, 29.8565f, 25.0f, 28.0f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFD7D7D7)),
                strokeLineWidth = 2.0f, strokeLineCap = StrokeCap.Companion.Round,
                strokeLineJoin = Round, strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(18.0f, 38.0f)
                verticalLineTo(36.0f)
            }
        }
            .build()
        return _detailsAudio!!
    }

private var _detailsAudio: ImageVector? = null
