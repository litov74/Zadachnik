@file:OptIn(ExperimentalMaterialApi::class)

package ru.baccasoft.zadachnik.features.mainScreen


import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.FloatingActionButtonDefaults.elevation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import ru.baccasoft.zadachnik.R
import ru.baccasoft.zadachnik.Utils
import ru.baccasoft.zadachnik.base.*
import ru.baccasoft.zadachnik.data.network.models.*
import ru.baccasoft.zadachnik.data.prefs.PrefsHelper
import ru.baccasoft.zadachnik.dialogs.showErrorAlert
import ru.baccasoft.zadachnik.features.loginScreen.LoginScreenViewModel
import ru.baccasoft.zadachnik.icons.ZadachnikIcons
import ru.baccasoft.zadachnik.icons.additions.additions2.Additions2
import ru.baccasoft.zadachnik.icons.additions.additions2.additions2.DetailsSend
import ru.baccasoft.zadachnik.icons.zadachnikicons.*
import ru.baccasoft.zadachnik.ui.theme.boldFont
import ru.baccasoft.zadachnik.ui.theme.commonFont
import ru.baccasoft.zadachnik.ui.theme.thinFont
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


const val ACTION_ITEM_SIZE = 64
const val CARD_HEIGHT = 64
const val CARD_OFFSET = 60f


@Composable
fun MainScreen(navController: NavController, hintsEnabled: Boolean) {
    hiltViewModel<MainScreenViewModel>()
}

fun processDateText(days: Int, months: Int, year: Int) : String {
    var out = ""
    if(days < 10) {
        out += "0${days}"
    } else {
        out += "$days"
    }
    out += "."
    if(months < 10){
        out += "0${months}"
    } else {
        out += "$months"
    }
    out += ".${year}"
    return out
}

