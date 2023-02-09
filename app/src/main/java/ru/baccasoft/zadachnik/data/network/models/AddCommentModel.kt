package ru.baccasoft.zadachnik.data.network.models

import com.google.gson.annotations.SerializedName

data class AddCommentModel(
    @SerializedName("text"    ) var text   : String,
    @SerializedName("task_id" ) var taskId : String
)
