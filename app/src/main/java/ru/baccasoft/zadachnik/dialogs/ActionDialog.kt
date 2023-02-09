package ru.baccasoft.zadachnik.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.ButtonDefaults.elevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import ru.baccasoft.zadachnik.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.baccasoft.zadachnik.ui.theme.commonFont

@Composable
fun ActionDialog(
    text: String,
    onActionText: String,
    onClose: () -> Unit,
    onAction: () -> Unit,
) {
    BaseDialog {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Bottom),
            modifier = Modifier
                .padding(top = 5.dp, start = 5.dp, end = 5.dp)
        ) {
            Text(
                fontFamily = commonFont,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = text
            )
            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = colorResource(id = R.color.message_grey))
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                Button(
                    modifier = Modifier.width(100.dp),
                    colors = buttonColors(backgroundColor = colorResource(id = R.color.white)),
                    elevation = elevation(0.dp, 0.dp),
                    onClick = onAction) {
                    Text(text = onActionText, color = colorResource(id = R.color.colorPrimary), fontFamily = commonFont)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(50.dp)
                        .background(color = colorResource(id = R.color.message_grey))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier.width(100.dp),
                    colors = buttonColors(backgroundColor = Color.White),
                    elevation = elevation(0.dp, 0.dp),
                    onClick = onClose) {
                    Text(text = "Отмена", color = colorResource(id = R.color.colorPrimary), fontFamily = commonFont)
                }
            }
        }
    }
}

@Preview
@Composable
fun showAlertDialog() {
    ActionDialog(
        text = "Вы действительно хотите выйти?",
        onActionText = "Выйти", onClose = {}
        , onAction = {}
    )
}