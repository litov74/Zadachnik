package ru.baccasoft.zadachnik.data.network.models

import com.google.gson.annotations.SerializedName

data class SendCodeResponseModel(
    @SerializedName("code")
    val code: String
)