fun processTimeText(hours: Int, minutes: Int) : String {
    var out = ""
    if(hours < 10) {
        out += "0${hours}"
    } else {
        out += "$hours"
    }
    out += ":"
    if(minutes < 10){
        out += "0${minutes}"
    } else {
        out += "$minutes"
    }
    return out
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "Range")
@Composable
fun MainScreenOpenedTask(
    nameTextFieldState: MutableState<String>,
    workerTextFieldState: MutableState<String>,
    workerPhoneState: MutableState<String>,
    descriptionTextFieldState: MutableState<String>,
    documentsList:  MutableState<ArrayList<FilesListModel>>,
    onTimeClick: () -> Unit,
    selectedDate: MutableState<SelectedDateModel>,
    selectedTime: MutableState<SelectedTimeModel>,
    context: Context,
    result: MutableState<Uri?>,
    onFileClick: (FilesListModel) -> Unit,
    onCancelClick: (FilesListModel) -> Unit,
    comments: State<List<DataModel>>,
    onCommentAdd: (DataModel) -> Unit,
    phone: String,
    text: MutableState<String>,
    onProceedButtonClick: () -> Unit,
    onTitleSpeechRecognition: () -> Unit,
    onDescriptionSpeechRecognition: () -> Unit,
    remindIn: MutableState<Int>,
    fastDateSelector: MutableState<Int>,
    isEdited: MutableState<Boolean>,
    isCreated: Boolean,
    commentTextFieldState: MutableState<String>,
    isTaskCreated: MutableState<Boolean>,
    getTaskModel: GetTaskModel,
    currentUserId: String,
    updateComments: MutableState<Boolean>,
    onCloseClick: () -> Unit,
    isFromArchive: MutableState<Boolean>,
    rememberScrollStateDetails: ScrollState,
    nestedFilesHorizontalScroll: ScrollState,
    onContactsError: () -> Unit,
    downloadingProgress: MutableState<Float>,
    currentSelTask: MutableState<String>,
    ) {

    val focusManager = LocalFocusManager.current

    Timber.d("MainScreenOpenedTask  %s", "${documentsList.value}")

    val isСreatorTask = getTaskModel.creatorId != currentUserId
    val isEditLockNotAuthor = isСreatorTask && !isCreated

    // region STATES
    val interactionSource = remember { MutableInteractionSource() }

    // endregion
    // region STATIC
    val messageString = stringResource(id = R.string.message)
    val nameString = stringResource(id = R.string.name)
    val workerString = stringResource(id = R.string.worker)
    val dueDateString = stringResource(id = R.string.due_date)
    val nestedFilesString = stringResource(id = R.string.nested_files)
    val remindInString = stringResource(id = R.string.remind_in)
    val messageTextString = stringResource(id = R.string.message_text)
    val commentTextString = stringResource(id = R.string.comment)
    val colorRed = colorResource(id = R.color.colorPrimary).toArgb()

    val sdf = SimpleDateFormat("dd/M/yyyy")
    val currentDate = sdf.format(Date())
    var initialDate = selectedDate.value.day.toString() + "-" + selectedDate.value.month.toString() + "-" + selectedDate.value.year.toString()

    val redColor = colorResource(id = R.color.colorPrimary).toArgb()
    // endregion
    // region SPECIAL
    val activity = context as ComponentActivity
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickContact()) {
        result.value = it
    }

    val iSButtonSendEnabled = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = nameTextFieldState.value, key2 = isEdited.value) {
        iSButtonSendEnabled.value = if (isTaskCreated.value) nameTextFieldState.value.isNotEmpty()
        else (isEdited.value && nameTextFieldState.value.isNotEmpty())
    }

    LaunchedEffect(text.value) {
        if(!text.value.isNullOrBlank()) {
            rememberScrollStateDetails.animateScrollTo(rememberScrollStateDetails.maxValue, tween(0))
        }
    }

    // endregion
    Scaffold(
        backgroundColor = Color.White,
        modifier = Modifier.heightIn(min = 100.dp, max = (LocalConfiguration.current.screenHeightDp - 20).dp),
        // region ROUNDED_CORNER_BUTTON
        floatingActionButton = {
            // region BUTTON
            if(!isEditLockNotAuthor) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                ) {
                    RoundedCornersSquareButton(onClick = {
                        onProceedButtonClick()
                    }, content = {
                        Text(
                            text = if (isTaskCreated.value) stringResource(R.string.SEND)
                            else stringResource(R.string.SAVE),
                            fontFamily = boldFont,
                            fontSize = 15.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = if(iSButtonSendEnabled.value) Color.White else colorResource(id = R.color.grey)
                        )
                    }, shape = RoundedCornerShape(28.dp),
                        isEnabled = iSButtonSendEnabled
                    )
                }
            }
            // endregion
        },
        floatingActionButtonPosition = FabPosition.Center
        // endregion
    ) { paddingValues ->
        Column{
            Column(modifier = Modifier
                .padding(top = 12.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                // region HEADER
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .height(6.dp)
                            .width(46.dp)
                            .background(
                                color = colorResource(
                                    id = R.color.message_grey
                                ),
                                shape = RoundedCornerShape(3.dp)
                            ),
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
                    Text(
                        text = messageString,
                        fontFamily = boldFont,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Left
                    )
                    Image(imageVector = ZadachnikIcons.Cross, contentDescription = "", modifier = Modifier
                        .height(24.dp)
                        .width(24.dp)
                        .clickable(interactionSource = interactionSource, indication = null) {
                            focusManager.clearFocus()
                            onCloseClick()
                        })
                }
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                // endregion
            }
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollStateDetails)
                    .padding(start = 16.dp, end = 16.dp, bottom = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // region NAME
                Spacer(modifier = Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
                    Text(
                        text = nameString,
                        fontFamily = commonFont,
                        color = colorResource(id = R.color.darker_gray),
                        fontSize = 15.sp,
                        textAlign = TextAlign.Left,
                    )

                    if(!isEditLockNotAuthor) {
                        Image(imageVector = ZadachnikIcons.DetailsMic, contentDescription = "", modifier =
                        Modifier
                            .clickable(
                                interactionSource = interactionSource,
                                enabled = !isEditLockNotAuthor,
                                indication = null
                            ) {
                                focusManager.clearFocus()
                                onTitleSpeechRecognition()
                            }
                            .height(24.dp)
                            .width(24.dp))
                    } else {
                        Box(){}
                    }

                }
                BasicTextField(
                    value = nameTextFieldState.value,
                    onValueChange = {
                        nameTextFieldState.value = it
                    },
                    enabled = !isEditLockNotAuthor && !isFromArchive.value,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle.Default.copy(
                        fontFamily = commonFont,
                        fontSize = 14.sp
                    ),
                    cursorBrush = Brush.verticalGradient(colors = listOf(
                        colorResource(id = R.color.colorPrimary), colorResource(id = R.color.colorPrimary)))
                )
                Spacer(modifier = Modifier.height(12.dp))
                Divider()
                // endregion
                // region DESCRIPTION
                Spacer(modifier = Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
                    Text(
                        text = "Описание",
                        fontFamily = commonFont,
                        color = colorResource(id = R.color.darker_gray),
                        fontSize = 15.sp,
                        textAlign = TextAlign.Left,
                    )
                    if(!isEditLockNotAuthor) {
                        Image(imageVector = ZadachnikIcons.DetailsMic, contentDescription = "", modifier =
                        Modifier.clickable(
                            interactionSource = interactionSource,
                            enabled = isTaskCreated.value,
                            indication = null
                        ){
                            focusManager.clearFocus()
                            onDescriptionSpeechRecognition()
                        })
                    } else {
                        Box(){}
                    }


            }
            BasicTextField(
                value = descriptionTextFieldState.value,
                onValueChange = {
                    descriptionTextFieldState.value = it
                },
                enabled = isTaskCreated.value,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle.Default.copy(
                    fontFamily = commonFont,
                    fontSize = 14.sp
                ),
                cursorBrush = Brush.verticalGradient(colors = listOf(
                    colorResource(id = R.color.colorPrimary), colorResource(id = R.color.colorPrimary)))
            )
            Spacer(modifier = Modifier.height(12.dp))
            Divider()
            // endregion
            // region WORKER
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = workerString,
                fontFamily = commonFont,
                color = colorResource(id = R.color.darker_gray),
                fontSize = 15.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .padding(end = 32.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {

                    // region CHOOSE_CONTACT
                    result.value?.let {
                        val cursor: Cursor? = context.contentResolver.query(
                            it,
                            null,
                            null,
                            null,
                            null
                        )
                        cursor?.moveToFirst()
                        val id: String = cursor!!.getString(
                            cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID)
                        )
                        workerTextFieldState.value = try {
                            val name: String = cursor!!.getString(
                                cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                            )
                            name
                        } catch (e: Exception) {
                            ""
                        }
                        var hasPhone = cursor!!.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                        if(hasPhone.equals("1", ignoreCase = true)) {
                            var phones : Cursor = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null)!!
                            phones.moveToFirst()
                            var contactNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            Timber.d("WORKER_PHONE_STATE_CURSOR %s", contactNumber.toString())
                            workerPhoneState.value = contactNumber.toString().replace("+", "").replace(" ", "").replace("-", "")
                        }
                        result.value = null
                    }
                    // endregion

                    Text(
                        text = if (workerTextFieldState.value.isNotEmpty()) workerTextFieldState.value else stringResource(R.string.my_task),
                        fontFamily = commonFont,
                        fontSize = 14.sp
                    )
                }
                if(!isEditLockNotAuthor) {
                    Icon(imageVector = ZadachnikIcons.DetailsUser, contentDescription = "user",
                        Modifier
                            .height(24.dp)
                            .width(24.dp)
                            .clickable(
                                enabled = !isEditLockNotAuthor && !isFromArchive.value,
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                focusManager.clearFocus()
                                val utils = Utils()
                                val prefsHelper = PrefsHelper(context)
                                val requestShown = prefsHelper.getContactsRequestShown()
                                if (utils.hasContactPermission(context)) {
                                    launcher.launch()
                                } else {
                                    if (requestShown) {
                                        onContactsError()
                                    } else {
                                        utils.requestPermissionReadContacts(context, activity)
                                        prefsHelper.setContactsRequestShown(true)
                                    }
                                }
                            })
                } else {
                    Box(){}
                }

            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            // endregion
            // region DUE_DATE
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = dueDateString,
                fontFamily = boldFont,
                fontSize = 15.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                // region DUE_DATE_DATE
                RoundedCornerContainer(
                    image = if(fastDateSelector.value == 0) ZadachnikIcons.DetailsCalendar else ZadachnikIcons.DetailsCalendarBlack,
                    content = processDateText(selectedDate.value.day, selectedDate.value.month, selectedDate.value.year),
                    modifier = Modifier
                        .background(
                            color = if (fastDateSelector.value == 0) Color(redColor) else Color(
                                colorResource(id = R.color.lighter_grey).toArgb()
                            ),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(24.dp),
                            color = Color.LightGray
                        )
                        .height(36.dp)
                        .width(120.dp)
                        .clickable(
                            enabled = !isEditLockNotAuthor && !isFromArchive.value,
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            focusManager.clearFocus()
                            fastDateSelector.value = 0
                        },
                    isWhite = fastDateSelector.value == 0,
                )
                Spacer(modifier = Modifier.width(8.dp))
                // endregion
                // region DUE_DATE_TIME
                RoundedCornerContainer(
                    image = ZadachnikIcons.DetailsClock,
                    content = processTimeText(selectedTime.value.hour, selectedTime.value.minute),
                    modifier = Modifier
                        .background(
                            color = Color(colorResource(id = R.color.lighter_grey).toArgb()),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(24.dp),
                            color = Color.LightGray
                        )
                        .height(36.dp)
                        .width(110.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            enabled = !isEditLockNotAuthor && !isFromArchive.value,
                            indication = null
                        ) {
                            focusManager.clearFocus()
                            onTimeClick()
                            Timber.d("bottom_sheet %s", "onTimeClick()")
                        },

                        )
                    // endregion
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    // region DUE_DATE_TODAY
                    RoundedCornerContainer(
                        image = if(fastDateSelector.value == 1) ZadachnikIcons.DetailsSunFullWhite else ZadachnikIcons.DatailsSunFull,
                        content = "Сегодня",
                        isWhite = fastDateSelector.value == 1,
                        modifier = Modifier
                            .background(
                                color = if (fastDateSelector.value == 1) Color(redColor) else Color.White,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(24.dp),
                                color = Color.LightGray
                            )
                            .height(36.dp)
                            .width(105.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                enabled = !isEditLockNotAuthor && !isFromArchive.value,
                                indication = null
                            ) {
                                focusManager.clearFocus()
                                if (fastDateSelector.value != 1) fastDateSelector.value = 1
                                else fastDateSelector.value = -1
                            }
                    )
                    Spacer(Modifier.width(8.dp))
                    // endregion
                    // region DUE_DATE_TOMORROW
                    RoundedCornerContainer(
                        image = if(fastDateSelector.value == 2) ZadachnikIcons.DetailsSunHalfWhite else ZadachnikIcons.DetailsSunHalf,
                        content = "Завтра",
                        isWhite = fastDateSelector.value == 2,
                        modifier = Modifier
                            .background(
                                color = if (fastDateSelector.value == 2) Color(redColor) else Color.White,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(24.dp),
                                color = Color.LightGray
                            )
                            .height(36.dp)
                            .width(105.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                enabled = !isEditLockNotAuthor && !isFromArchive.value,
                                indication = null
                            ) {
                                focusManager.clearFocus()
                                if (fastDateSelector.value != 2) fastDateSelector.value = 2
                                else fastDateSelector.value = -1
                            }
                    )
                    Spacer(Modifier.width(8.dp))
                    // endregion
                    // region DUE_DATE_WEEK
                    RoundedCornerContainer(
                        image = if(fastDateSelector.value == 3) ZadachnikIcons.DetailsWeekWhite else ZadachnikIcons.DetailsWeek,
                        content = "Неделя",
                        isWhite = fastDateSelector.value == 3,
                        modifier = Modifier
                            .background(
                                color = if (fastDateSelector.value == 3) Color(redColor) else Color.White,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(24.dp),
                                color = Color.LightGray
                            )
                            .height(36.dp)
                            .width(105.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                enabled = !isEditLockNotAuthor && !isFromArchive.value,
                                indication = null
                            ) {
                                focusManager.clearFocus()
                                if (fastDateSelector.value != 3) fastDateSelector.value = 3
                                else fastDateSelector.value = -1
                            }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // endregion
                }
                if(fastDateSelector.value == 0) {
                    Box {
                        val localDensity = LocalDensity.current
                        var heightIs by remember {
                            mutableStateOf(0.dp)
                        }
                        AndroidView(
                            {
                                CalendarView(ContextThemeWrapper(it, R.style.CalenderViewCustom))
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .onGloballyPositioned { coordinates ->
                                    heightIs = with(localDensity) { coordinates.size.height.toDp() }
                                },
                            update = { views ->
                                views.firstDayOfWeek = Calendar.MONDAY
                                if(isCreated) {
                                    views.minDate = DateTime().millis
                                }
                                val initialDate = selectedDate.value.year.toString() + "-" + selectedDate.value.month.toString() + "-" + selectedDate.value.day.toString()
                                val formatter = SimpleDateFormat("yyyy-MM-dd")
                                val date = formatter.parse(initialDate)
                                views.date = date!!.time
                                views.setOnDateChangeListener { _, year, month, date ->
                                    val cal = Calendar.getInstance()
                                    cal.set(year, month, date)
                                    fastDateSelector.value = -2 // -1 не ставить, календарь не будет выбирать дату. -1 чисто для обнуления до текущего дня
                                    selectedDate.value = SelectedDateModel(year, month + 1, date)
                                }
                            }
                        )
                        if (isEditLockNotAuthor && !isFromArchive.value) {
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0x00000000))
                                .size(heightIs)
                                .clickable {
                                    focusManager.clearFocus()
                                })
                        }
                    }
                }
                // endregion
                // region REMIND_IN
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = remindInString,
                    fontFamily = boldFont,
                    fontSize = 15.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RoundedCornerContainer(
                        content = "15 минут",
                        modifier = Modifier
                            .background(
                                color = if (remindIn.value == 0) Color(redColor) else Color.White,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(24.dp),
                                color = Color.LightGray
                            )
                            .height(36.dp)
                            .width(85.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                enabled = !isEditLockNotAuthor,
                                indication = null
                            ) {
                                focusManager.clearFocus()
                                if (remindIn.value != 0) remindIn.value = 0
                                else remindIn.value = -1
                            },
                        isWhite = remindIn.value == 0
                    )
                    RoundedCornerContainer(
                        content = "1 час",
                        modifier = Modifier
                            .background(
                                color = if (remindIn.value == 1) Color(redColor) else Color.White,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(24.dp),
                                color = Color.LightGray
                            )
                            .height(36.dp)
                            .width(60.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                enabled = !isEditLockNotAuthor,
                                indication = null
                            ) {
                                focusManager.clearFocus()
                                if (remindIn.value != 1) remindIn.value = 1
                                else remindIn.value = -1
                            },
                        isWhite = remindIn.value == 1
                    )
                    RoundedCornerContainer(
                        content = "4 часа",
                        modifier = Modifier
                            .background(
                                color = if (remindIn.value == 2) Color(redColor) else Color.White,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(24.dp),
                                color = Color.LightGray
                            )
                            .height(36.dp)
                            .width(85.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                enabled = !isEditLockNotAuthor,
                                indication = null
                            ) {
                                focusManager.clearFocus()
                                if (remindIn.value != 2) remindIn.value = 2
                                else remindIn.value = -1
                            },
                        isWhite = remindIn.value == 2
                    )
                    RoundedCornerContainer(
                        content = "1 день",
                        modifier = Modifier
                            .background(
                                color = if (remindIn.value == 3) Color(redColor) else Color.White,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(24.dp),
                                color = Color.LightGray
                            )
                            .height(36.dp)
                            .width(85.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                enabled = !isEditLockNotAuthor,
                                indication = null
                            ) {
                                focusManager.clearFocus()
                                if (remindIn.value != 3) remindIn.value = 3
                                else remindIn.value = -1
                            },
                        isWhite = remindIn.value == 3
                    )
                }

                // endregion
                Timber.tag("TaskIsCreated").d("$isCreated")
                Timber.tag("NestedFilesCounterCheck").d("${documentsList.value.size}")
                if(!isCreated) {
                    if(documentsList.value.isNotEmpty()) {
                        // region NESTED_FILES
                        Spacer(modifier = Modifier.height(40.dp))
                        Text(
                            text = nestedFilesString,
                            fontFamily = boldFont,
                            fontSize = 15.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Left
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        var progressPlaceholder = remember {
                            mutableStateOf(0.0f)
                        }
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(nestedFilesHorizontalScroll), horizontalArrangement = Arrangement.Start){
                            repeat(times = documentsList.value.size, action = {
                                Timber.tag("NestedFilesCounterCheck").d("${documentsList.value.size}")
                                NestedFileItem(
                                    image = ZadachnikIcons.DetailsDoc,
                                    title = documentsList.value[it].filename ?: "",
                                    size = (documentsList.value[it].fileSize ?: ""),
                                    onFileClick = {
                                        focusManager.clearFocus()
                                        onFileClick(documentsList.value[it])
                                    },
                                    progress = downloadingProgress,
                                    currentSelTask = currentSelTask
                                )
                            })
                        }
                        // endregion
                    }
                    // region MESSAGE_TEXT
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
                        Text(
                            text = commentTextString,
                            fontFamily = boldFont,
                            fontSize = 17.sp,
                        )
                        Box(
                            modifier = Modifier
                                .background(
                                    color = colorResource(id = R.color.message_grey),
                                    shape = RoundedCornerShape(25)
                                )
                                .padding(2.dp)
                                .defaultMinSize(minWidth = 24.dp, minHeight = 24.dp),
                            contentAlignment = Alignment.Center
                        ){
                            Text(text = if(workerTextFieldState.value == "Моя задача" || getTaskModel.responsibleId == currentUserId) "" else workerTextFieldState.value
                                , fontFamily = commonFont, fontSize = 10.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(Modifier.height(8.dp))
                    MainScreenBottomComments(
                        comments = comments,
                        isAuthor = !isEditLockNotAuthor,
                        responsible = workerTextFieldState,
                        onCommentAdd = { onCommentAdd(it) },
                        phone = phone,
                        text = text,
                        scroll = rememberScrollStateDetails,
                        update = updateComments,
                    )
                    // endregion
                } else {
                    val scope = rememberCoroutineScope()
                    // region MESSAGE_TEXT
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
                        Text(
                            text = "Комментарий",
                            fontFamily = commonFont,
                            color = colorResource(id = R.color.darker_gray),
                            fontSize = 15.sp,
                            textAlign = TextAlign.Left,
                        )
                    }
                    BasicTextField(
                        value = commentTextFieldState.value,
                        onValueChange = {
                            commentTextFieldState.value = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusEvent {
                                if (it.isFocused) scope.launch {
                                    delay(500L)
                                    rememberScrollStateDetails.animateScrollTo(
                                        rememberScrollStateDetails.maxValue,
                                        tween(0)
                                    )
                                }
                            },
                        textStyle = TextStyle.Default.copy(
                            fontFamily = commonFont,
                            fontSize = 14.sp
                        ),
                        cursorBrush = Brush.verticalGradient(colors = listOf(
                            colorResource(id = R.color.colorPrimary), colorResource(id = R.color.colorPrimary)))
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider()
                    // endregion
                }
                Box(modifier = Modifier.height(10.dp)){}
            }
        }
    }
}

