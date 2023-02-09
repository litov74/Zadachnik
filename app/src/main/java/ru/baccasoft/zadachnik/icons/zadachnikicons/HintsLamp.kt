package ru.baccasoft.zadachnik.icons.zadachnikicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.baccasoft.zadachnik.icons.ZadachnikIcons

public val ZadachnikIcons.HintsLamp: ImageVector
    get() {
        if (_hintsLamp != null) {
            return _hintsLamp!!
        }
        _hintsLamp = Builder(
            name = "HintsLamp", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
            viewportWidth = 24.0f, viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF040404)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(9.1438f, 15.6858f)
                lineTo(8.6817f, 15.8768f)
                lineTo(9.1438f, 15.6858f)
                close()
                moveTo(8.9184f, 15.2337f)
                lineTo(8.5233f, 15.5401f)
                lineTo(8.9184f, 15.2337f)
                close()
                moveTo(15.0816f, 15.2337f)
                lineTo(15.4767f, 15.5401f)
                lineTo(15.0816f, 15.2337f)
                close()
                moveTo(17.5f, 10.0f)
                curveTo(17.5f, 11.8007f, 16.6352f, 13.3992f, 15.2962f, 14.4033f)
                lineTo(15.8961f, 15.2034f)
                curveTo(17.4761f, 14.0185f, 18.5f, 12.1288f, 18.5f, 10.0f)
                horizontalLineTo(17.5f)
                close()
                moveTo(12.0f, 4.5f)
                curveTo(15.0376f, 4.5f, 17.5f, 6.9624f, 17.5f, 10.0f)
                horizontalLineTo(18.5f)
                curveTo(18.5f, 6.4102f, 15.5899f, 3.5f, 12.0f, 3.5f)
                verticalLineTo(4.5f)
                close()
                moveTo(6.5f, 10.0f)
                curveTo(6.5f, 6.9624f, 8.9624f, 4.5f, 12.0f, 4.5f)
                verticalLineTo(3.5f)
                curveTo(8.4101f, 3.5f, 5.5f, 6.4102f, 5.5f, 10.0f)
                horizontalLineTo(6.5f)
                close()
                moveTo(8.7038f, 14.4033f)
                curveTo(7.3648f, 13.3992f, 6.5f, 11.8007f, 6.5f, 10.0f)
                horizontalLineTo(5.5f)
                curveTo(5.5f, 12.1288f, 6.5239f, 14.0185f, 8.1039f, 15.2034f)
                lineTo(8.7038f, 14.4033f)
                close()
                moveTo(10.4974f, 19.7535f)
                curveTo(10.4668f, 18.2874f, 10.1631f, 16.8435f, 9.6059f, 15.4949f)
                lineTo(8.6817f, 15.8768f)
                curveTo(9.1917f, 17.1111f, 9.4696f, 18.4325f, 9.4976f, 19.7744f)
                lineTo(10.4974f, 19.7535f)
                close()
                moveTo(13.5652f, 19.6584f)
                curveTo(12.5799f, 20.151f, 11.4201f, 20.151f, 10.4348f, 19.6584f)
                lineTo(9.9875f, 20.5528f)
                curveTo(11.2544f, 21.1862f, 12.7456f, 21.1862f, 14.0125f, 20.5528f)
                lineTo(13.5652f, 19.6584f)
                close()
                moveTo(14.3941f, 15.4949f)
                curveTo(13.8369f, 16.8435f, 13.5332f, 18.2874f, 13.5026f, 19.7535f)
                lineTo(14.5024f, 19.7744f)
                curveTo(14.5304f, 18.4325f, 14.8083f, 17.1111f, 15.3183f, 15.8768f)
                lineTo(14.3941f, 15.4949f)
                close()
                moveTo(14.0125f, 20.5528f)
                curveTo(14.3131f, 20.4024f, 14.4956f, 20.0987f, 14.5024f, 19.7744f)
                lineTo(13.5026f, 19.7535f)
                curveTo(13.5033f, 19.7185f, 13.5234f, 19.6793f, 13.5652f, 19.6584f)
                lineTo(14.0125f, 20.5528f)
                close()
                moveTo(9.4976f, 19.7744f)
                curveTo(9.5044f, 20.0987f, 9.6869f, 20.4024f, 9.9875f, 20.5528f)
                lineTo(10.4348f, 19.6584f)
                curveTo(10.4766f, 19.6793f, 10.4967f, 19.7185f, 10.4974f, 19.7535f)
                lineTo(9.4976f, 19.7744f)
                close()
                moveTo(8.1039f, 15.2034f)
                curveTo(8.4225f, 15.4422f, 8.493f, 15.5011f, 8.5233f, 15.5401f)
                lineTo(9.3135f, 14.9273f)
                curveTo(9.1787f, 14.7534f, 8.9613f, 14.5964f, 8.7038f, 14.4033f)
                lineTo(8.1039f, 15.2034f)
                close()
                moveTo(9.6059f, 15.4949f)
                curveTo(9.5191f, 15.2849f, 9.4461f, 15.0983f, 9.3135f, 14.9273f)
                lineTo(8.5233f, 15.5401f)
                curveTo(8.5557f, 15.582f, 8.578f, 15.6259f, 8.6817f, 15.8768f)
                lineTo(9.6059f, 15.4949f)
                close()
                moveTo(15.2962f, 14.4033f)
                curveTo(15.0387f, 14.5964f, 14.8213f, 14.7534f, 14.6865f, 14.9273f)
                lineTo(15.4767f, 15.5401f)
                curveTo(15.5069f, 15.5011f, 15.5775f, 15.4422f, 15.8961f, 15.2034f)
                lineTo(15.2962f, 14.4033f)
                close()
                moveTo(15.3183f, 15.8768f)
                curveTo(15.422f, 15.6259f, 15.4443f, 15.582f, 15.4767f, 15.5401f)
                lineTo(14.6865f, 14.9273f)
                curveTo(14.5539f, 15.0983f, 14.4809f, 15.2849f, 14.3941f, 15.4949f)
                lineTo(15.3183f, 15.8768f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF040404)),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(14.5f, 16.5f)
                verticalLineTo(16.5f)
                curveTo(12.8951f, 17.1419f, 11.1049f, 17.1419f, 9.5f, 16.5f)
                verticalLineTo(16.5f)
            }
        }
            .build()
        return _hintsLamp!!
    }

private var _hintsLamp: ImageVector? = null
