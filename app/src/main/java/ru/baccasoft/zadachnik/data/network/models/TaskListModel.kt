package ru.baccasoft.zadachnik.data.network.models

import com.google.gson.annotations.SerializedName
import ru.baccasoft.zadachnik.features.mainScreen.TaskModel
import javax.annotation.concurrent.Immutable

@Immutable
data class TaskListModel(
    @SerializedName("data")
    val taskList: ArrayList<TaskModel>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("count")
    val count: Int,
    @SerializedName("pagination")
    val pagination: PaginationModel
)