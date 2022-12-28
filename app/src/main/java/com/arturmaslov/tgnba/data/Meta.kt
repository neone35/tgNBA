package com.arturmaslov.tgnba.data

import com.google.gson.annotations.SerializedName

data class Meta(

    @field:SerializedName("next_page")
    val nextPage: Int? = null,

    @field:SerializedName("per_page")
    val perPage: Int? = null,

    @field:SerializedName("total_count")
    val totalCount: Int? = null,

    @field:SerializedName("total_pages")
    val totalPages: Int? = null,

    @field:SerializedName("current_page")
    val currentPage: Int? = null
)
