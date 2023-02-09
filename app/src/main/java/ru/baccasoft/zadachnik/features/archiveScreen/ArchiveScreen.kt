package ru.baccasoft.zadachnik.features.archiveScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.baccasoft.zadachnik.R
import ru.baccasoft.zadachnik.ui.theme.boldFont


@ExperimentalCoroutinesApi
@Composable
fun ArchiveScreen(navController: NavController) {

}

@Composable
fun ArchiveScreenAppBar(
    onClick: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.wrapContentSize(),
        title = {
            IconButton(onClick = {
                onClick()
            }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_menu_24),
                    contentDescription = "menu",
                )
            }
            Box(Modifier.padding(end = 48.dp)) {
                Text(
                    text = "Архив",
                    fontFamily = boldFont,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .wrapContentSize()
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        },
        backgroundColor = colorResource(id = R.color.white),
    )
}