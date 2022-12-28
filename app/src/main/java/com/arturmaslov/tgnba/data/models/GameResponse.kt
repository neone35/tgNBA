package com.arturmaslov.tgnba.data.models

import com.google.gson.annotations.SerializedName

data class GameResponse(

	@field:SerializedName("data")
	val data: List<Game?>? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
)

data class Game(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("postseason")
	val postseason: Boolean? = null,

	@field:SerializedName("period")
	val period: Int? = null,

	@field:SerializedName("season")
	val season: Int? = null,

	@field:SerializedName("visitor_team_score")
	val visitorTeamScore: Int? = null,

	@field:SerializedName("visitor_team")
	val visitorTeam: VisitorTeam? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("home_team_score")
	val homeTeamScore: Int? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("home_team")
	val homeTeam: HomeTeam? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class VisitorTeam(

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

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("abbreviation")
	val abbreviation: String? = null
)

data class HomeTeam(

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

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("abbreviation")
	val abbreviation: String? = null
)
