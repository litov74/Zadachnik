package ru.baccasoft.zadachnik.data.network.models

import com.google.gson.annotations.SerializedName

data class TaskToArchiveModel(
    @SerializedName("in_archive_for_creator")
    val inArchiveForCreator: Boolean? = null,
    @SerializedName("in_archive_for_responsible")
    val inArchiveForResponsible: Boolean? = null,
)