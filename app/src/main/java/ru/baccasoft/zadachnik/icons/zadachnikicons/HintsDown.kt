package ru.baccasoft.zadachnik.icons.zadachnikicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.baccasoft.zadachnik.icons.ZadachnikIcons

public val ZadachnikIcons.HintsDown: ImageVector
    get() {
        if (_hintsDown != null) {
            return _hintsDown!!
        }
        _hintsDown = Builder(
            name = "HintsDown", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
            viewportWidth = 24.0f, viewportHeight = 24.0f
        ).apply {
            group {
                path(
                    fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFB5B5B5)),
                    strokeLineWidth = 1.5f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType =
                    NonZero
                ) {
                    moveTo(6.0f, 9.0f)
                    lineTo(12.0f, 15.0f)
                    lineTo(18.0f, 9.0f)
                }
            }
        }
            .build()
        return _hintsDown!!
    }

private var _hintsDown: ImageVector? = null
