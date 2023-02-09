package ru.baccasoft.zadachnik.data.network.models

import com.google.gson.annotations.SerializedName

data class Pagination (

    @SerializedName("next"     ) var next     : String? = null,
    @SerializedName("previous" ) var previous : String? = null

)