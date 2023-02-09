package ru.baccasoft.zadachnik.data.network.models

import com.google.gson.annotations.SerializedName

data class AuthResponseModel(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("user_id")
    val userId: String
)
