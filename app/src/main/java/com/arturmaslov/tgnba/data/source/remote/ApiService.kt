package com.arturmaslov.tgnba.data.source.remote

import com.arturmaslov.tgnba.data.models.GameResponse
import com.arturmaslov.tgnba.data.models.PlayerResponse
import com.arturmaslov.tgnba.data.models.TeamResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/v1/teams")
    suspend fun fetchTeamResponse(): Call<TeamResponse>

    @GET("api/v1/games")
    suspend fun fetchGameResponse(
        @Query("team_ids") teamIds: List<Int?>?,
        @Query("page") page: Int
    ): Call<GameResponse>

    @GET("api/v1/players")
    suspend fun fetchPlayerResponse(@Query("search") searchTerm: String): Call<PlayerResponse>

}