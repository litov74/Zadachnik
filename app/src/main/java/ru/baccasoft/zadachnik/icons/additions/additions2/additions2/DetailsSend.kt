package ru.baccasoft.zadachnik.icons.additions.additions2.additions2

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.baccasoft.zadachnik.icons.additions.additions2.Additions2

public val Additions2.DetailsSend: ImageVector
    get() {
        if (_detailsSend != null) {
            return _detailsSend!!
        }
        _detailsSend = Builder(name = "DetailsSend", defaultWidth = 24.0.dp, defaultHeight =
                24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFDB3340)),
                    strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Round,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(20.0f, 12.0f)
                lineTo(4.0f, 4.0f)
                lineTo(6.0f, 11.0f)
                horizontalLineTo(9.0f)
                curveTo(9.5523f, 11.0f, 10.0f, 11.4477f, 10.0f, 12.0f)
                curveTo(10.0f, 12.5523f, 9.5523f, 13.0f, 9.0f, 13.0f)
                horizontalLineTo(6.0f)
                lineTo(4.0f, 20.0f)
                lineTo(20.0f, 12.0f)
                close()
            }
        }
        .build()
        return _detailsSend!!
    }

private var _detailsSend: ImageVector? = null
