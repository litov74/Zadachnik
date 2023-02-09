package ru.baccasoft.zadachnik.data.network.models

import com.google.gson.annotations.SerializedName

data class UpdateTaskModel(
    @SerializedName("title") var title: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("comment") var comment: String? = null,
    @SerializedName("tracker_id") var trackerId: String? = null,
    @SerializedName("creator_id") var creatorId: String? = null,
    @SerializedName("responsible_id") var responsibleId: String? = null,
    @SerializedName("responsible_phone") var responsiblePhone: String? = null,
    @SerializedName("responsible_fullname") var responsibleFullName: String? = null,
    @SerializedName("deadline") var deadline: String? = null,
    @SerializedName("reply_for") var replyFor: String? = null,
    @SerializedName("in_archive_for_creator") var inArchiveForCreator: Boolean? = null,
    @SerializedName("in_archive_for_responsible") var inArchiveForResponsible: Boolean?
)
