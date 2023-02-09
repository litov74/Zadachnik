package ru.baccasoft.zadachnik.icons.zadachnikicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.baccasoft.zadachnik.icons.ZadachnikIcons

public val ZadachnikIcons.DetailsUser: ImageVector
    get() {
        if (_detailsUser != null) {
            return _detailsUser!!
        }
        _detailsUser = Builder(
            name = "DetailsUser", defaultWidth = 24.0.dp, defaultHeight =
            24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF040404)),
                strokeLineWidth = 1.2f, strokeLineCap = Round, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(19.7274f, 20.4471f)
                curveTo(19.2716f, 19.1713f, 18.2672f, 18.0439f, 16.8701f, 17.2399f)
                curveTo(15.4729f, 16.4358f, 13.7611f, 16.0f, 12.0f, 16.0f)
                curveTo(10.2389f, 16.0f, 8.5271f, 16.4358f, 7.1299f, 17.2399f)
                curveTo(5.7328f, 18.0439f, 4.7284f, 19.1713f, 4.2726f, 20.4471f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF040404)),
                strokeLineWidth = 1.2f, strokeLineCap = Round, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(12.0f, 8.0f)
                moveToRelative(-4.0f, 0.0f)
                arcToRelative(4.0f, 4.0f, 0.0f, true, true, 8.0f, 0.0f)
                arcToRelative(4.0f, 4.0f, 0.0f, true, true, -8.0f, 0.0f)
            }
        }
            .build()
        return _detailsUser!!
    }

private var _detailsUser: ImageVector? = null
