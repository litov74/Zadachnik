package ru.baccasoft.zadachnik.base

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import ru.baccasoft.zadachnik.R
import ru.baccasoft.zadachnik.data.network.models.HintModel
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


abstract class BaseViewModel : ViewModel() {

    protected fun setHint(hint: HintModel) {
        _hint.value = hint
    }

    suspend fun showError(error: String, onCloseAlertError: () -> Unit = {}) {
        _showError.emit(Pair(error, onCloseAlertError))
    }

    suspend fun showActionDialog(text: String, onActionText: String, onAction: () -> Unit = {}, onClose: () -> Unit = {}) {
        _showAction.emit(Pair(Pair(onAction, onClose), Pair(text, onActionText)))
    }

    fun clearIs403ErrorLodOut() {
        _isErrorLodOut.value = null
    }

    suspend fun showError(
        throwable: Throwable
    ) {
        Timber.e(throwable)
        showError(
            if (isInternetException(throwable)) getInternetErrorText(throwable)
            else throwable.message.toString()
        )
    }

    suspend fun showAction(
        text: String,
        onActionText: String,
        onAction: () -> Unit,
        onClose: () -> Unit,
    ) {
        showActionDialog(
            text = text,
            onActionText = onActionText,
            onClose = onClose,
            onAction = onAction
        )
    }

    protected fun getInternetErrorText(throwable: Throwable?): String {
        return when {
            throwable is HttpException
                    && (throwable.code() == 401 || throwable.code() == 403) -> {
                _isErrorLodOut.value = true
                context.getString(R.string.you_need_re_authorize)
            }
            throwable is HttpException
                    && (throwable.code() == 405 || throwable.code() == 500) -> {
                context.getString(R.string.system_error_has_occurred_retry_operation_later)
            }
            throwable is SocketTimeoutException -> {
                context.getString(R.string.no_response_from_server)
            }
            throwable is ConnectException
                    || throwable is UnknownHostException -> {
                context.getString(R.string.check_internet)
            }
            else -> {
                throwable?.message
                    ?: context.getString(R.string.unknown_error)
            }
        }
    }

    protected fun isInternetException(throwable: Throwable?): Boolean {
        return throwable is HttpException
                || throwable is SocketTimeoutException
                || throwable is ConnectException
                || throwable is UnknownHostException
    }

    companion object {
        private val _hint = MutableStateFlow(HintModel("", ""))
        val hint: StateFlow<HintModel> get() = _hint

        internal val _showError = MutableSharedFlow<Pair<String, () -> Unit>?>()
        val showError = _showError.asSharedFlow()

        internal val _showAction = MutableSharedFlow<Pair<Pair<() -> Unit, () -> Unit>, Pair<String, String>>?>()
        val showAction = _showAction.asSharedFlow()

        internal val _isErrorLodOut = MutableStateFlow<Boolean?>(false)
        val isErrorLodOut = _isErrorLodOut.asStateFlow()

        lateinit var context: Context
    }
}