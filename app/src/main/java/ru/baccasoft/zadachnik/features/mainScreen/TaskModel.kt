package ru.baccasoft.zadachnik.features.mainScreen

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class TaskModel(
    @SerializedName("id") var id: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("tracker_id") var trackerId: String? = null,
    @SerializedName("creator_id") var creatorId: String? = null,
    @SerializedName("responsible_id") var responsibleId: String? = null,
    @SerializedName("responsible_fullname") var responsibleFullName: String? = null,
    @SerializedName("deadline") var deadline: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("in_archive_for_creator") var inArchiveForCreator: Boolean? = null,
    @SerializedName("in_archive_for_responsible") var inArchiveForResponsible: Boolean? = null,
    @SerializedName("is_commented") var isCommented: Boolean? = null
)