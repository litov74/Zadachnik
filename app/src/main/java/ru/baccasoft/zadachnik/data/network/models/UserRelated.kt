package ru.baccasoft.zadachnik.data.network.models

import com.google.gson.annotations.SerializedName

data class UserRelated (

    @SerializedName("id"    ) var id    : String? = null,
    @SerializedName("phone" ) var phone : String? = null

)