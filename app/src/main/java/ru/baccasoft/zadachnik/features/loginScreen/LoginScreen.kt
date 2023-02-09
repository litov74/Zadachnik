package ru.baccasoft.zadachnik.features.loginScreen

import android.app.Application
import android.os.Handler
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.elevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.i18n.phonenumbers.PhoneNumberUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.baccasoft.zadachnik.R
import ru.baccasoft.zadachnik.base.CodesCell
import ru.baccasoft.zadachnik.base.PhoneNumberVisualTransformation
import ru.baccasoft.zadachnik.base.PinInput
import ru.baccasoft.zadachnik.icons.ZadachnikIcons
import ru.baccasoft.zadachnik.icons.zadachnikicons.Cross
import ru.baccasoft.zadachnik.icons.zadachnikicons.SearchLight
import ru.baccasoft.zadachnik.ui.theme.boldFont
import ru.baccasoft.zadachnik.ui.theme.commonFont
import ru.baccasoft.zadachnik.ui.theme.thinFont
import timber.log.Timber
import java.util.*
import kotlin.time.Duration.Companion.seconds


@Composable
@OptIn(
    ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
fun LoginScreen(navController: NavController) {

    //region Vars, Init values, Listeners

    val context = LocalContext.current
    val viewModel = hiltViewModel<LoginScreenViewModel>()
    viewModel.setPhoneNumber("")
    viewModel.generateCountriesList(context)
    val ticksDefaultValue = 120

    var regionTextFieldState by remember {
        mutableStateOf("+7")
    }
    var smsCodeTextFieldState by remember {
        mutableStateOf("")
    }
    var numberTextFieldState by remember {
        mutableStateOf("")
    }
    var buttonEnabled by remember {
        mutableStateOf(false)
    }
    var regionText by remember {
        mutableStateOf("Russian Federation")
    }
    var smsCodeText by remember {
        mutableStateOf("")
    }
    var ticks by remember {
        mutableStateOf(ticksDefaultValue) // количество секунд
    }
    var colorState by remember {
        mutableStateOf(0xFF888888)
    }
    var whatWasPressed by remember {
        mutableStateOf(0)
    }
    var searchTextFieldState by remember {
        mutableStateOf("")
    }
    var isTimerRunning by remember {
        mutableStateOf(false)
    }
    var isErrorInCode by remember {
        mutableStateOf(false)
    }
        // игнорируем ошибки
    val loadingState by viewModel.loadingState.collectAsStateWithLifecycle()

    val colorRed = colorResource(id = R.color.colorPrimary).toArgb().toLong() // R.colors.red
    val colorGrey =
        colorResource(id = R.color.darker_gray).toArgb().toLong() // R.colors.darker_grey
    colorState = colorGrey
    val lighterColorGrey = colorResource(id = R.color.grey).toArgb().toLong()

    val sendNewCodeIn =
        stringResource(id = R.string.phone_screen_send_new_code_in) + " (${processTimerText(ticks.div(60), ticks.mod(60))})"
    val sendNewCodeString = stringResource(id = R.string.phone_screen_send_new_code)
    val focusRequester = remember { FocusRequester() }
    smsCodeText = sendNewCodeIn

    val interactionSource = remember { MutableInteractionSource() }

    val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val lightGreyColor = colorResource(id = R.color.light_grey).toArgb()
    val lighterGreyColor = colorResource(id = R.color.lighter_grey).toArgb()
    val greyColor = colorResource(id = R.color.grey).toArgb()
    val whiteColor = colorResource(id = R.color.white).toArgb()

    val keyboard = LocalSoftwareKeyboardController.current
    //endregion

    LaunchedEffect(ticks) {
        if(!state.isVisible) {
            ticks = 0
            ticks = ticksDefaultValue
        }
    }

    LaunchedEffect(state.isVisible){
        if(!state.isVisible) searchTextFieldState = ""
    }

    Box(modifier = Modifier.fillMaxSize()) {
        //region Bottom flyout menu
        ModalBottomSheetLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            sheetState = state,
            sheetShape = RoundedCornerShape(16.dp),
            sheetContent = {
                if (whatWasPressed == 0) {
                    Column(modifier = Modifier.fillMaxHeight(0.9f)) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(id = R.string.phone_screen_insert_code),
                            modifier = Modifier
                                .fillMaxWidth(),
                            fontSize = 22.sp,
                            fontFamily = boldFont,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(id = R.string.phone_screen_code_sent),
                            modifier = Modifier
                                .fillMaxWidth(),
                            fontSize = 15.sp,
                            fontFamily = commonFont,
                            color = Color(colorGrey),
                            textAlign = TextAlign.Center
                        )
                        BasicTextField(
                            value = regionTextFieldState + numberTextFieldState,
                            onValueChange = {},
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = commonFont,
                                color = Color(colorGrey),
                                textAlign = TextAlign.Center
                            ),
                            visualTransformation = PhoneNumberVisualTransformation()
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Неверный код подтверждения",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth(),
                            fontFamily = commonFont,
                            fontSize = 15.sp,
                            color = if(isErrorInCode) Color(colorRed) else Color.White
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            PinInput(
                                onValueChanged = {
                                    smsCodeTextFieldState = it
                                    Timber.d("LoginScreen %s", "Current code is: $it")
                                    if(smsCodeTextFieldState.length == 4) {
                                        scope.launch {
                                            if (!viewModel.login(smsCodeTextFieldState)) {
                                                smsCodeTextFieldState = ""
                                                isErrorInCode = true
                                            } else {
                                                navController.navigate("main_screen") {
                                                    popUpTo("login_screen") {
                                                        inclusive = true
                                                    }
                                                }
                                            }
                                        }
                                    }
                                },
                                value = smsCodeTextFieldState,
                                length = 4,
                                _focusRequester = focusRequester
                            )
                        }
                        Spacer(modifier = Modifier.height(50.dp))
                        Spacer(modifier = Modifier.height(100.dp))
                        if (!isTimerRunning) {
                            Text(
                                text = sendNewCodeString,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        smsCodeTextFieldState = ""
                                        isTimerRunning = true
                                        ticks = ticksDefaultValue
                                        scope.launch {
                                            viewModel.requestCode()
                                            isTimerRunning = true
                                            ticks = ticksDefaultValue
                                            while (ticks > 0) {
                                                Timber.tag("TimerIsRunning")
                                                    .d("Seng again %s", ticks)
                                                delay(1.seconds)
                                                ticks--
                                                smsCodeText = sendNewCodeIn
                                                colorState = colorGrey
                                                if (!state.isVisible) {
                                                    Timber.tag("TimerIsStopped")
                                                        .d("Seng again stopped on bottom close %s", ticks)
                                                    isTimerRunning = false
                                                    return@launch
                                                }
                                            }
                                            Timber.tag("TimerIsStopped")
                                                .d("Seng again stopped on end %s", ticks)
                                            smsCodeText = sendNewCodeString
                                            colorState = colorRed
                                            isTimerRunning = false
                                            return@launch
                                        }
                                    },
                                fontFamily = boldFont,
                                fontSize = 17.sp,
                                color = Color(colorRed)
                            )
                        } else {
                            Text(
                                text = smsCodeText,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                fontFamily = boldFont,
                                fontSize = 17.sp,
                                color = Color(colorState)
                            )
                        }
                    }
                } else if (whatWasPressed == 1) {
                    Column(
                        modifier = Modifier.fillMaxHeight(0.9f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))
                        // region HEADER
                        Text(
                            text = "Выберите код страны",
                            modifier = Modifier
                                .fillMaxWidth(),
                            fontSize = 16.sp,
                            fontFamily = boldFont,
                            textAlign = TextAlign.Center
                        )
                        // endregion
                        Spacer(modifier = Modifier.height(20.dp))
                        // region SEARCH_REGION
                        Row(
                            modifier = Modifier
                                .background(
                                    color = Color(lighterGreyColor),
                                    shape = RoundedCornerShape(17.dp)
                                )
                                .height(34.dp)
                                .padding(horizontal = 12.dp, vertical = 1.dp)
                                .wrapContentSize(align = Alignment.CenterStart)
                                .padding(end = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image(
                                imageVector = ZadachnikIcons.SearchLight,
                                contentDescription = "search",
                                modifier = Modifier
                                    .height(24.dp)
                                    .width(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = Color(lighterGreyColor),
                                        shape = RoundedCornerShape(17.dp)
                                    )
                                    .height(34.dp)
                                    .padding(horizontal = 8.dp, vertical = 1.dp)
                                    .fillMaxWidth(0.95f),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                BasicTextField(
                                    value = searchTextFieldState,
                                    onValueChange = {

                                        searchTextFieldState = it.replace("\n", "")
                                    }, modifier = Modifier
                                        .height(36.dp)
                                        .padding(top = 7.dp, bottom = 3.dp),
                                    textStyle = LocalTextStyle.current.copy(
                                        textAlign = TextAlign.Start,
                                        fontFamily = commonFont,
                                        fontSize = 16.sp
                                    ),
                                    cursorBrush = Brush.verticalGradient(colors = listOf(
                                        colorResource(id = R.color.colorPrimary), colorResource(id = R.color.colorPrimary)))
                                )
                                if (searchTextFieldState.isEmpty()) Text(
                                    "Поиск",
                                    fontFamily = commonFont,
                                    fontSize = 16.sp,
                                    color = colorResource(id = R.color.grey)
                                )
                            }

                            if (searchTextFieldState != "" && searchTextFieldState != "Поиск") {
                                Image(imageVector = ZadachnikIcons.Cross,
                                    contentDescription = "delete",
                                    modifier = Modifier
                                        .height(24.dp)
                                        .width(24.dp)
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) { searchTextFieldState = "" })
                            }
                        }
                        // endregion
                        // region REGIONS
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                            items(items = viewModel.codesList, key = {
                                it.code
                            }) { card ->
                                if (card.name.lowercase(Locale.getDefault())
                                        .contains(searchTextFieldState.lowercase(Locale.getDefault()))
                                ) {
                                    CodesCell(codeModel = card, onClick = {
                                        regionTextFieldState = "+" + card.dialCode
                                        regionText = card.name
                                        scope.launch {
                                            state.animateTo(ModalBottomSheetValue.Hidden)
                                        }
                                    })
                                }

                            }
                        }
                        // endregion
                    }
                }

            }
        )
        //endregion
        //region main content
        {

            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)


            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.TopCenter),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    LoginScreenHeader()
                    Text(
                        regionText,
                        fontSize = 16.sp,
                        fontFamily = commonFont,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
                        Column{
                            // region PHONE_INPUT
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = regionTextFieldState,
                                    fontFamily = commonFont,
                                    fontSize = 17.sp,
                                    maxLines = 1,
                                    modifier = Modifier
                                        .defaultMinSize(minWidth = 64.dp)
                                        .height(32.dp)
                                        .background(Color.White)
                                        .padding(start = 20.dp, end = 12.dp)
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) {
                                            whatWasPressed = 1
                                            scope.launch {
                                                state.animateTo(ModalBottomSheetValue.Expanded)
                                            }
                                        }
                                )
                                Box(
                                    modifier = Modifier
                                        .height(49.dp)
                                        .width(1.dp)
                                        .border(
                                            1.dp,
                                            shape = RectangleShape,
                                            color = Color.LightGray
                                        )
                                )
                                Box {
                                    BasicTextField(
                                        value = numberTextFieldState,
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number
                                        ),
                                        textStyle = TextStyle.Default.copy(
                                            fontFamily = commonFont,
                                            fontSize = 17.sp,
                                        ),
                                        onValueChange = {
                                            buttonEnabled =
                                                (it.replace(" ", "").replace("(", "").replace(")", "").replace("-", "").length == 10
                                                        && regionTextFieldState.length >= 2)
                                            if(it.length <= 10) numberTextFieldState = it
                                        },
                                        singleLine = true,
                                        enabled = true,
                                        /*visualTransformation = PhoneNumberVisualTransformation(),*/
                                        cursorBrush = Brush.verticalGradient(colors = listOf(
                                            colorResource(id = R.color.colorPrimary), colorResource(id = R.color.colorPrimary))),

                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(32.dp)
                                            .background(Color.White)
                                            .padding(start = 20.dp, end = 24.dp)
                                    )
                                    if (numberTextFieldState.isEmpty()) Text(
                                        "Номер телефона",
                                        fontFamily = commonFont,
                                        fontSize = 17.sp,
                                        modifier = Modifier.padding(start = 20.dp),
                                        color = Color(lighterColorGrey)
                                    )
                                }
                            }
                            Divider()
                            // endregion
                        }
                        // region PROCEED_BUTTON
                        Column(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                        ) {

                            Button(
                                onClick =
                                {
                                    isTimerRunning = true
                                    keyboard?.hide()
                                    if (regionTextFieldState.length >= 2 &&
                                        numberTextFieldState
                                            .replace(" ", "")
                                            .replace("(", "")
                                            .replace(")", "")
                                            .replace("-", "").length == 10 &&
                                                buttonEnabled
                                    ) {
                                        viewModel.setPhoneNumber(
                                            regionTextFieldState.replace(
                                                "+",
                                                ""
                                            ) + numberTextFieldState.replace(" ", "").replace("(", "").replace(")", "").replace("-", "")
                                        )
                                        scope.launch {
                                            if (viewModel.requestCode()) {
                                                state.animateTo(ModalBottomSheetValue.Expanded)
                                                whatWasPressed = 0
                                                ticks = ticksDefaultValue
                                                smsCodeTextFieldState = ""
                                                focusRequester.requestFocus()
                                                keyboard?.show()
                                                while (ticks > 0) {
                                                    Timber.tag("TimerIsRunning")
                                                        .d("Proceed %s", ticks)
                                                    delay(1.seconds)
                                                    ticks--
                                                    smsCodeText = sendNewCodeIn
                                                    colorState = colorGrey
                                                    if (!state.isVisible) {
                                                        Timber.tag("TimerIsStopped")
                                                            .d("Proceed stopped on bottom close %s", ticks)
                                                        isTimerRunning = false
                                                        return@launch
                                                    }
                                                }
                                                Timber.tag("TimerIsStopped")
                                                    .d("Proceed stopped on end %s", ticks)
                                                smsCodeText = sendNewCodeString
                                                colorState = colorRed
                                                isTimerRunning = false
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .height(56.dp)
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp),
                                colors =
                                ButtonDefaults.buttonColors(
                                    backgroundColor = if(buttonEnabled) colorResource(id = R.color.colorPrimary) else Color(whiteColor)

                                ),
                                shape = RoundedCornerShape(28.dp),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = Color.LightGray
                                ),
                                elevation = elevation(
                                    defaultElevation = 0.dp,
                                    pressedElevation = 0.dp
                                ),
                            ) {
                                Text(
                                    text = "Продолжить",
                                    fontFamily = boldFont,
                                    color = if(buttonEnabled) Color(whiteColor) else Color(greyColor)
                                )
                            }
                        }
                        // endregion
                    }
                }
            }
        }
        if (loadingState) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(colorRed))
            }
        }
        //endregion
    }

}

@Composable
fun LoginScreenHeader() {
    Column {
        Box(
            modifier = Modifier
                .padding(24.dp)
        )
        Text(
            modifier = Modifier.fillMaxWidth(), text = stringResource(
                id = R.string.phone_screen_insert_number
            ),
            fontSize = 22.sp,
            fontFamily = boldFont,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(53.dp))


    }
}

fun processTimerText(minutes: Int, seconds: Int) : String {
    var out = "0${minutes}:"
    if(seconds < 10){
        out += "0${seconds}"
    } else {
        out += "${seconds}"
    }
    return out
}
