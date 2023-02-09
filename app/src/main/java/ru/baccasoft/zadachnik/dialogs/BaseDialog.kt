package ru.baccasoft.zadachnik.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BaseDialog(
    onDismissRequest: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable {
                onDismissRequest.invoke()
            }

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .heightIn(max = 200.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(Color.White)
                .align(Alignment.CenterHorizontally)
        ) {
            content()
        }
    }
}