@Composable
fun MainScreenFABContent(
    onClick: () -> Unit
) {

    val redColor = colorResource(id = R.color.colorPrimary).toArgb()
    val interactionSource = remember { MutableInteractionSource() }

    Box(modifier = Modifier
        .height(140.dp)
        .width(60.dp)
        .padding(bottom = 80.dp)){
        FloatingActionButton(
            onClick = {
                onClick()
            },
            backgroundColor = Color(redColor),
            elevation = elevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "",
                tint = Color.White
            )
        }
    }
}

@Composable
fun MainScreenAppBar(
    searchTextFieldState: State<String>,
    placeholder: String = "Поиск по задачам",
    onClick: () -> Unit,
    integration: (@Composable() () -> Unit),
    onTextChange: (String) -> Unit,
) {
    val focus = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    Column(modifier = Modifier.fillMaxWidth()){
        TopAppBar(
            modifier = Modifier.wrapContentSize(),
            title = {
                // region SEARCH
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_menu_24),
                        contentDescription = "menu",
                        modifier = Modifier.pointerInput(Unit){
                            detectTapGestures(onTap = {
                                focus.clearFocus()
                                onClick()
                            })
                        }
                    )
                }
                Row(
                    modifier = Modifier
                        .background(
                            color = Color(colorResource(id = R.color.lighter_grey).toArgb()),
                            shape = RoundedCornerShape(18.dp)
                        )
                        .height(34.dp)
                        .padding(horizontal = 12.dp, vertical = 1.dp)
                        .fillMaxWidth(0.95f)
                        .padding(end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically){
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
                                    color = Color(colorResource(id = R.color.lighter_grey).toArgb()),
                                    shape = RoundedCornerShape(18.dp)
                                )
                                .height(34.dp)
                                .padding(top = 1.dp, bottom = 1.dp, start = 8.dp, end = 8.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            BasicTextField(
                                value = searchTextFieldState.value,
                                onValueChange = {
                                    onTextChange.invoke(it)
                                }, modifier = Modifier
                                    .height(36.dp)
                                    .padding(top = 7.dp, bottom = 3.dp),
                                textStyle = LocalTextStyle.current.copy(
                                    textAlign = TextAlign.Start,
                                    fontFamily = commonFont,
                                    fontSize = 16.sp,
                                    color = colorResource(id = R.color.grey)
                                ),
                                cursorBrush = Brush.verticalGradient(colors = listOf(
                                    colorResource(id = R.color.colorPrimary), colorResource(id = R.color.colorPrimary)))
                            )
                            if (searchTextFieldState.value.isEmpty()) Text(
                                placeholder,
                                fontFamily = commonFont,
                                fontSize = 16.sp,
                                color = colorResource(id = R.color.grey)
                            )
                        }

                    }

                    if (searchTextFieldState.value != "") {
                        Image(imageVector = ZadachnikIcons.Cross,
                            contentDescription = "delete",
                            modifier = Modifier
                                .height(24.dp)
                                .width(24.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) { onTextChange.invoke("") })
                    }
                }
                // endregion
                Spacer(modifier = Modifier.height(10.dp))
            },
            backgroundColor = colorResource(id = R.color.white),
        )
        integration()
    }
}


