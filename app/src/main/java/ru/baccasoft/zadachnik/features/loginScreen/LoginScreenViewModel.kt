package ru.baccasoft.zadachnik.features.loginScreen

import android.app.Application
import android.content.Context
import android.util.Log
import android.util.LogPrinter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.baccasoft.zadachnik.ZadachnikApp
import ru.baccasoft.zadachnik.base.BaseViewModel
import ru.baccasoft.zadachnik.data.SessionDataStorage
import ru.baccasoft.zadachnik.data.network.RetrofitApi
import ru.baccasoft.zadachnik.data.network.models.LoginRequestModel
import ru.baccasoft.zadachnik.data.network.models.PhoneCodesModel
import ru.baccasoft.zadachnik.data.network.models.SendCodeRequestModel
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class LoginScreenViewModel @Inject constructor (
    @ApplicationContext private val context: Context,
    val apiService: RetrofitApi,
    val sessionDataStorage: SessionDataStorage,
    private val appCoroutineScope: CoroutineScope
    ) : BaseViewModel() {

    private val _phoneNumberLiveData = MutableStateFlow("")
    val phoneNumberLiveData: StateFlow<String> get() = _phoneNumberLiveData

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> get() = _loadingState

    val codesList = ArrayList<PhoneCodesModel>()

    init {
        generateCountriesList(context)
    }

    suspend fun requestCode(): Boolean {
        try {
            return suspendCoroutine { continuation ->
                val googleApiAvailability = GoogleApiAvailability.getInstance()
                val status = googleApiAvailability.isGooglePlayServicesAvailable(context)
                if(status != ConnectionResult.SUCCESS) {
                    appCoroutineScope.launch {
                        showError("Для работы приложения обновите сервисы Google")
                    }
                    viewModelScope.launch {
                        changeLoadingValue()
                        apiService.sendCode(
                            SendCodeRequestModel(
                                phoneNumberLiveData.value,
                                ""
                            )
                        ).onSuccess {
                            continuation.resume(true)
                        }.onFailure {
                            showError(it)
                            continuation.resume(false)
                        }
                        changeLoadingValue()
                    }
                } else {
                    FirebaseMessaging.getInstance().token.addOnCompleteListener { tokenTask ->
                        val token = if (tokenTask.exception == null) tokenTask.result else ""
                        Timber.i("LoginScreenViewModel %s", token)
                        viewModelScope.launch {
                            changeLoadingValue()
                            apiService.sendCode(
                                SendCodeRequestModel(
                                    phoneNumberLiveData.value,
                                    token
                                )
                            ).onSuccess {
                                continuation.resume(true)
                            }.onFailure {
                                showError(it)
                                continuation.resume(false)
                            }
                            changeLoadingValue()
                        }
                    }.addOnFailureListener {
                        Timber.e(it)
                        continuation.resume(false)
                    }
                }
            }
        } catch (t: Exception) {
            return false
        }

    }

    suspend fun login(code: String): Boolean {
        changeLoadingValue() // true
        var res = true
        apiService.login(LoginRequestModel(phoneNumberLiveData.value, code))
            .onSuccess {
                sessionDataStorage.setLogin(it.userId)
                sessionDataStorage.setAccessToken(it.accessToken)
                sessionDataStorage.setRefreshToken(it.refreshToken)
                sessionDataStorage.setContactsRequestShown(false)
                sessionDataStorage.setPhoneNumber(phoneNumberLiveData.value)
            }.onFailure {
                res = false
            }
        changeLoadingValue()
        return res
    }

    // вот тут аккуратно, т.к. номер телефона не должен содержать знаков, только цифры
    fun setPhoneNumber(number: String) {
        if (number.isNotEmpty()) _phoneNumberLiveData.value = number
    }

    fun changeLoadingValue() {
        _loadingState.value = !_loadingState.value
    }

    fun generateCountriesList(ctx: Context) {
        val manager = ctx.assets.open("country_codes.json").bufferedReader().use { it.readText() }
        // жесткое порево, но пусть будет
        // и вот теперь совершенно вынезапно это уже стринга, а не стрим
        var gson = Gson().fromJson(manager, Array<PhoneCodesModel>::class.java).asList()
        codesList.clear()
        codesList.addAll(gson)
    }

}