package com.arturmaslov.tgnba.data.models

import com.google.gson.annotations.SerializedName

data class Player(

    @field:SerializedName("weight_pounds")
    val weightPounds: Any? = null,

    @field:SerializedName("height_feet")
    val heightFeet: Any? = null,

    @field:SerializedName("height_inches")
    val heightInches: Any? = null,

    @field:SerializedName("last_name")
    val lastName: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("position")
    val position: String? = null,

    @field:SerializedName("team")
    val team: Team? = null,

    @field:SerializedName("first_name")
    val firstName: String? = null
)
