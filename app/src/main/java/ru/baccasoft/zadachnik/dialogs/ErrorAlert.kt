package ru.baccasoft.zadachnik.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.elevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.baccasoft.zadachnik.R
import ru.baccasoft.zadachnik.ui.theme.boldFont
import ru.baccasoft.zadachnik.ui.theme.commonFont

@Composable
fun ErrorAlert(
    text: String,
    onClose: () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    BaseDialog {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Bottom),
            modifier = Modifier
                .padding(top = 5.dp, start = 5.dp, end = 5.dp)
        ) {
            Text(
                fontFamily = boldFont,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Ошибка",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                fontFamily = commonFont,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                text = text
            )
            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = colorResource(id = R.color.message_grey))
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                Button(
                    modifier = Modifier.width(100.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    elevation = elevation(0.dp, 0.dp),
                    onClick = onClose) {
                    Text(text = "ОК", color = colorResource(id = R.color.colorPrimary), fontFamily = commonFont)
                }
            }
        }
    }
}

//@Preview
@Composable
fun showErrorAlert() {
    ErrorAlert(text = "Произошла ошибка подключения к интернету. Проверьте подключение к сети") {

    }
}

@Preview
@Composable
fun showErrorNoInetAlert() {
    Column(modifier = Modifier.fillMaxSize()) {
        //ErrorAlert(text = "Error 404") {}
        showErrorAlert()
    }

}