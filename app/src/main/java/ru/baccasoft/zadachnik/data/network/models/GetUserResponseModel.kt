package ru.baccasoft.zadachnik.data.network.models

import com.google.gson.annotations.SerializedName

data class GetUserResponseModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("show_tips")
    val showTips: Boolean
)