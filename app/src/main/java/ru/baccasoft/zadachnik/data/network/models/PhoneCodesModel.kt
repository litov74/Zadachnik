package ru.baccasoft.zadachnik.data.network.models

import com.google.gson.annotations.SerializedName

data class PhoneCodesModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("dial_code")
    val dialCode: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("format")
    val format: String
)