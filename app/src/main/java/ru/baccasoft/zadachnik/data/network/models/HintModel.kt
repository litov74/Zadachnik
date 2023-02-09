package ru.baccasoft.zadachnik.data.network.models

import com.google.gson.annotations.SerializedName

data class HintModel(
    @SerializedName("text")
    val name: String,
    @SerializedName("url")
    val url: String
)
