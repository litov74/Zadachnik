package ru.baccasoft.zadachnik.data.network.models

import com.google.gson.annotations.SerializedName

data class SendCodeRequestModel(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("firebase_token")
    val firebase: String
)