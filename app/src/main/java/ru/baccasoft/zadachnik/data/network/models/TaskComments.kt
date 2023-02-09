package ru.baccasoft.zadachnik.data.network.models

import com.google.gson.annotations.SerializedName

data class TaskComments (

    @SerializedName("data"       ) var data       : ArrayList<DataModel> = arrayListOf(),
    @SerializedName("total"      ) var total      : Int?            = null,
    @SerializedName("count"      ) var count      : Int?            = null,
    @SerializedName("pagination" ) var pagination : Pagination?     = Pagination()

)