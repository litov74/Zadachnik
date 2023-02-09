package ru.baccasoft.zadachnik.data.network.models

import com.google.gson.annotations.SerializedName

data class DataModel (

    @SerializedName("id"           ) var id          : String?      = null,
    @SerializedName("text"         ) var text        : String?      = null,
    @SerializedName("author_id"    ) var authorId    : String?      = null,
    @SerializedName("task_id"      ) var taskId      : String?      = null,
    @SerializedName("timestamp"    ) var timestamp   : String?      = null,
    @SerializedName("user_related" ) var userRelated : UserRelated? = UserRelated()

)