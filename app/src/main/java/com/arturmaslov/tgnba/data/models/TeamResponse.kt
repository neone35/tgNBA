package com.arturmaslov.tgnba.data.models

import com.google.gson.annotations.SerializedName

data class TeamResponse(

	@field:SerializedName("data")
	val data: List<Team?>? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
)

