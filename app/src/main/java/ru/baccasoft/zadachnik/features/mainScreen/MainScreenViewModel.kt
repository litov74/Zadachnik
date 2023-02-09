package ru.baccasoft.zadachnik.features.mainScreen

import android.app.Application
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.baccasoft.zadachnik.data.network.RetrofitApi
import ru.baccasoft.zadachnik.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val apiService: RetrofitApi,
    val application: Application
) : BaseViewModel() {

    init {
        viewModelScope.launch {
            requestHintData()
        }
    }

    suspend fun requestHintData() {
        apiService.getTips()
            .onSuccess {
                setHint(it)
            }
    }

}