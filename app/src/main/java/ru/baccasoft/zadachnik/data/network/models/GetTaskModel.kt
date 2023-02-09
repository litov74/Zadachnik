package ru.baccasoft.zadachnik.data.network.models

import com.google.gson.annotations.SerializedName

data class GetTaskModel(
    @SerializedName("id"                         ) var id                      : String,
    @SerializedName("title"                      ) var title                   : String,
    @SerializedName("description"                ) var description             : String,
    @SerializedName("comment"                    ) var comment                 : String,
    @SerializedName("tracker_id"                 ) var trackerId               : String,
    @SerializedName("creator_id"                 ) var creatorId               : String,
    @SerializedName("responsible_id"             ) var responsibleId           : String,
    @SerializedName("responsible_fullname"       ) var responsibleFullName     : String,
    @SerializedName("deadline"                   ) var deadline                : String,
    @SerializedName("reply_for"                  ) var replyFor                : String,
    @SerializedName("created_at"                 ) var createdAt               : String,
    @SerializedName("updated_at"                 ) var updatedAt               : String,
    @SerializedName("in_archive_for_creator"     ) var inArchiveForCreator     : Boolean,
    @SerializedName("in_archive_for_responsible" ) var inArchiveForResponsible : Boolean,
    @SerializedName("task_comments"              ) var taskComments            : List<DataModel>?
)