@Composable
fun MainScreenTaskList(
    tasksPagedList: LazyPagingItems<TaskModel>,
    onArchiveClick: (TaskModel) -> Unit,
    onCardClick: (TaskModel) -> Unit,
    revealedTaskIds: State<List<String>>,
    onExpand: (String) -> Unit,
    onCollapse: (String) -> Unit,
    unarchive: Boolean = false,
    pullRefreshState: PullRefreshState,
    refreshing: MutableState<Boolean>,
) {
    Spacer(modifier = Modifier.height(5.dp))
    Timber.d("Tasks counter check  %s", "${tasksPagedList.itemCount}")

    Box(
        Modifier
            .fillMaxSize()
            .zIndex(-1f)
            .pullRefresh(pullRefreshState), contentAlignment = Alignment.Center) {
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            items(tasksPagedList) { card ->
                card?.let { card ->
                    Box(Modifier.fillMaxWidth()) {
                        ActionRow(
                            unarchive = unarchive,
                            toArchive = {
                                onArchiveClick(card)
                            }
                        )
                        DraggableCard(
                            card = card,
                            isRevealed = revealedTaskIds.value.contains(card.id),
                            cardHeight = CARD_HEIGHT.dp,
                            cardOffset = CARD_OFFSET.dp(),
                            onExpand = { onExpand(it.id ?: "") },
                            onCollapse = { onCollapse(it.id ?: "") },
                            onClick = {
                                onCardClick(card)
                            }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreenHints(
    hint: HintModel,
) {
    // region HINTS
    Timber.d("Hints %s", hint.toString())
    val interactionSource = remember { MutableInteractionSource() }
    val uriHandler = LocalUriHandler.current
    val isExpanded = remember { mutableStateOf(false) }

    Row(horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(colorResource(id = R.color.lighter_grey).toArgb()))
            .padding(10.dp)
        ) {
        Image(
            imageVector = ZadachnikIcons.HintsLamp,
            contentDescription = "",
            Modifier
                .height(24.dp)
                .width(24.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(0.85f)
        ) {
            if (!isExpanded.value) {
                Text(text = hint.name,
                    maxLines = 2,
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontFamily = commonFont,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            if (hint.url.contains("http") || hint.url.contains(
                                    "https"
                                )
                            ) {
                                uriHandler.openUri(hint.url)
                            }
                        })
            } else {
                Text(text = hint.name,
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontFamily = commonFont,
                    modifier = Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            if (hint.url.contains("http") || hint.url.contains(
                                    "https"
                                )
                            ) {
                                uriHandler.openUri(hint.url)
                            }
                        })
            }
        }
        Image(imageVector = ZadachnikIcons.HintsDown,
            contentDescription = "",
            modifier = Modifier
                .height(24.dp)
                .width(24.dp)
                .graphicsLayer {
                    rotationZ = if (isExpanded.value) 180f else 0f
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    isExpanded.value = !isExpanded.value
                })
    }
    // endregion
}

@Composable
fun MainScreenFilter(
    activeFilter: State<Int>,
    increase: State<Boolean>,
    mine: State<Boolean>,
    fromMe: State<Boolean>,
    tasksPagedList: LazyPagingItems<TaskModel>,
    onFilterChanged: (Int, Boolean, Boolean, Boolean) -> Unit
) {

    val lightGreyColor = colorResource(id = R.color.light_grey).toArgb()
    val redColor = colorResource(id = R.color.colorPrimary).toArgb()
    val interactionSource = remember { MutableInteractionSource() }
    val arrowDown = "↓"
    val arrowUp = "↑"
    val focus = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(color = Color.White)
            .padding(top = 5.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)){
            // region MINE_FILTER
            Row(
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = if (mine.value) {
                                Color(redColor)
                            } else {
                                Color.White
                            },
                            shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                        )
                        .height(32.dp)
                        .width(50.dp)
                        .border(
                            width = 1.dp,
                            color = if (mine.value) {
                                Color(redColor)
                            } else {
                                Color(lightGreyColor)
                            },
                            shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                        )
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                focus.clearFocus()
                                if (mine.value) onFilterChanged(
                                    activeFilter.value,
                                    increase.value,
                                    false,
                                    fromMe.value
                                )
                                if (!mine.value) onFilterChanged(
                                    activeFilter.value,
                                    increase.value,
                                    true,
                                    false
                                )
                                tasksPagedList.refresh()
                            })
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Мои",
                        maxLines = 1,
                        fontFamily = commonFont, color =
                        if (mine.value) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    )
                }
                Box(
                    modifier = Modifier
                        .background(
                            color = if (fromMe.value) {
                                Color(redColor)
                            } else {
                                Color.White
                            },
                            shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                        )
                        .height(32.dp)
                        .width(70.dp)
                        .border(
                            width = 1.dp,
                            color = if (fromMe.value) {
                                Color(redColor)
                            } else {
                                Color(lightGreyColor)
                            },
                            shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                        )
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                focus.clearFocus()
                                if (fromMe.value) onFilterChanged(
                                    activeFilter.value,
                                    increase.value,
                                    mine.value,
                                    false
                                )
                                if (!fromMe.value) onFilterChanged(
                                    activeFilter.value,
                                    increase.value,
                                    false,
                                    true
                                )
                                tasksPagedList.refresh()
                            })
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "От меня",
                        maxLines = 1,
                        fontFamily = commonFont, color =
                        if (fromMe.value) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    )
                }
            }
            // endregion
            // region RESPONSIBLE_FILTER
            Box(
                modifier = Modifier
                    .background(
                        color = if (activeFilter.value == 1) {
                            Color(redColor)
                        } else {
                            Color.White
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                    .height(32.dp)
                    .width(140.dp)
                    .border(
                        width = 1.dp,
                        color = if (activeFilter.value == 1) {
                            Color(redColor)
                        } else {
                            Color(lightGreyColor)
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focus.clearFocus()
                            if (activeFilter.value == 1) onFilterChanged(
                                activeFilter.value,
                                !increase.value,
                                mine.value,
                                fromMe.value
                            )
                            if (activeFilter.value != 1) onFilterChanged(
                                1,
                                true,
                                mine.value,
                                fromMe.value
                            )
                            Log.d("MainScreenFilter","${activeFilter.value} ${increase.value} ${mine.value} ${fromMe.value}")
                            tasksPagedList.refresh()
                        })
                    }, contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (activeFilter.value == 1 && increase.value) {
                        "Ответственный $arrowUp"
                    } else if (activeFilter.value == 1 && !increase.value) {
                        "Ответственный $arrowDown"
                    } else {
                        "Ответственный"
                    },
                    fontFamily = commonFont,
                    maxLines = 1,
                    color =
                    if (activeFilter.value == 1) {
                        Color.White
                    } else {
                        Color.Black
                    }
                )
            }
            // endregion
            // region DATE_FILTER
            Box(
                modifier = Modifier
                    .background(
                        color = if (activeFilter.value == 2) {
                            Color(redColor)
                        } else {
                            Color.White
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                    .height(32.dp)
                    .width(70.dp)
                    .border(
                        width = 1.dp,
                        color = if (activeFilter.value == 2) {
                            Color(redColor)
                        } else {
                            Color(lightGreyColor)
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focus.clearFocus()
                            if (activeFilter.value == 2) onFilterChanged(
                                activeFilter.value,
                                !increase.value,
                                mine.value,
                                fromMe.value
                            )
                            if (activeFilter.value != 2) onFilterChanged(
                                2,
                                true,
                                mine.value,
                                fromMe.value
                            )
                            Log.d("MainScreenFilter","${activeFilter.value} ${increase.value} ${mine.value} ${fromMe.value}")
                            tasksPagedList.refresh()
                        })
                    }, contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (activeFilter.value == 2 && increase.value) {
                            "Дата $arrowUp"
                        } else if (activeFilter.value == 2 && !increase.value) {
                            "Дата $arrowDown"
                        } else {
                            "Дата"
                        },
                        fontFamily = commonFont,
                        maxLines = 1,
                        color =
                        if (activeFilter.value == 2) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    )
                }
            }
            // endregion
        }
    }
}

