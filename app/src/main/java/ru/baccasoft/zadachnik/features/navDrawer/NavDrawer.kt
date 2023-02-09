package ru.baccasoft.zadachnik.features.navDrawer

import android.graphics.Paint.Align
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.baccasoft.zadachnik.R
import ru.baccasoft.zadachnik.base.CustomSwitch
import ru.baccasoft.zadachnik.base.NavDrawerItem
import ru.baccasoft.zadachnik.features.mainScreen.MainScreenViewModel
import ru.baccasoft.zadachnik.icons.ZadachnikIcons
import ru.baccasoft.zadachnik.icons.additions.MyIconPack
import ru.baccasoft.zadachnik.icons.additions.myiconpack._24
import ru.baccasoft.zadachnik.icons.additions.myiconpack._32
import ru.baccasoft.zadachnik.icons.zadachnikicons.MenuAddTask
import ru.baccasoft.zadachnik.icons.zadachnikicons.MenuArchive
import ru.baccasoft.zadachnik.icons.zadachnikicons.MenuTasks
import ru.baccasoft.zadachnik.ui.theme.commonFont
import timber.log.Timber

@Composable
fun NavDrawer(
    navController: NavController,
    currentScreen: MutableState<String>,
    onCreateTaskClick: () -> Unit,
    onHintsClicked: (Boolean) -> Unit,
    switchInitialValue: MutableState<Boolean>,
    onLogOutClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
    onClick: (String) -> Unit,
    onCloseClick: () -> Unit,
) {

    val context = LocalContext.current

    val image: Painter = painterResource(id = R.drawable.logo2)

    val greyColor = colorResource(id = R.color.darker_gray)
    val lighterGrey = colorResource(id = R.color.another_grey)
    val icon = ZadachnikIcons.MenuAddTask.tintColor

    val interactionSource = remember { MutableInteractionSource() }

    Row(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.width(22.dp))
        Spacer(modifier = Modifier.height(30.dp))
        Column(modifier = Modifier.fillMaxHeight(0.95f), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.SpaceBetween) {
            Column(horizontalAlignment = Alignment.Start) {
                Box(modifier = Modifier.size(56.dp)) {
                    Image(painter = image, contentDescription = "logo", modifier = Modifier.clickable(interactionSource = interactionSource, indication = null){onCloseClick()})
                }
                Spacer(modifier = Modifier.height(30.dp))
                NavDrawerItem(text = "Задачи", modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    navController.navigate("main_screen") {
                        try {
                            popUpTo("login_screen") {
                                inclusive = true
                            }
                            popUpTo("archive_screen") {
                                inclusive = true
                            }
                        } catch (t: Throwable) {
                            Timber.e(t)
                        }
                    }
                    onClick("main_screen")
                },
                    icon = {
                        Image(
                            imageVector = if (currentScreen.value == "main_screen") MyIconPack._32 else ZadachnikIcons.MenuTasks,
                            contentDescription = "main",
                            modifier = Modifier
                                .height(32.dp)
                                .width(32.dp),
                        )
                    }
                )
                Spacer(modifier = Modifier.height(40.dp))
                NavDrawerItem(text = "Архив",
                    modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        navController.navigate("archive_screen") {
                            try {
                                popUpTo("main_screen") {
                                    inclusive = true
                                }
                                popUpTo("login_screen") {
                                    inclusive = true
                                }
                            } catch (e: Error) {
                                Timber.e(e)
                            }
                        }
                        onClick("archive_screen")
                    },
                    icon = {
                        Image(
                            imageVector = if(currentScreen.value == "archive_screen") MyIconPack._24 else ZadachnikIcons.MenuArchive,
                            contentDescription = "archive",
                            modifier = Modifier
                                .height(32.dp)
                                .width(32.dp),
                        )
                    }
                )
                Spacer(modifier = Modifier.height(40.dp))
                NavDrawerItem(
                    text = "Создать задачу",
                    modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onCreateTaskClick()
                    },
                    isRed = true,
                    icon = {
                        Image(
                            imageVector = ZadachnikIcons.MenuAddTask,
                            contentDescription = "add",
                            modifier = Modifier
                                .height(32.dp)
                                .width(32.dp),
                        )
                    }
                )
            }
            Column(horizontalAlignment = Alignment.Start) {
                Row(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Советы", fontFamily = commonFont)
                    Spacer(modifier = Modifier.width(100.dp))
                    CustomSwitch(
                        ctx = context,
                        initialValue = switchInitialValue,
                        onClick = { b ->
                            onHintsClicked(b)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Divider()
                Spacer(modifier = Modifier.height(15.dp))
                Row(modifier = Modifier.fillMaxWidth().clickable(interactionSource = interactionSource, indication = null){onLogOutClicked()}){
                    Text(text = "Выйти", fontFamily = commonFont)
                }
                Spacer(modifier = Modifier.height(15.dp))
                Divider()
                Spacer(modifier = Modifier.height(15.dp))
                Column(modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onDeleteAccountClicked()
                }) {
                    Text(text = "Удалить аккаунт", fontFamily = commonFont, color = greyColor)
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = "Вы можете полностью удалить аккаунт, данные и информация о подключенных сервисах будут полностью удалены.",
                        fontFamily = commonFont,
                        color = lighterGrey,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .padding(end = 16.dp)
                    )
                }
            }


        }
    }


}