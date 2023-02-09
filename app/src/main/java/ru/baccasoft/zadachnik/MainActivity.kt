@file:Suppress("MoveLambdaOutsideParentheses", "LocalVariableName")
@file:OptIn(ExperimentalComposeUiApi::class)

package ru.baccasoft.zadachnik


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.joda.time.LocalDate
import ru.baccasoft.zadachnik.base.BaseViewModel
import ru.baccasoft.zadachnik.data.SessionDataStorage
import ru.baccasoft.zadachnik.data.network.AuthSubscriptionCallback
import ru.baccasoft.zadachnik.data.network.AuthSubscriptionManager
import ru.baccasoft.zadachnik.data.network.models.*
import ru.baccasoft.zadachnik.data.prefs.PrefsHelper
import ru.baccasoft.zadachnik.dialogs.ActionDialog
import ru.baccasoft.zadachnik.dialogs.ErrorAlert
import ru.baccasoft.zadachnik.features.mainScreen.*
import ru.baccasoft.zadachnik.features.navDrawer.NavDrawer
import ru.baccasoft.zadachnik.icons.ZadachnikIcons
import ru.baccasoft.zadachnik.icons.zadachnikicons.MenuArchive
import ru.baccasoft.zadachnik.navigation.Navigation
import ru.baccasoft.zadachnik.ui.theme.commonFont
import timber.log.Timber
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var session: SessionDataStorage

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        //todo надо как-то обработать
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {

            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val callback = object : AuthSubscriptionCallback {
        override fun expiredRefreshToken() {
            runOnUiThread {
                Timber.d("Authorize again, Expired Token")
            }
        }
    }

    lateinit var viewModel: MainActivityViewModel

    var speechOutput = mutableStateOf("")
    val nameTextFieldMutableState = mutableStateOf("")
    var descriptionTextFieldMutableState = mutableStateOf("")

    val task_id_key = "task_id"

    @SuppressLint("StateFlowValueCalledInComposition")
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseViewModel.context = this
        askNotificationPermission()

        setContent {
            Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {

                // region VARS/CONF
                viewModel = hiltViewModel()
                AuthSubscriptionManager.addSubscription(callback)
                // для тестов входа. вход работает исправно
                //val clean = session.cleanSession()
                Timber.d("Tokens: %s", "${session.getAccessToken()}\n${session.getRefreshToken()}")
                Timber.d("Hints: %s", "${session.getHints()}")
                Timber.d("User: %s", "${session.getLogin()}")
                val loggedIn = session.getLogin()
                val systemUiController = rememberSystemUiController()
                systemUiController.isSystemBarsVisible = true
                systemUiController.setSystemBarsColor(Color.White)

                // region MAIN_SCREEN_VARS
                val revealedTaskIds = viewModel.revealedCardIdsList.collectAsStateWithLifecycle()
                var hintsEnabled = remember {
                    mutableStateOf(true)
                }
                val snackState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()

                hintsEnabled.value = session.getHints() ?: true
                val colorRed = colorResource(id = R.color.colorPrimary).toArgb()

                val activeFilter = viewModel.activeFilter.collectAsStateWithLifecycle()
                val increase = viewModel.descending.collectAsStateWithLifecycle()
                val mineFilter = viewModel.mineFilter.collectAsStateWithLifecycle()
                val fromMeFilter = viewModel.fromMeFilter.collectAsStateWithLifecycle()
                val processingTask = viewModel.currentProcessedTask.collectAsStateWithLifecycle()

                val activeArchiveFilter =
                    viewModel.activeArchiveFilter.collectAsStateWithLifecycle()
                val increaseArchive = viewModel.descendingArchive.collectAsStateWithLifecycle()
                val mineArchiveFilter = viewModel.mineArchiveFilter.collectAsStateWithLifecycle()
                val fromMeArchiveFilter = viewModel.fromMeArchiveFilter.collectAsStateWithLifecycle()

                // endregion

                // region BOTTOM_SHEET
                val nameTextFieldState = remember {
                    nameTextFieldMutableState
                }
                val workerTextFieldState = remember {
                    mutableStateOf("")
                }
                val workerPhoneState = remember {
                    mutableStateOf("")
                }
                val result = remember {
                    mutableStateOf<Uri?>(null)
                }
                val selectedDate = remember {
                    mutableStateOf(SelectedDateModel(0, 0, 0))
                }
                val selectedTime = remember {
                    mutableStateOf(SelectedTimeModel(0, 0, 0))
                }
                val remindIn = remember {
                    mutableStateOf(0)
                }
                val documentsList = remember {
                    mutableStateOf(ArrayList<FilesListModel>())
                }
                val dateAndTimeSelector = remember {
                    mutableStateOf(0)
                }

                var commentTextState = remember {
                    mutableStateOf("")
                }

                val descriptionTextFieldValue = remember {
                    descriptionTextFieldMutableState
                }

                val commentsList = viewModel.getComments.collectAsStateWithLifecycle()

                val calendar = Calendar.getInstance()
                val hour = calendar[Calendar.HOUR_OF_DAY]
                val minute = calendar[Calendar.MINUTE]

                // для сравнения значений. Проверка измененности задачи.
                val _nameTextFieldState = remember {
                    mutableStateOf("")
                }
                val _descriptionTextFieldState = remember {
                    mutableStateOf("")
                }
                val _workerTextFieldState = remember {
                    mutableStateOf("")
                }
                val _selectedDate = remember {
                    mutableStateOf(SelectedDateModel(0, 0, 0))
                }
                val _selectedTime = remember {
                    mutableStateOf(SelectedTimeModel(0, 0, 0))
                }
                val _dateAndTimeSelector = remember {
                    mutableStateOf(0)
                }
                val _remindIn = remember {
                    mutableStateOf(0)
                }
                val isTaskCreated = remember {
                    mutableStateOf(false)
                }
                val isEdited = remember {
                    mutableStateOf(false)
                }
                val fastDateSelector = remember {
                    mutableStateOf(-1)
                }
                val _fastDateSelector = remember {
                    mutableStateOf(-1)
                }
                val commentTextFieldState = remember {
                    mutableStateOf("")
                }
                var rememberScrollStateDetails = rememberScrollState()
                var rememberNestedFilesScroll = rememberScrollState()

                LaunchedEffect(nameTextFieldState.value) {
                    isEdited.value = _nameTextFieldState.value != nameTextFieldState.value
                }

                LaunchedEffect(descriptionTextFieldValue.value) {
                    isEdited.value = _descriptionTextFieldState.value != descriptionTextFieldValue.value
                }

                LaunchedEffect(workerTextFieldState.value) {
                    isEdited.value = _workerTextFieldState.value != workerTextFieldState.value
                }

                LaunchedEffect(selectedDate.value) {
                    Log.d("LaunchedEffect(selectedDate.value)", "isEdited = ${isEdited.value}")
                    Log.d("LaunchedEffect(selectedDate.value)", "selectedDate = ${selectedDate.value}")
                    Log.d("LaunchedEffect(selectedDate.value)", "_selectedDate = ${_selectedDate.value}")
                    isEdited.value = _selectedDate.value != selectedDate.value
                    Log.d("LaunchedEffect(selectedDate.value)", "isEdited = ${isEdited.value}")
                }

                LaunchedEffect(selectedTime.value) {
                    isEdited.value = _selectedTime.value != selectedTime.value
                }

                LaunchedEffect(dateAndTimeSelector.value) {
                    isEdited.value = _dateAndTimeSelector.value != dateAndTimeSelector.value
                }

                LaunchedEffect(remindIn.value) {
                    isEdited.value = _remindIn.value != remindIn.value
                }

                var currentSelTask = remember{
                    mutableStateOf("")
                }


                // endregion


                val scope = rememberCoroutineScope()
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState(snackbarHostState = snackState)
                val mainScreenSearchTextFieldState = viewModel.mainScreenSearchTextFieldState.collectAsState()
                var popupControl by remember { mutableStateOf(false) }
                var updateComments = remember { mutableStateOf(false)}

                val toArchiveText = "Задача перенесена в архив"
                val fromArchiveText = "Задача удалена из архива"
                val archiveScreenSearchTextFieldState = viewModel.archiveScreenSearchTextFieldState.collectAsState()
                val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
                LaunchedEffect(Unit) {
                    delay(4000L)
                    Log.d("onPushLaunchedEffect","Started Effect")
                    scaffoldState.drawerState.close()

                    val extras = intent.extras
                    Log.d("onPushLaunchedEffect","${extras}")
                    val task_id = extras?.getString(task_id_key)
                    Log.d("onPushLaunchedEffect","Started Effect from key $task_id")
                    Timber.d("task_id %s", task_id)

                    task_id?.let {
                        viewModel.getTaskDetails(it, {
                            val _taskDetails = viewModel.getTaskModel.value
                            viewModel.getFilesList(it, {
                                val documentList = viewModel.getDocumentsList.value
                                Timber.d("MainActivity_DocumentList %s", "$documentList")
                                documentsList.value.addAll(documentList)
                            })
                            fillBottomSheetData(
                                details = _taskDetails,
                                documentList = documentsList.value,
                                documentsList = documentsList,
                                description = descriptionTextFieldValue,
                                name = nameTextFieldState,
                                worker = workerTextFieldState,
                                selectedDate = selectedDate,
                                selectedTime = selectedTime,
                                dateAndTimeSelector = dateAndTimeSelector,
                                remindIn = remindIn,
                                _name = _nameTextFieldState,
                                _description = _descriptionTextFieldState,
                                _worker = _workerTextFieldState,
                                _selectedDate = _selectedDate,
                                _selectedTime = _selectedTime,
                                _remindIn = _remindIn,
                                _dateAndTimeSelector = _dateAndTimeSelector,
                                isEdited = isEdited,
                            )
                            bottomState.animateTo(ModalBottomSheetValue.Expanded)
                        })
                    }
                }

                LaunchedEffect(fastDateSelector.value) {
                    isEdited.value = selectedDate.value != _selectedDate.value || selectedTime.value != _selectedTime.value

                    if(bottomState.isVisible) {
                        when(fastDateSelector.value) {
                            -1 -> {
                                val localDate = LocalDate().toString().split("T")[0]
                                val year = localDate.split("-")[0].toInt()
                                val month = localDate.split("-")[1].toInt()
                                val day = localDate.split("-")[2].toInt()
                                selectedDate.value = SelectedDateModel(year = year, month = month, day = day)
                                selectedTime.value = SelectedTimeModel(hour = 12, minute = 0, second = 0)
                            }
                            1 -> {
                                val localDate = LocalDate().toString().split("T")[0]
                                val year = localDate.split("-")[0].toInt()
                                val month = localDate.split("-")[1].toInt()
                                val day = localDate.split("-")[2].toInt()
                                selectedDate.value = SelectedDateModel(year = year, month = month, day = day)
                                selectedTime.value = SelectedTimeModel(hour = 12, minute = 0, second = 0)
                            }
                            2 -> {
                                val localDate = LocalDate().plusDays(1).toString().split("T")[0]
                                val year = localDate.split("-")[0].toInt()
                                val month = localDate.split("-")[1].toInt()
                                val day = localDate.split("-")[2].toInt()
                                selectedDate.value = SelectedDateModel(year = year, month = month, day = day)
                                selectedTime.value = SelectedTimeModel(hour = 12, minute = 0, second = 0)
                            }
                            3 -> {
                                val localDate = LocalDate().plusDays(7).toString().split("T")[0]
                                val year = localDate.split("-")[0].toInt()
                                val month = localDate.split("-")[1].toInt()
                                val day = localDate.split("-")[2].toInt()
                                selectedDate.value = SelectedDateModel(year = year, month = month, day = day)
                                selectedTime.value = SelectedTimeModel(hour = 12, minute = 0, second = 0)
                            }
                        }
                    }

                }

                // region NAVIGATION_CONFIG
                val navigationCurrentScreen = remember {
                    mutableStateOf("")
                }
                // endregion
                Timber.d("Main activity login check %s", loggedIn.toString())
                // endregion

                // region CONF/UPD
                val loading = remember {
                    mutableStateOf(false)
                }

                LaunchedEffect(bottomState.isVisible) {
                    if(!bottomState.isVisible) {
                        commentTextState.value = ""
                    }
                }

                LaunchedEffect(navigationCurrentScreen.value) {
                    if(navigationCurrentScreen.value != "main_screen" || navigationCurrentScreen.value != "archive_screen") {
                        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                    } else {
                        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
                    }
                }

                LaunchedEffect(bottomState.isVisible) {
                    if(bottomState.isVisible) {
                        Log.d("bottomState_is_visible","${bottomState.isVisible}")
                        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                    } else {
                        Log.d("bottomState_is_visible","${bottomState.isVisible}")
                        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
                    }
                }

                LaunchedEffect(navigationCurrentScreen.value) {
                    if(navigationCurrentScreen.value != "main_screen" || navigationCurrentScreen.value != "archive_screen") scaffoldState.drawerState.animateTo(DrawerValue.Closed, tween(0))
                }

                var isFromArchive = remember {
                    mutableStateOf(false)
                }

                var downloadingProgress = remember {
                    mutableStateOf(0.0f)
                }

                LaunchedEffect(navigationCurrentScreen.value){
                    isFromArchive.value = navigationCurrentScreen.value != "main_screen"
                }

                LaunchedEffect(activeFilter.value, block = {
                    viewModel.collapseItem()
                })

                LaunchedEffect(activeArchiveFilter.value, block = {
                    viewModel.collapseItem()
                })

                LaunchedEffect(increase.value, block = {
                    viewModel.collapseItem()
                })

                LaunchedEffect(increaseArchive.value, block = {
                    viewModel.collapseItem()
                })

                LaunchedEffect(mineFilter.value, block = {
                    viewModel.collapseItem()
                })

                LaunchedEffect(mineArchiveFilter.value, block = {
                    viewModel.collapseItem()
                })

                LaunchedEffect(fromMeFilter.value, block = {
                    viewModel.collapseItem()
                })

                LaunchedEffect(fromMeArchiveFilter.value, block = {
                    viewModel.collapseItem()
                })

                LaunchedEffect(archiveScreenSearchTextFieldState.value, block = {
                    viewModel.collapseItem()
                })

                LaunchedEffect(mainScreenSearchTextFieldState.value, block = {
                    viewModel.collapseItem()
                })

                // endregion

                Navigation(
                    isLoggedIn = loggedIn != "",
                    navController = navController,
                    currentDestination = navigationCurrentScreen
                )
                if (navigationCurrentScreen.value == "main_screen" || navigationCurrentScreen.value == "archive_screen") {
                    viewModel.setNavigationCurrentScreen(navigationCurrentScreen.value)
                    val tasksPagedList = viewModel.taskSource.collectAsLazyPagingItems()
                    val hint by BaseViewModel.hint.collectAsStateWithLifecycle()

                    LaunchedEffect(key1 = tasksPagedList.loadState.refresh, block = {
                        tasksPagedList.apply {
                            when {
                                loadState.append is LoadState.Loading -> {

                                }
                                loadState.append is LoadState.Error ||
                                        loadState.refresh is LoadState.Error -> {
                                    scope.launch {
                                        val e = loadState.refresh as LoadState.Error
                                        viewModel.showError(e.error)
                                    }
                                }
                            }
                        }
                    })
                    val focusManager = LocalFocusManager.current
                    LaunchedEffect(key1 = bottomState.currentValue, block = {
                        if (bottomState.currentValue == ModalBottomSheetValue.Hidden) {
                            focusManager.clearFocus()
                        }
                    })
                    ModalBottomSheetLayout(
                        sheetShape = RoundedCornerShape(
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp,
                            topStart = 12.dp,
                            topEnd = 12.dp
                        ),
                        sheetContent =
                        {
                            MainScreenOpenedTask(
                                nameTextFieldState = nameTextFieldState,
                                workerTextFieldState = workerTextFieldState,
                                workerPhoneState = workerPhoneState,
                                onCloseClick = {
                                    scope.launch{
                                        bottomState.hide()
                                    }
                                },
                                context = this@MainActivity,
                                result = result,
                                onTimeClick = {
                                    TimePickerDialog(
                                        this@MainActivity,
                                        R.style.TimePickerTheme,
                                        { _, hour: Int, minute: Int ->
                                            selectedTime.value = SelectedTimeModel(hour, minute, 0)
                                            Timber.d("MainActivityTimeSelector %s", "$hour:$minute")
                                        }, hour, minute, true
                                    ).show()
                                },
                                selectedDate = selectedDate,
                                selectedTime = selectedTime,
                                documentsList = documentsList,
                                onFileClick = {
                                    currentSelTask.value = it.filename + it.fileSize
                                    viewModel.downloadFile(this@MainActivity, it, downloadingProgress)
                                },
                                onCancelClick = {
                                    // TODO
                                },
                                comments = viewModel.getComments.collectAsStateWithLifecycle(),
                                updateComments = updateComments,
                                onCommentAdd = {
                                    val _taskDetails = viewModel.getTaskModel.value
                                    scope.launch {
                                        if(commentTextState.value != "") {
                                            updateComments.value = true
                                            loading.value = true
                                            if (viewModel.sendComment(it.text!!, _taskDetails.id)) {
                                                viewModel.addCommentForPreview(it)
                                                commentTextState.value = ""
                                                val _details = viewModel.getTaskModel.value
                                                viewModel.getTaskDetails(_details.id, {
                                                    val _taskDetails = viewModel.getTaskModel.value

                                                    viewModel.getFilesList(_taskDetails.id!!, {
                                                        val documentList = viewModel.getDocumentsList.value
                                                        Timber.d("MainActivity_DocumentList %s", "$documentList")
                                                        documentsList.value.addAll(documentList)
                                                    })

                                                    clearBottomSheetData(
                                                        documentsList = documentsList,
                                                        name = nameTextFieldState,
                                                        worker = workerTextFieldState,
                                                        description = descriptionTextFieldValue,
                                                        workerPhone = workerPhoneState,
                                                        selectedDate = selectedDate,
                                                        selectedTime = selectedTime,
                                                        fastDateSelector = fastDateSelector,
                                                        replyFor = remindIn,
                                                        comments = commentTextFieldState,
                                                    )

                                                    fillBottomSheetData(
                                                        details = _taskDetails,
                                                        documentList = documentsList.value,
                                                        documentsList = documentsList,
                                                        description = descriptionTextFieldValue,
                                                        name = nameTextFieldState,
                                                        worker = workerTextFieldState,
                                                        selectedDate = selectedDate,
                                                        selectedTime = selectedTime,
                                                        dateAndTimeSelector = dateAndTimeSelector,
                                                        remindIn = remindIn,
                                                        _name = _nameTextFieldState,
                                                        _description = _descriptionTextFieldState,
                                                        _worker = _workerTextFieldState,
                                                        _selectedDate = _selectedDate,
                                                        _selectedTime = _selectedTime,
                                                        _remindIn = _remindIn,
                                                        _dateAndTimeSelector = _dateAndTimeSelector,
                                                        isEdited = isEdited,
                                                    )
                                                })
                                            }
                                            updateComments.value = false
                                            loading.value = false
                                        }
                                    }
                                },
                                phone = session.getPhoneNumber() ?: "",
                                text = commentTextState,
                                onProceedButtonClick = {
                                    scope.launch {
                                        loading.value = true
                                        val prefs = PrefsHelper(applicationContext)
                                        if (isTaskCreated.value) {
                                            viewModel.createTask(
                                                CreateTaskModel(
                                                    title = nameTextFieldState.value.replace(
                                                        "  ",
                                                        ""
                                                    ).replace("   ", ""),
                                                    description = descriptionTextFieldValue.value.replace(
                                                        "  ",
                                                        ""
                                                    ).replace("   ", ""),
                                                    comment = commentTextFieldState.value,
                                                    trackerId = "",
                                                    creatorId = session.getLogin() ?: "",
                                                    responsiblePhone =
                                                        if (workerPhoneState.value.isBlank()) prefs.getPhoneNumber()!!
                                                        else if(workerPhoneState.value.length == 11 && workerPhoneState.value[0] == "8".toCharArray()[0]) "7${workerPhoneState.value.takeLast(10)}"
                                                        else workerPhoneState.value,
                                                    responsibleFullName = workerTextFieldState.value,
                                                    deadline = Utils().generateDate(selectedDate.value, selectedTime.value, remindIn.value, false),
                                                    replyFor = if(remindIn.value == -1) null else Utils().generateDate(selectedDate.value, selectedTime.value, remindIn.value, true),
                                                    inArchiveForCreator = null,
                                                    inArchiveForResponsible = null,
                                                ),
                                                tasksPagedList = tasksPagedList
                                            )
                                        } else {
                                            Log.d("UpdateTask","responsiblePhone=${workerPhoneState.value}")
                                            Log.d("UpdateTask","responsibleName=${workerTextFieldState.value}")
                                            val openedTask = viewModel.getTaskModel.value
                                            viewModel.updateTask(
                                                UpdateTaskModel(
                                                    title = nameTextFieldState.value,
                                                    description = descriptionTextFieldValue.value,
                                                    comment = "",
                                                    trackerId = "",
                                                    creatorId = session.getLogin() ?: "",
                                                    responsibleId = if(!workerPhoneState.value.isBlank()) null else openedTask.responsibleId,
                                                    responsiblePhone = if (workerPhoneState.value.isBlank()) null
                                                    else if(workerPhoneState.value.length == 11 && workerPhoneState.value[0] == "8".toCharArray()[0]) "+7${workerPhoneState.value.takeLast(10)}"
                                                    else workerPhoneState.value,
                                                    responsibleFullName = workerTextFieldState.value,
                                                    deadline = Utils().generateDate(selectedDate.value, selectedTime.value, remindIn.value, false),
                                                    replyFor = if(remindIn.value == -1) null else Utils().generateDate(selectedDate.value, selectedTime.value, remindIn.value, true),
                                                    inArchiveForCreator = null,
                                                    inArchiveForResponsible = null,
                                                ),
                                                viewModel.getTaskModel.value.id,
                                                tasksPagedList = tasksPagedList
                                            )
                                        }
                                        loading.value = false
                                        bottomState.hide()
                                    }
                                },
                                descriptionTextFieldState = descriptionTextFieldValue,
                                onTitleSpeechRecognition = {
                                    getSpeechInputForTitle(this@MainActivity)
                                },
                                onDescriptionSpeechRecognition = {
                                    getSpeechInputForDescription(this@MainActivity)
                                },
                                remindIn = remindIn,
                                isEdited = isEdited,
                                fastDateSelector = fastDateSelector,
                                commentTextFieldState = commentTextFieldState,
                                isCreated = isTaskCreated.value,
                                isTaskCreated = isTaskCreated,
                                currentUserId = loggedIn!!,
                                getTaskModel = viewModel.getTaskModel.value,
                                isFromArchive = isFromArchive,
                                rememberScrollStateDetails = rememberScrollStateDetails,
                                nestedFilesHorizontalScroll = rememberNestedFilesScroll,
                                onContactsError = {
                                    scope.launch {
                                        viewModel.showError("Разрешите приложению доступ к контактам в настройках")
                                    }
                                },
                                downloadingProgress = downloadingProgress,
                                currentSelTask = currentSelTask
                            )
                        }, sheetState = bottomState
                    ) {
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            drawerContent = {
                                NavDrawer(
                                    navController = navController,
                                    switchInitialValue = hintsEnabled,
                                    currentScreen = navigationCurrentScreen,
                                    onCreateTaskClick = {

                                        isTaskCreated.value = true
                                        clearBottomSheetData(
                                            documentsList = documentsList,
                                            name = nameTextFieldState,
                                            worker = workerTextFieldState,
                                            description = descriptionTextFieldValue,
                                            workerPhone = workerPhoneState,
                                            selectedDate = selectedDate,
                                            selectedTime = selectedTime,
                                            fastDateSelector = fastDateSelector,
                                            replyFor = remindIn,
                                            comments = commentTextFieldState,
                                        )
                                        viewModel.clearComments()
                                        scope.launch {
                                            bottomState.animateTo(ModalBottomSheetValue.Expanded)
                                            scaffoldState.drawerState.close()
                                        }
                                    },
                                    onHintsClicked = { b ->
                                        hintsEnabled.value = b
                                        session.setHints(hintsEnabled.value)
                                    },
                                    onLogOutClicked = {
                                        scope.launch {
                                            scaffoldState.drawerState.close()
                                            viewModel.showAction(
                                                text = "Выйти из учетной записи?",
                                                onActionText = "Выйти",
                                                onAction = {
                                                    Timber.tag("AlertDialog").d("OnLogout")
                                                    session.cleanSession()
                                                    viewModel.closeLogOutDialog()
                                                    navController.navigate("login_screen") {
                                                        popUpTo(navigationCurrentScreen.value) {
                                                            inclusive = true
                                                        }
                                                    }
                                                },
                                                onClose = {
                                                    Timber.tag("AlertDialog").d("OnDismiss")
                                                    viewModel.closeLogOutDialog()
                                                },
                                            )
                                        }
                                    },
                                    onDeleteAccountClicked = {
                                        session.cleanSession()
                                        navController.navigate("login_screen") {
                                            popUpTo(navigationCurrentScreen.value) {
                                                inclusive = true
                                            }
                                        }
                                    },
                                    onClick = {
                                        viewModel.setNavigationCurrentScreen(it)
                                        tasksPagedList.refresh()
                                        scope.launch {
                                            scaffoldState.drawerState.close()
                                        }
                                    },
                                    onCloseClick = {
                                        scope.launch {
                                            scaffoldState.drawerState.close()
                                        }
                                    }
                                )
                            },
                            scaffoldState = scaffoldState,
                            topBar = {
                                MainScreenAppBar(
                                    searchTextFieldState = if (navController.currentDestination?.route == "archive_screen") archiveScreenSearchTextFieldState else mainScreenSearchTextFieldState,
                                    onClick = {
                                        scope.launch {
                                            scaffoldState.drawerState.open()
                                        }
                                    },
                                    placeholder = if (navController.currentDestination?.route == "archive_screen") "Поиск по архиву" else "Поиск по задачам",
                                    onTextChange = {
                                        if (navController.currentDestination?.route == "archive_screen")
                                            viewModel.setArchiveScreenSearchTextFieldState(it.replace("\n", " "))
                                        else
                                            viewModel.setMainScreenSearchTextFieldState(it.replace("\n", " "))
                                        if (it.length > 2 || it.isEmpty())
                                            tasksPagedList.refresh()
                                    },
                                    integration = {
                                        // region FILTER
                                        if(navigationCurrentScreen.value == "main_screen") {

                                            MainScreenFilter(
                                                activeFilter = activeFilter,
                                                increase = increase,
                                                tasksPagedList = tasksPagedList,
                                                mine = mineFilter,
                                                fromMe = fromMeFilter,
                                                onFilterChanged = { i, b, m, f ->
                                                    viewModel.onFilterChanged(
                                                        currentActive = i,
                                                        descending = b,
                                                        forArchive = false,
                                                        mine = m,
                                                        fromMe = f
                                                    )
                                                },
                                            )

                                        } else if(navigationCurrentScreen.value == "archive_screen") {
                                            MainScreenFilter(
                                                activeFilter = activeArchiveFilter,
                                                increase = increaseArchive,
                                                tasksPagedList = tasksPagedList,
                                                mine = mineArchiveFilter,
                                                fromMe = fromMeArchiveFilter,
                                                onFilterChanged = { i, b, m, f ->
                                                    viewModel.onFilterChanged(
                                                        currentActive = i,
                                                        descending = b,
                                                        forArchive = true,
                                                        mine = m,
                                                        fromMe = f
                                                    )
                                                },
                                            )
                                        }
                                        // endregion
                                    }
                                )
                            },
                            drawerGesturesEnabled = true,
                            snackbarHost = {
                                SnackbarHost(hostState = it, ) { data ->
                                    Timber.d("MainActivity %s", data.message)
                                    showSnackBar(text = data.message, onClick = {
                                        scaffoldState.snackbarHostState
                                            .currentSnackbarData?.dismiss()
                                        coroutineScope.launch {
                                            val isCreatorTask = processingTask.value[0].creatorId == loggedIn
                                            val isResponsibleTask = processingTask.value[0].responsibleId == loggedIn
                                            loading.value = true
                                            if (navController.currentDestination?.route == "main_screen") {
                                                viewModel.toArchive(processingTask.value[0], false, isCreatorTask, isResponsibleTask)
                                            } else {
                                                viewModel.toArchive(processingTask.value[0], true, isCreatorTask, isResponsibleTask)
                                            }
                                            loading.value = false
                                            tasksPagedList.refresh()
                                        }
                                    })
                                }
                            },
                            content = { padding ->
                                val refreshing = remember {
                                    mutableStateOf(false)
                                }

                                val pullRefreshState = rememberPullRefreshState(refreshing.value, {
                                    scope.launch {
                                        refreshing.value = true
                                        tasksPagedList.refresh()
                                        delay(100)
                                        refreshing.value = false
                                    }
                                })
                                Box(
                                    modifier = Modifier
                                        .padding(padding)
                                ) {
                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){
                                        Column(
                                            Modifier
                                                .fillMaxSize(),
                                            verticalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(
                                                verticalArrangement = Arrangement.spacedBy(0.dp),
                                                modifier = Modifier
                                                    .padding(
                                                        bottom = if (hintsEnabled.value && navigationCurrentScreen.value != "archive_screen") 60.dp else 0.dp
                                                    )
                                            ) {
                                                // region PLACEHOLDER_NO_TASKS
                                                if(tasksPagedList.itemSnapshotList.isEmpty()){
                                                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()){
                                                        Text(
                                                            text = "Отсутствуют задачи",
                                                            fontFamily = commonFont,
                                                            fontSize = 18.sp,
                                                            modifier = Modifier.fillMaxWidth(),
                                                            textAlign = TextAlign.Center,
                                                            color = colorResource(id = R.color.grey),
                                                        )
                                                    }
                                                }
                                                // endregion
                                                tasksPagedList.apply {
                                                    when {
                                                        loadState.refresh is LoadState.Loading
                                                                || loadState.append is LoadState.Loading-> {
                                                            loading.value = true
                                                        }
                                                        loadState.append is LoadState.Error
                                                                || loadState.append is LoadState.NotLoading -> {
                                                            loading.value = false
                                                        }
                                                    }
                                                }
                                                // region LAZY_COLUMN
                                                MainScreenTaskList(
                                                    tasksPagedList = tasksPagedList,
                                                    unarchive = navController.currentDestination?.route != "main_screen",
                                                    onArchiveClick = { t ->
                                                        viewModel.clearProcessedTask()
                                                        viewModel.addProcessedTask(t)
                                                        coroutineScope.launch {
                                                            val isCreatorTask = processingTask.value[0].creatorId == loggedIn
                                                            val isResponsibleTask = processingTask.value[0].responsibleId == loggedIn
                                                            loading.value = true
                                                            if (navController.currentDestination?.route == "main_screen") {
                                                                viewModel.toArchive(t, true, isCreatorTask, isResponsibleTask)
                                                            } else {
                                                                viewModel.toArchive(t, false, isCreatorTask, isResponsibleTask)
                                                            }
                                                            loading.value = true
                                                            tasksPagedList.refresh()
                                                        }
                                                        coroutineScope.launch {
                                                            scaffoldState.snackbarHostState.showSnackbar(
                                                                if (navController.currentDestination?.route == "main_screen")
                                                                    toArchiveText
                                                                else fromArchiveText
                                                            )
                                                        }
                                                    },
                                                    onCardClick = {
                                                        loading.value = true
                                                        isTaskCreated.value = false
                                                        scope.launch {
                                                            rememberScrollStateDetails.scrollTo(0)
                                                            rememberNestedFilesScroll.scrollTo(0)
                                                            viewModel.getTaskDetails(it.id!!, {
                                                                val _taskDetails = viewModel.getTaskModel.value

                                                                viewModel.getFilesList(it.id!!, {
                                                                    val documentList = viewModel.getDocumentsList.value
                                                                    Timber.d("MainActivity_DocumentList %s", "$documentList")
                                                                    documentsList.value.addAll(documentList)
                                                                })

                                                                clearBottomSheetData(
                                                                    documentsList = documentsList,
                                                                    name = nameTextFieldState,
                                                                    worker = workerTextFieldState,
                                                                    description = descriptionTextFieldValue,
                                                                    workerPhone = workerPhoneState,
                                                                    selectedDate = selectedDate,
                                                                    selectedTime = selectedTime,
                                                                    fastDateSelector = fastDateSelector,
                                                                    replyFor = remindIn,
                                                                    comments = commentTextFieldState,
                                                                )
                                                                fillBottomSheetData(
                                                                    details = _taskDetails,
                                                                    documentList = documentsList.value,
                                                                    documentsList = documentsList,
                                                                    description = descriptionTextFieldValue,
                                                                    name = nameTextFieldState,
                                                                    worker = workerTextFieldState,
                                                                    selectedDate = selectedDate,
                                                                    selectedTime = selectedTime,
                                                                    dateAndTimeSelector = dateAndTimeSelector,
                                                                    remindIn = remindIn,
                                                                    _name = _nameTextFieldState,
                                                                    _description = _descriptionTextFieldState,
                                                                    _worker = _workerTextFieldState,
                                                                    _selectedDate = _selectedDate,
                                                                    _selectedTime = _selectedTime,
                                                                    _remindIn = _remindIn,
                                                                    _dateAndTimeSelector = _dateAndTimeSelector,
                                                                    isEdited = isEdited,
                                                                )
                                                                bottomState.animateTo(ModalBottomSheetValue.Expanded)
                                                            })
                                                            loading.value = false
                                                        }
                                                    },
                                                    revealedTaskIds = revealedTaskIds,
                                                    onExpand = { s ->
                                                        viewModel.onItemExpanded(s)
                                                    },
                                                    onCollapse = { s ->
                                                        viewModel.onItemCollapsed(s)
                                                    },
                                                    pullRefreshState = pullRefreshState,
                                                    refreshing = refreshing,
                                                )
                                                // endregion
                                            }
                                        }
                                        if (navController.currentDestination?.route == "main_screen") {
                                            Row(
                                                Modifier
                                                    .fillMaxWidth()
                                                    .height(140.dp), horizontalArrangement = Arrangement.End){
                                                MainScreenFABContent(onClick = {
                                                    isTaskCreated.value = true
                                                    clearBottomSheetData(
                                                        documentsList = documentsList,
                                                        name = nameTextFieldState,
                                                        worker = workerTextFieldState,
                                                        description = descriptionTextFieldValue,
                                                        workerPhone = workerPhoneState,
                                                        selectedDate = selectedDate,
                                                        selectedTime = selectedTime,
                                                        fastDateSelector = fastDateSelector,
                                                        replyFor = remindIn,
                                                        comments = commentTextFieldState,
                                                    )
                                                    viewModel.clearComments()
                                                    scope.launch {
                                                        bottomState.animateTo(ModalBottomSheetValue.Expanded)
                                                    }
                                                })
                                            }
                                        }

                                        if (hintsEnabled.value && navController.currentDestination?.route == "main_screen") {
                                            MainScreenHints(
                                                hint = hint,
                                            )
                                        }
                                        // region PULL_REFRESH_INDICATOR
                                        PullRefreshIndicator(refreshing.value, pullRefreshState,
                                            Modifier
                                                .align(Alignment.TopCenter)
                                                .zIndex(1f))
                                        // endregion
                                    }
                                }
                            }
                        )
                    }
                }

                val isLoading = remember {
                    mutableStateOf(false)
                }
                val isTimerLoading = remember {
                    mutableStateOf(false)
                }
                LaunchedEffect(key1 = loading.value, block = {
                    if (loading.value) {
                        if (!isTimerLoading.value) {
                            scope.launch {
                                isLoading.value = true
                                isTimerLoading.value = true
                                delay(500)
                                isTimerLoading.value = false
                                if (!loading.value)
                                    isLoading.value = false
                            }
                        }
                    } else {
                        if (!isTimerLoading.value) {
                            isLoading.value = false
                        }
                    }
                })

                if (isLoading.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Black.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(colorRed))
                    }
                }

                val showError = BaseViewModel.showError.collectAsState(null)
                showError.value?.let {
                    ErrorAlert(text = it.first) {
                        viewModel.closeAlertError()
                        BaseViewModel.isErrorLodOut.value?.let {
                            if (it) {
                                session.cleanSession()
                                navController.navigate("login_screen") {
                                    popUpTo(navigationCurrentScreen.value) {
                                        inclusive = true
                                    }
                                }
                                viewModel.clearIs403ErrorLodOut()
                            }
                        }
                    }
                }

                val showAction = BaseViewModel.showAction.collectAsState(null)
                showAction.value?.let {
                    ActionDialog(
                        onAction = it.first.first,
                        onClose = it.first.second,
                        text = it.second.first,
                        onActionText = it.second.second
                    )
                }

            }
        }
    }

    override fun onDestroy() {
        AuthSubscriptionManager.removeSubscription(callback)
        super.onDestroy()
    }

    private fun fillBottomSheetData(
        details: GetTaskModel,
        documentList: List<FilesListModel>,
        documentsList: MutableState<ArrayList<FilesListModel>>,
        description: MutableState<String>,
        name: MutableState<String>,
        worker: MutableState<String>,
        selectedDate: MutableState<SelectedDateModel>,
        selectedTime: MutableState<SelectedTimeModel>,
        remindIn: MutableState<Int>,
        dateAndTimeSelector: MutableState<Int>,
        _name: MutableState<String>,
        _description: MutableState<String>,
        _worker: MutableState<String>,
        _selectedDate: MutableState<SelectedDateModel>,
        _selectedTime: MutableState<SelectedTimeModel>,
        _remindIn: MutableState<Int>,
        _dateAndTimeSelector: MutableState<Int>,
        isEdited: MutableState<Boolean>
    ) {

        name.value = details.title
        _name.value = name.value

        description.value = details.description
        _description.value = _description.value

        worker.value = details.responsibleFullName
        _worker.value = worker.value

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val deadlineDate = sdf.parse(details.deadline).time + TimeZone.getDefault().rawOffset
        val deadlineLocale = sdf.format(deadlineDate)
        val out = Utils().processDate(deadlineLocale)
        Timber.d("fillBottomSheetData %s", documentList)
        selectedDate.value = SelectedDateModel(out[2].toInt(), out[3].toInt(), out[4].toInt())
        selectedTime.value = SelectedTimeModel(out[5].toInt(), out[6].toInt(), out[7].toInt())
        _selectedDate.value = selectedDate.value
        _selectedTime.value = selectedTime.value
        Timber.d("fillBottomSheetList %s", selectedDate.value.toString())
        Timber.d("fillBottomSheetList %s", selectedTime.value.toString())



        if (details.replyFor != null) {
            val dif = sdf.parse(details.replyFor)!!.time - sdf.parse(details.deadline)!!.time
            val seconds = dif / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            when {
                abs(minutes) == 15.toLong() -> {
                    remindIn.value = 0
                }
                abs(hours) == 1.toLong() -> {
                    remindIn.value = 1
                }
                abs(hours) == 4.toLong() -> {
                    remindIn.value = 2
                }
                abs(days) == 1.toLong() -> {
                    remindIn.value = 3
                }
            }
        } else {
            remindIn.value = 4
        }
        _remindIn.value = remindIn.value


        documentsList.value.clear()
        documentsList.value.addAll(documentList)
        Timber.tag("fillBottomSheetDataDocumentsListSize").d("${documentsList.value.size}")
        Timber.d("fillBottomSheetData %s", documentsList.value)

        isEdited.value = false

    }

    fun clearBottomSheetData(
        documentsList: MutableState<ArrayList<FilesListModel>>,
        name: MutableState<String>,
        description: MutableState<String>,
        worker: MutableState<String>,
        workerPhone: MutableState<String>,
        selectedDate: MutableState<SelectedDateModel>,
        selectedTime: MutableState<SelectedTimeModel>,
        fastDateSelector: MutableState<Int>,
        replyFor: MutableState<Int>,
        comments: MutableState<String>,
    ) {
        documentsList.value.clear()
        name.value = ""
        worker.value = ""
        workerPhone.value = ""
        description.value = ""
        val time = java.util.Calendar.getInstance().time
        val formatter = SimpleDateFormat("HH:mm")
        val current = formatter.format(time)
        val dayFormat = SimpleDateFormat("dd")
        val monthFormat = SimpleDateFormat("M")
        val yearFormat = SimpleDateFormat("yyyy")
        selectedDate.value = SelectedDateModel(yearFormat.format(Date()).toInt(), monthFormat.format(Date()).toInt(), dayFormat.format(Date()).toInt())
        selectedTime.value = SelectedTimeModel(hour = 12, minute = 0, second = 0)
        fastDateSelector.value = -1
        replyFor.value = 4
        comments.value = ""
    }

    @Composable
    fun showSnackBar(
        text: String,
        onClick: () -> Unit
    ) {

        val interactionSource = remember { MutableInteractionSource() }

        Snackbar(
            backgroundColor = colorResource(id = R.color.lighter_grey),
            shape = RoundedCornerShape(8.dp),
            contentColor = colorResource(id = R.color.black),
            elevation = 0.dp,
            modifier = Modifier
                .height(56.dp),
        ){
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(color = colorResource(id = R.color.lighter_grey))
                ,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(imageVector = ZadachnikIcons.MenuArchive, contentDescription = "")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = text, fontFamily = commonFont)
                }
                Text(text = "Отменить", color = colorResource(id = R.color.colorPrimary), fontFamily = commonFont, modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null
                ){
                    onClick()
                })
            }
        }
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

    // вот это мега-костыль галактического масштаба. инту потом не получить, поэтому так

    private fun getSpeechInputForTitle(context: Context){

        if(!SpeechRecognizer.isRecognitionAvailable(context)) {
            Toast.makeText(context, "Распознавание речи недоступно", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Говорите")
            startActivityForResult(intent, 101)
        }

    }

    private fun getSpeechInputForDescription(context: Context){

        if(!SpeechRecognizer.isRecognitionAvailable(context)) {
            Toast.makeText(context, "Распознавание речи недоступно", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Говорите")
            startActivityForResult(intent, 102)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            nameTextFieldMutableState.value = nameTextFieldMutableState.value + result?.get(0).toString()
            // debug only
            speechOutput.value = result?.get(0).toString()
        }
        if (requestCode == 102 && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            descriptionTextFieldMutableState.value = descriptionTextFieldMutableState.value + result?.get(0).toString()
            // debug only
            speechOutput.value = result?.get(0).toString()
        }
    }

    companion object {
        fun newPushIntent(context: Context, payload: Map<String, String>) : Intent {
            return newPushIntent(context, Bundle().apply {
                payload.entries.forEach{
                    putString(it.key, it.value)
                }
            })
        }

        fun newPushIntent(appContext: Context, bundle: Bundle): Intent {
            return Intent(appContext, MainActivity::class.java).apply {
                putExtras(bundle)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

}

class WindowCenterOffsetPositionProvider(
    private val x: Int = 0,
    private val y: Int = 0
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        return IntOffset(
            (windowSize.width - popupContentSize.width) / 2 + x,
            (windowSize.height - popupContentSize.height) / 2 + y
        )
    }
}