@Composable
fun MainScreenBottomComments(
    comments: State<List<DataModel>>,
    isAuthor: Boolean,
    responsible: MutableState<String>,
    onCommentAdd: (DataModel) -> Unit,
    phone: String,
    scroll: ScrollState,
    text: MutableState<String>,
    update: MutableState<Boolean>,
    ){

    Timber.d("MainScreenBottomComments %s", comments.value.toString())
    val colorRed = colorResource(id = R.color.colorPrimary).toArgb()
    val darkerGrey = colorResource(id = R.color.darker_gray)


    Column(modifier = Modifier
        .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)){

        if(!update.value) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.defaultMinSize(minHeight = 1.dp)){
                repeat(comments.value.size) {
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                        if(it == 0){
                            var date = Utils().processDate(comments.value[it].timestamp!!)
                            var month = Utils().monthToFullName(date[3])
                            Text(text= "${date[4]} ${month} ${date[2]}", fontSize = 13.sp, color = darkerGrey)
                        }
                        if(it != 0) {
                            val _date = Utils().processDate(comments.value[it-1].timestamp!!)
                            var date = Utils().processDate(comments.value[it].timestamp!!)
                            if(_date[3] != date [3] || _date[2] != date[2] || _date[4] != date[4]){
                                var month = Utils().monthToFullName(date[3])
                                Text(text= "${date[4]} ${month} ${date[2]}", fontSize = 13.sp, color = darkerGrey)
                            }
                        }
                    }
                    MainScreenCommentBox(
                        isFromMe = comments.value[it].userRelated?.phone == phone,
                        text = comments.value[it].text ?: "",
                        author = if(isAuthor) "${responsible.value}" else "Автор (+${comments.value[it].userRelated!!.phone!!})",
                        time = comments.value[it].timestamp ?: ""
                    )

                }
            }
        }
        MessagesDownBorder(text = text, scroll = scroll, onSendClick = {
            onCommentAdd(DataModel(text = text.value, timestamp = Utils().generateCurrentDateAndTime(), userRelated = UserRelated(phone = phone)))
        })
    }
}



