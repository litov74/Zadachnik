package ru.baccasoft.zadachnik.data.network.models

import com.google.gson.annotations.SerializedName

data class LoginResponseModel(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("user_id")
    val userId: String,
    // в случае ошибки от сервера чекнуть эти поля
    @SerializedName("status")
    val status: String,
    @SerializedName("msg")
    val message: String,
    @SerializedName("status_code")
    val statusCode: Int

)
