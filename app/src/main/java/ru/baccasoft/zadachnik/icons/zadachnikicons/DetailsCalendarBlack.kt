package ru.baccasoft.zadachnik.icons.zadachnikicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.baccasoft.zadachnik.icons.ZadachnikIcons

public val ZadachnikIcons.DetailsCalendarBlack: ImageVector
    get() {
        if (_detailsCalendar != null) {
            return _detailsCalendar!!
        }
        _detailsCalendar = Builder(
            name = "DetailsCalendar", defaultWidth = 24.0.dp, defaultHeight =
            24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF040404)),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(5.0f, 6.0f)
                lineTo(19.0f, 6.0f)
                arcTo(2.0f, 2.0f, 0.0f, false, true, 21.0f, 8.0f)
                lineTo(21.0f, 19.0f)
                arcTo(2.0f, 2.0f, 0.0f, false, true, 19.0f, 21.0f)
                lineTo(5.0f, 21.0f)
                arcTo(2.0f, 2.0f, 0.0f, false, true, 3.0f, 19.0f)
                lineTo(3.0f, 8.0f)
                arcTo(2.0f, 2.0f, 0.0f, false, true, 5.0f, 6.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF040404)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(21.0f, 10.0f)
                horizontalLineTo(3.0f)
                verticalLineTo(9.0f)
                horizontalLineTo(21.0f)
                verticalLineTo(10.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF040404)),
                strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(7.0f, 3.0f)
                lineTo(7.0f, 6.0f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF040404)),
                strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(17.0f, 3.0f)
                lineTo(17.0f, 6.0f)
            }
            path(
                fill = SolidColor(Color(0xFF040404)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(7.5f, 12.0f)
                lineTo(10.5f, 12.0f)
                arcTo(0.5f, 0.5f, 0.0f, false, true, 11.0f, 12.5f)
                lineTo(11.0f, 13.5f)
                arcTo(0.5f, 0.5f, 0.0f, false, true, 10.5f, 14.0f)
                lineTo(7.5f, 14.0f)
                arcTo(0.5f, 0.5f, 0.0f, false, true, 7.0f, 13.5f)
                lineTo(7.0f, 12.5f)
                arcTo(0.5f, 0.5f, 0.0f, false, true, 7.5f, 12.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF040404)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(7.5f, 16.0f)
                lineTo(10.5f, 16.0f)
                arcTo(0.5f, 0.5f, 0.0f, false, true, 11.0f, 16.5f)
                lineTo(11.0f, 17.5f)
                arcTo(0.5f, 0.5f, 0.0f, false, true, 10.5f, 18.0f)
                lineTo(7.5f, 18.0f)
                arcTo(0.5f, 0.5f, 0.0f, false, true, 7.0f, 17.5f)
                lineTo(7.0f, 16.5f)
                arcTo(0.5f, 0.5f, 0.0f, false, true, 7.5f, 16.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF040404)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(13.5f, 12.0f)
                lineTo(16.5f, 12.0f)
                arcTo(0.5f, 0.5f, 0.0f, false, true, 17.0f, 12.5f)
                lineTo(17.0f, 13.5f)
                arcTo(0.5f, 0.5f, 0.0f, false, true, 16.5f, 14.0f)
                lineTo(13.5f, 14.0f)
                arcTo(0.5f, 0.5f, 0.0f, false, true, 13.0f, 13.5f)
                lineTo(13.0f, 12.5f)
                arcTo(0.5f, 0.5f, 0.0f, false, true, 13.5f, 12.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF040404)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(13.5f, 16.0f)
                lineTo(16.5f, 16.0f)
                arcTo(0.5f, 0.5f, 0.0f, false, true, 17.0f, 16.5f)
                lineTo(17.0f, 17.5f)
                arcTo(0.5f, 0.5f, 0.0f, false, true, 16.5f, 18.0f)
                lineTo(13.5f, 18.0f)
                arcTo(0.5f, 0.5f, 0.0f, false, true, 13.0f, 17.5f)
                lineTo(13.0f, 16.5f)
                arcTo(0.5f, 0.5f, 0.0f, false, true, 13.5f, 16.0f)
                close()
            }
        }
            .build()
        return _detailsCalendar!!
    }

private var _detailsCalendar: ImageVector? = null