@Composable
fun MainScreenCommentBox(
    isFromMe: Boolean,
    text: String,
    author: String,
    time: String,
) {

    val selfGrey = colorResource(id = R.color.message_self_grey)
    val otherGrey = colorResource(id = R.color.message_grey)
    val darkerGrey = colorResource(id = R.color.darker_gray)

    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    val timeDate = sdf.parse(time).time + TimeZone.getDefault().rawOffset
    val timeLocale = sdf.format(timeDate)

    val utils = Utils().processDate(timeLocale)

    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = if(isFromMe) Arrangement.End else Arrangement.Start
    ){
        Box(
            modifier = Modifier
                .background(
                    shape = if (isFromMe) RoundedCornerShape(
                        topStart = 8.dp,
                        bottomStart = 8.dp,
                        topEnd = 8.dp
                    ) else RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomEnd = 8.dp),
                    color = if (isFromMe) selfGrey else otherGrey
                )
                .fillMaxWidth(0.55f)
        ){
            Column(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 12.dp, end = 12.dp), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.spacedBy(1.dp)) {
                if(!isFromMe){
                    Text(text = author, fontSize = 13.sp, color = darkerGrey, maxLines = 1)
                }

                val textAnnotated = buildAnnotatedString {
                    for (link in text.split(' ')) {
                        if (link.matches(".*(#\\w+)|(http(s)?://.+).*".toRegex())) {
                            pushStringAnnotation(tag = "URL",
                                annotation = link)
                            withStyle(SpanStyle(color = Color.Blue)) {
                                append(link + ' ')
                            }
                        } else {
                            append(link + ' ')
                        }
                    }
                }
                val uriHandler = LocalUriHandler.current
                ClickableText(text = textAnnotated, style = TextStyle(fontSize = 16.sp)) {
                    textAnnotated.getStringAnnotations("URL", it, it)
                        .firstOrNull()?.let { stringAnnotation ->
                            uriHandler.openUri(stringAnnotation.item)
                        }
                }
                Row(horizontalArrangement = if(isFromMe) Arrangement.Start else Arrangement.End, modifier = Modifier.fillMaxWidth()){
                    Text(text = "${utils[5]}:${utils[6]}", fontSize = 13.sp, color = darkerGrey)
                }
            }
        }
    }

}

