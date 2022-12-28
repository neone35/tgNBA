package com.arturmaslov.tgnba.data

import com.google.gson.annotations.SerializedName

data class PlayerResponse(

	@field:SerializedName("data")
	val data: List<Player?>? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
)
