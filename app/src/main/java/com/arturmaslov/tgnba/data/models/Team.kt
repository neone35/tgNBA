package com.arturmaslov.tgnba.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Team(

    @field:SerializedName("division")
    val division: String? = null,

    @field:SerializedName("conference")
    val conference: String? = null,

    @field:SerializedName("full_name")
    val fullName: String? = null,

    @field:SerializedName("city")
    val city: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:PrimaryKey
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("abbreviation")
    val abbreviation: String? = null
)
