package ru.baccasoft.zadachnik

import android.app.Application
import android.content.Context
import com.github.anrwatchdog.ANRWatchDog
import dagger.hilt.android.HiltAndroidApp
import okhttp3.Dispatcher
import ru.baccasoft.zadachnik.data.SessionDataStorage
import ru.baccasoft.zadachnik.data.network.RetrofitApi
import ru.baccasoft.zadachnik.logs.FileLoggingTree
import ru.baccasoft.zadachnik.logs.ReleaseTree
import ru.baccasoft.zadachnik.logs.exception.ExceptionHandler
import timber.log.Timber

@HiltAndroidApp
class ZadachnikApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initLogger(this)
    }

    private fun initLogger(context: Context) {
        ANRWatchDog().start()
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler())
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree()) else Timber.plant(ReleaseTree())
        Timber.plant(FileLoggingTree(context))
        Timber.i("Logger initialized")
    }
}

interface AuthRepo {

    fun getNewTokens(): String

}

class AuthRepoImpl (
    private val sessionDataStorage: SessionDataStorage,
    private val retrofitApi: RetrofitApi
    ) : AuthRepo {

    override fun getNewTokens(): String {

        val call = retrofitApi.refreshToken()
        val response = call.execute().body()
        val accessToken = response?.accessToken ?: ""
        val refreshToken = response?.refreshToken ?: ""

        if (accessToken.isEmpty() || refreshToken.isEmpty()) {
            return ""
        }

        sessionDataStorage.setAccessToken(accessToken)
        sessionDataStorage.setRefreshToken(refreshToken)
        return accessToken

    }
}