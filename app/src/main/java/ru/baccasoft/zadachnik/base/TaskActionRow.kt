package ru.baccasoft.zadachnik.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.baccasoft.zadachnik.R
import ru.baccasoft.zadachnik.icons.ZadachnikIcons
import ru.baccasoft.zadachnik.icons.zadachnikicons.Archive
import ru.baccasoft.zadachnik.icons.zadachnikicons.Unarchive

@Composable
fun ActionRow(
    toArchive: () -> Unit,
    unarchive: Boolean = false
) {

    val textColor = colorResource(id = R.color.grey).toArgb()
    val interactionSource = MutableInteractionSource()

    Row(
        Modifier
            .padding(horizontal = 1.dp)
            .background(color = Color(textColor))
            .fillMaxWidth()) {
        IconButton(
            modifier = Modifier
                .background(color = Color(textColor)),
            onClick = {},
            content = {
                if (!unarchive) {
                    Image(
                        imageVector = ZadachnikIcons.Archive,
                        modifier = Modifier
                            .padding(start = 10.dp).clickable(interactionSource = interactionSource, indication = null) { toArchive() },
                        contentDescription = "archive action",
                    )
                } else {
                    Image(
                        imageVector = ZadachnikIcons.Unarchive,
                        modifier = Modifier
                            .padding(start = 10.dp).clickable(interactionSource = interactionSource, indication = null) { toArchive() },
                        contentDescription = "unarchive action",
                    )
                }
            },
        )
    }
}