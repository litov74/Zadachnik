package ru.baccasoft.zadachnik.data.network.models

import com.google.gson.annotations.SerializedName

data class LoginRequestModel(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("code")
    val code: String
)
