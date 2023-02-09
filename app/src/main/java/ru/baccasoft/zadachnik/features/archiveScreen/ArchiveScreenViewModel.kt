package ru.baccasoft.zadachnik.features.archiveScreen

import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.baccasoft.zadachnik.data.network.RetrofitApi
import javax.inject.Inject

@HiltViewModel
class ArchiveScreenViewModel @Inject constructor(
    private val apiService: RetrofitApi,
    application: Application
) : ViewModel() {

}