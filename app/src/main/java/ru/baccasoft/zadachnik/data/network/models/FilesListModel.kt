package ru.baccasoft.zadachnik.data.network.models

import com.google.gson.annotations.SerializedName

data class FilesListModel (

    @SerializedName("id"           ) var id          : String? = null,
    @SerializedName("task_id"      ) var taskId      : String? = null,
    @SerializedName("filename"     ) var filename    : String? = null,
    @SerializedName("url"          ) var url         : String? = null,
    @SerializedName("content_type" ) var contentType : String? = null,
    @SerializedName("file_size"    ) var fileSize    : String? = null

)