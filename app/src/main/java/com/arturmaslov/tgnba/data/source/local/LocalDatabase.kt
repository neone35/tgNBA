package com.arturmaslov.tgnba.data.source.local

import androidx.room.*
import com.arturmaslov.tgnba.data.models.Team

@Database(
    entities = [
        Team::class,
    ], version = 2
)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    // The associated DAOs for the database
    abstract val teamDao: TeamDao?
}

@Dao
interface TeamDao {
    //Teams
    @Query("SELECT * FROM team")
    fun getTeams(): List<Team?>?

    // returns row id of inserted item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTeam(team: Team?): Long

    // returns number of rows affected
    @Query("DELETE FROM team")
    fun deleteTeams(): Int

}