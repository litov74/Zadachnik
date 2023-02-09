package ru.baccasoft.zadachnik.data.network.models

import com.google.gson.annotations.SerializedName

data class PaginationModel(
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String?
)
