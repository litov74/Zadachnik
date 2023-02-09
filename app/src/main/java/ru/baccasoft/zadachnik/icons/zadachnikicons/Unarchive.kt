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

public val ZadachnikIcons.Unarchive: ImageVector
    get() {
        if (_unarchive != null) {
            return _unarchive!!
        }
        _unarchive = Builder(
            name = "Unarchive", defaultWidth = 32.0.dp, defaultHeight = 32.0.dp,
            viewportWidth = 32.0f, viewportHeight = 32.0f
        ).apply {
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFffffff)),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(17.3333f, 9.333f)
                horizontalLineTo(18.3431f)
                curveTo(19.1606f, 9.333f, 19.5694f, 9.333f, 19.9369f, 9.4853f)
                curveTo(20.3045f, 9.6375f, 20.5935f, 9.9265f, 21.1716f, 10.5046f)
                lineTo(22.8284f, 12.1614f)
                curveTo(23.4065f, 12.7395f, 23.6955f, 13.0285f, 23.8478f, 13.3961f)
                curveTo(24.0f, 13.7636f, 24.0f, 14.1724f, 24.0f, 14.9899f)
                verticalLineTo(22.6663f)
                curveTo(24.0f, 24.552f, 24.0f, 25.4948f, 23.4142f, 26.0806f)
                curveTo(22.8284f, 26.6663f, 21.8856f, 26.6663f, 20.0f, 26.6663f)
                horizontalLineTo(12.0f)
                curveTo(10.1144f, 26.6663f, 9.1716f, 26.6663f, 8.5858f, 26.0806f)
                curveTo(8.0f, 25.4948f, 8.0f, 24.552f, 8.0f, 22.6663f)
                verticalLineTo(14.9899f)
                curveTo(8.0f, 14.1724f, 8.0f, 13.7636f, 8.1522f, 13.3961f)
                curveTo(8.3045f, 13.0285f, 8.5935f, 12.7395f, 9.1716f, 12.1614f)
                lineTo(10.8284f, 10.5046f)
                curveTo(11.4065f, 9.9265f, 11.6955f, 9.6375f, 12.0631f, 9.4853f)
                curveTo(12.4306f, 9.333f, 12.8394f, 9.333f, 13.6569f, 9.333f)
                horizontalLineTo(14.6667f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF040404)),
                strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(12.0f, 9.3337f)
                lineTo(8.1387f, 6.7594f)
                curveTo(8.0547f, 6.7035f, 7.9453f, 6.7035f, 7.8613f, 6.7594f)
                lineTo(4.2537f, 9.1645f)
                curveTo(4.1225f, 9.252f, 4.1041f, 9.4377f, 4.2156f, 9.5493f)
                lineTo(7.7239f, 13.0575f)
                curveTo(7.9007f, 13.2343f, 8.0f, 13.4741f, 8.0f, 13.7242f)
                verticalLineTo(13.7242f)
                curveTo(8.0f, 14.2449f, 8.4221f, 14.667f, 8.9428f, 14.667f)
                horizontalLineTo(23.0572f)
                curveTo(23.5779f, 14.667f, 24.0f, 14.2449f, 24.0f, 13.7242f)
                verticalLineTo(13.7242f)
                curveTo(24.0f, 13.4741f, 24.0993f, 13.2343f, 24.2761f, 13.0575f)
                lineTo(27.7844f, 9.5493f)
                curveTo(27.8959f, 9.4377f, 27.8775f, 9.252f, 27.7463f, 9.1645f)
                lineTo(24.1387f, 6.7594f)
                curveTo(24.0547f, 6.7035f, 23.9453f, 6.7035f, 23.8613f, 6.7594f)
                lineTo(20.0f, 9.3337f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF040404)),
                strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(13.3334f, 5.3337f)
                lineTo(16.0f, 2.667f)
                moveTo(16.0f, 2.667f)
                lineTo(18.6667f, 5.3337f)
                moveTo(16.0f, 2.667f)
                lineTo(16.0f, 12.667f)
            }
        }
            .build()
        return _unarchive!!
    }

private var _unarchive: ImageVector? = null
