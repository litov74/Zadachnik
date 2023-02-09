package ru.baccasoft.zadachnik.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.*
import okhttp3.internal.http.RealResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import okio.Okio
import okio.Source
import okio.buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.baccasoft.zadachnik.AuthRepo
import ru.baccasoft.zadachnik.AuthRepoImpl
import ru.baccasoft.zadachnik.Constants
import ru.baccasoft.zadachnik.data.SessionDataStorage
import ru.baccasoft.zadachnik.data.network.ResultCallAdapterFactory
import ru.baccasoft.zadachnik.data.network.RetrofitApi
import ru.baccasoft.zadachnik.data.prefs.PrefsHelper
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DIModule {

    @Provides
    @Singleton
    fun provideAppCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.IO + Job())

    @Singleton
    @Provides
    fun provideSessionDataStorage(@ApplicationContext ctx: Context): SessionDataStorage = PrefsHelper(ctx)

    @Singleton
    @Provides
    fun provideAuthRepo(sessionDataStorage: SessionDataStorage, retrofitApi: RetrofitApi): AuthRepo = AuthRepoImpl(sessionDataStorage, retrofitApi)

    private fun getLogger(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.tag("OkHttp").d(message);
            }
        })
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        authenticator: AuthenticatorMain,
        headerInterceptor: HeaderInterceptor
    ): RetrofitApi {

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .authenticator(authenticator)
            .addInterceptor(getLogger())
            .callTimeout(60, TimeUnit.SECONDS)

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.API_SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .build()
        return retrofit.create(RetrofitApi::class.java)
    }
}

@Singleton
class HeaderInterceptor  @Inject constructor (
    val sessionDataStorage: SessionDataStorage,
    ) : Interceptor {

    private val tokenHeaderName = "Authorization"

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().addHeader(tokenHeaderName, "Bearer ${
                    if (request.url.toString().contains("refresh_token")) sessionDataStorage.getRefreshToken()
                    else sessionDataStorage.getAccessToken()
                }").build()
        val response = chain.proceed(request)
        return response
    }

}

@Singleton
class AuthenticatorMain @Inject constructor (
    private val authRepo: dagger.Lazy<AuthRepo>
    ) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val link = response.request.url.toString()
        return if (link.contains("refresh_token") || link.contains("login")) {
            null
        } else {
            val accessToken = authRepo.get().getNewTokens()
            response.request.newBuilder().header("Authorization", "Bearer $accessToken").build()
        }
    }
}