@Composable
fun MessagesDownBorder(
    text: MutableState<String>,
    scroll: ScrollState,
    onSendClick: (String) -> Unit,
){

    val lighterGreyColor = colorResource(id = R.color.lighter_grey)
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }

    // region SEARCH_REGION
    Row(
        modifier = Modifier
            .background(
                color = lighterGreyColor,
                shape = RoundedCornerShape(17.dp)
            )
            .padding(end = 10.dp, bottom = 4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = lighterGreyColor,
                    shape = RoundedCornerShape(17.dp)
                )
                .padding(start = 10.dp)
                .fillMaxWidth(0.8f),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = text.value,
                onValueChange = {
                    text.value = it
                }, modifier = Modifier
                    .padding(top = 7.dp, bottom = 3.dp)
                    .defaultMinSize(minWidth = 250.dp)
                    .onFocusEvent {
                        if (it.isFocused) scope.launch {
                            delay(500L) // еще костыль. при открытии клавиатуры до ввода все равно перекрывает поле
                            scroll.animateScrollTo(scroll.maxValue, tween(0))
                        }
                    },
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Start,
                    fontFamily = commonFont,
                    fontSize = 16.sp
                ),
                cursorBrush = Brush.verticalGradient(colors = listOf(
                    colorResource(id = R.color.colorPrimary), colorResource(id = R.color.colorPrimary)))
            )
            if (text.value.isEmpty()) Text(
                "Сообщение",
                fontFamily = commonFont,
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                color = colorResource(id = R.color.grey)
            )
        }

        Image(imageVector = Additions2.DetailsSend,
            contentDescription = "delete",
            modifier = Modifier
                .height(24.dp)
                .width(24.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { onSendClick(text.value) })
    }
    // endregion

}

@Composable
@Preview
fun _message(){

    var text = remember {
        mutableStateOf("")
    }

    /*Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        MainScreenCommentBox(isFromMe = true, text = "Sample text", author = "Sample Author", time = "Sample Time")
        MainScreenCommentBox(isFromMe = false, text = "Sample text", author = "Sample Author", time = "Sample Time")
        MainScreenCommentBox(isFromMe = false, text = "Sample text", author = "Sample Author", time = "Sample Time")
        MainScreenCommentBox(isFromMe = true, text = "Sample text", author = "Sample Author", time = "Sample Time")
        MainScreenCommentBox(isFromMe = false, text = "Sample text", author = "Sample Author", time = "Sample Time")
        MessagesDownBorder(text = text, {

        })


    }*/
}

@Composable
@Preview
fun _mic() {

    val micIcon = painterResource(id = R.drawable.ic_outline_mic_none_24)

    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Описание",
            fontFamily = commonFont,
            color = colorResource(id = R.color.darker_gray),
            fontSize = 15.sp,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Left,
        )
        Icon(painter = micIcon, contentDescription = "")

    }

}