package ru.baccasoft.zadachnik.data.network.models

import com.google.gson.annotations.SerializedName

data class BeatModel(
    @SerializedName("Hello")
    val response: String
)

class Wrapper<T>(
    var result: T
)