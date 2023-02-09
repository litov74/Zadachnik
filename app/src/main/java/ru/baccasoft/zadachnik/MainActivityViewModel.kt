package ru.baccasoft.zadachnik

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.core.content.FileProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.LazyPagingItems
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import ru.baccasoft.zadachnik.data.network.RetrofitApi
import ru.baccasoft.zadachnik.data.network.models.*
import ru.baccasoft.zadachnik.data.prefs.PrefsHelper
import ru.baccasoft.zadachnik.base.BaseViewModel
import ru.baccasoft.zadachnik.features.mainScreen.TaskModel
import ru.baccasoft.zadachnik.features.mainScreen.TaskSource
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val apiService: RetrofitApi,
    val application: Application,
    @ApplicationContext private val context: Context
) : BaseViewModel() {


    private val _cards = MutableStateFlow(listOf<TaskModel>())

    private val _revealedCardIdsList = MutableStateFlow(listOf<String>())
    val revealedCardIdsList: StateFlow<List<String>> get() = _revealedCardIdsList

    private val session = PrefsHelper(application)

    private val _activeFilter = MutableStateFlow(2)
    val activeFilter: StateFlow<Int> get() = _activeFilter

    private val _mineFilter = MutableStateFlow(false)
    val mineFilter: StateFlow<Boolean> get() = _mineFilter

    private val _fromMeFilter = MutableStateFlow(false)
    val fromMeFilter: StateFlow<Boolean> get() = _fromMeFilter

    private val _descending = MutableStateFlow(false)
    val descending: StateFlow<Boolean> get() = _descending

    private val _activeArchiveFilter = MutableStateFlow(2)
    val activeArchiveFilter: StateFlow<Int> get() = _activeArchiveFilter

    private val _descendingArchive = MutableStateFlow(false)
    val descendingArchive: StateFlow<Boolean> get() = _descendingArchive

    private val _currentProcessedTask = MutableStateFlow(listOf<TaskModel>())
    val currentProcessedTask: StateFlow<List<TaskModel>> get() = _currentProcessedTask

    private val _mineArchiveFilter = MutableStateFlow(false)
    val mineArchiveFilter: StateFlow<Boolean> get() = _mineArchiveFilter

    private val _fromMeArchiveFilter = MutableStateFlow(false)
    val fromMeArchiveFilter: StateFlow<Boolean> get() = _fromMeArchiveFilter

    private val _getTaskModel = MutableStateFlow(GetTaskModel("","","","","","","","","","","","",false, false, ArrayList<DataModel>()))
    val getTaskModel: StateFlow<GetTaskModel> get() = _getTaskModel

    private val _getComments = MutableStateFlow(listOf<DataModel>())
    val getComments: MutableStateFlow<List<DataModel>> get() = _getComments

    private val _isDataReady = MutableStateFlow(false)
    val isDataReady: StateFlow<Boolean> get() = _isDataReady

    private val _getDocumentsList = MutableStateFlow(listOf<FilesListModel>())
    val getDocumentsList: StateFlow<List<FilesListModel>> get() = _getDocumentsList

    private val _needToLogOut = MutableStateFlow(false)
    val needToLogOut: StateFlow<Boolean> get() = _needToLogOut

    fun clearComments() {
        _getComments.value = ArrayList()
    }

    private val _navigationCurrentScreen = MutableStateFlow("")
    val navigationCurrentScreen = _navigationCurrentScreen.asStateFlow()

    fun setNavigationCurrentScreen(navigationCurrentScreen: String) {
        _navigationCurrentScreen.value = navigationCurrentScreen
    }

    private val _mainScreenSearchTextFieldState = MutableStateFlow("")
    val mainScreenSearchTextFieldState = _mainScreenSearchTextFieldState.asStateFlow()

    fun setMainScreenSearchTextFieldState(str: String) {
        _mainScreenSearchTextFieldState.value = str
    }

    private val _archiveScreenSearchTextFieldState = MutableStateFlow("")
    val archiveScreenSearchTextFieldState = _archiveScreenSearchTextFieldState.asStateFlow()

    fun setArchiveScreenSearchTextFieldState(str: String) {
        _archiveScreenSearchTextFieldState.value = str
    }

    fun closeAlertError() {
        viewModelScope.launch {
            _showError.emit(null)
        }
    }

    fun closeLogOutDialog() {
        viewModelScope.launch {
            _showAction.emit(null)
        }
    }


    val taskSource = Pager(PagingConfig(
        pageSize = 20,
        enablePlaceholders = false,
        initialLoadSize = 20,
        maxSize = 200,
        prefetchDistance = 5,
    )) {

        TaskSource(
            apiService = apiService,
            navigationCurrentScreen = navigationCurrentScreen,
            activeFilter = activeFilter,
            activeArchiveFilter = activeArchiveFilter,
            descending = descending,
            descendingArchive = descendingArchive,
            mineFilter = mineFilter,
            fromMeFilter = fromMeFilter,
            mineArchiveFilter = mineArchiveFilter,
            fromMeArchiveFilter = fromMeArchiveFilter,
            mainScreenSearchTextFieldState = mainScreenSearchTextFieldState,
            archiveScreenSearchTextFieldState = archiveScreenSearchTextFieldState,
        )
    }.flow

    fun downloadFile(activity: Activity, filesListModel: FilesListModel, progress: MutableState<Float>) {
        progress.value = 0.1f
        viewModelScope.launch {
            progress.value = 0.2f
            val hashFile = filesListModel.url?.split("/")?.last()
            progress.value = 0.3f
            progress.value = 0.4f
            progress.value = 0.5f
            progress.value = 0.6f
            progress.value = 0.7f
            progress.value = 0.8f
            progress.value = 0.9f
            apiService.getFile(hashFile).onSuccess {
                progress.value = 0.9f
                saveFile(activity, it, filesListModel, progress)
            }.onFailure {
                showError(it)
            }
        }
    }

    fun saveFile(activity: Activity, body: ResponseBody?, filesListModel: FilesListModel, progress: MutableState<Float>) {
        val file = Utils().generateFile(context, "tasks/" + filesListModel.taskId, "" + filesListModel.filename)
        if (body==null || file ==null)
            return
        var input: InputStream? = null
        try {
            input = body.byteStream()
            //val file = File(getCacheDir(), "cacheFileAppeal.srl")
            val fos = FileOutputStream(file.path)
            progress.value = 0.0f
            fos.use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                    progress.value = 1.0f
                }
                output.flush()
            }
            progress.value = 0.0f
            openFile(activity, file, filesListModel.contentType)
        }catch (e:Exception){
            Timber.e(e)
        }
        finally {
            input?.close()
        }
    }

    fun openFile(activity: Activity, file: File, type: String?) {
        val contentUri: Uri = FileProvider.getUriForFile(context, "ru.baccasoft.zadachnik", file)
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_VIEW
            setDataAndType(contentUri, type)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        activity.startActivity(Intent.createChooser(shareIntent, null))
    }

    suspend fun getTaskDetails(id: String, onComplete: suspend () -> Unit) {
        setDataReady(false)
        apiService.getTaskDetails(id)
            .onSuccess {
                Timber.d("MainActivityViewModel %s","$it")
                _getTaskModel.emit(it)
                _getComments.value = if(it.taskComments != null) it.taskComments!!.reversed()
                else listOf()
                setDataReady(true)
                onComplete()
            }
            .onFailure {
                showError(it)
            }
    }

    suspend fun sendComment(text: String, taskId: String):Boolean {
        try {
            apiService.sendComment(
                AddCommentModel(text = text, taskId = taskId)
            )
        } catch (e : Exception) {
            showError(e)
            return false
        }
        return true
    }

    fun getFilesList(id: String, onComplete: () -> Unit) {
        setDataReady(false)
        viewModelScope.launch {
            apiService.getFilesList(id)
                .onSuccess {
                    _getDocumentsList.value = _getDocumentsList.value.toMutableList().also { list ->
                        list.clear()
                        list.addAll(it)
                    }
                    setDataReady(true)
                    onComplete()
                }
                .onFailure {
                    showError(it)
                }
        }
    }

    suspend fun createTask(task: CreateTaskModel, tasksPagedList: LazyPagingItems<TaskModel>){
        runCatching {
            apiService.createTask(task)
        }.onFailure {
            showError(it)
        }.onSuccess {
            Timber.d("MainActivityViewModel %s","${it.getOrNull()}")
            val _task = it.getOrNull()
            if(_task != null){
                _getTaskModel.emit(_task)
                _getComments.value = if(_task.taskComments != null) _task.taskComments!!.reversed()
                else listOf()
                setDataReady(true)
                tasksPagedList.refresh()
            }

        }
    }

    suspend fun updateTask(task: UpdateTaskModel, taskId: String, tasksPagedList: LazyPagingItems<TaskModel>){
        runCatching {
            apiService.updateTask(taskId, task)
        }.onFailure {
            showError(it)
        }.onSuccess {
            Timber.d("MainActivityViewModel %s","${it.getOrNull()}")
            val _task = it.getOrNull()
            if(_task != null){
                _getTaskModel.emit(_task)
                _getComments.value = if(_task.taskComments != null) _task.taskComments!!.reversed()
                else listOf()
                setDataReady(true)
                tasksPagedList.refresh()
            }
        }
    }

    private suspend fun onArchiveClicked(cardId: String, toArchive: Boolean, isCreatorTask: Boolean, isResponsibleTask: Boolean): Boolean {
        var res = true

        apiService.sendTaskToArchive(
            cardId,
            TaskToArchiveModel(
                inArchiveForCreator = if (isCreatorTask) toArchive else null,
                inArchiveForResponsible = if (isResponsibleTask) toArchive else null
            ))
            .onFailure {
                res = false
                showError(it)
            }
        return res
    }

    fun deleteAccount() {
        // TODO!
    }

    fun setDataReady(isReady: Boolean){
        _isDataReady.value = isReady
    }

    // region UI
    fun onItemExpanded(cardId: String) {
        if (_revealedCardIdsList.value.contains(cardId)) return
        _revealedCardIdsList.value = _revealedCardIdsList.value.toMutableList()
            .also { list ->
                list.clear()
                list.add(cardId)
            }
    }

    fun onItemCollapsed(cardId: String) {
        if (!_revealedCardIdsList.value.contains(cardId)) return
        _revealedCardIdsList.value = _revealedCardIdsList.value.toMutableList()
            .also { list ->
                list.remove(cardId)
            }
    }

    fun collapseItem() {
        _revealedCardIdsList.value = _revealedCardIdsList.value.toMutableList()
            .also {
                it.clear()
            }
    }

    fun onFilterChanged(currentActive: Int, descending: Boolean, forArchive: Boolean = false, mine: Boolean, fromMe: Boolean) {
        if(!forArchive) {
            _activeFilter.value = currentActive
            _descending.value = descending
            _mineFilter.value = mine
            _fromMeFilter.value = fromMe
            Log.d("ViewModelFilter","main ${currentActive} ${descending} ${mine} ${fromMe}")
        } else {
            _activeArchiveFilter.value = currentActive
            _descendingArchive.value = descending
            _mineArchiveFilter.value = mine
            _fromMeArchiveFilter.value = fromMe
            Log.d("ViewModelFilter","arch ${currentActive} ${descending} ${mine} ${fromMe}")
        }

    }

    suspend fun toArchive(task: TaskModel, toArchive: Boolean, isCreatorTask: Boolean, isResponsibleTask: Boolean): Boolean {
        onItemCollapsed(task.id ?: "")
        return onArchiveClicked(task.id ?: "", toArchive, isCreatorTask, isResponsibleTask)
    }

    fun clearProcessedTask() {
        _currentProcessedTask.value = _currentProcessedTask.value.toMutableList()
            .also {
                it.clear()
            }
    }

    fun addProcessedTask(task: TaskModel) {
        _currentProcessedTask.value = _currentProcessedTask.value.toMutableList()
            .also {
                it.add(task)
            }
    }

    fun <T> MutableList<T>.prepend(element: T) {
        add(0, element)
    }

    fun addCommentForPreview(model: DataModel) {
        _getComments.value = _getComments.value.toMutableList().also {
            Timber.tag("addCommentForPreview").d("${model.timestamp}")
            it.add(
                DataModel(
                    text = model.text,
                    timestamp = model.timestamp,
                    userRelated = model.userRelated
                )
            )
        }
        Timber.d("addCommentForPreview ${_getComments.value}")
    }

    // endregion

}