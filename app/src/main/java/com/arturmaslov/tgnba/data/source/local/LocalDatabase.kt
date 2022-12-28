package com.arturmaslov.tgnba.data.source.local

import android.content.Context
import androidx.room.*
import com.arturmaslov.tgnba.Constants
import com.arturmaslov.tgnba.data.models.Team
import com.orhanobut.logger.Logger

@Database(
    entities = [
        Team::class,
    ], version = 1
)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    // The associated DAOs for the database
    abstract val teamDao: TeamDao?

    companion object {
        private const val DATABASE_NAME = Constants.DATABASE_NAME
        private lateinit var INSTANCE: LocalDatabase

        fun getInstance(context: Context): LocalDatabase {
            // The .isInitialized Kotlin property returns true if the lateinit property (INSTANCE in this example) has been assigned a value, and false otherwise.
            if (!Companion::INSTANCE.isInitialized) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        LocalDatabase::class.java, DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    Logger.d("Made new database instance")
                }
            }
            return INSTANCE
        }
    }
}

@Dao
interface TeamDao {
    //Courier
    @Query("SELECT * FROM team")
    fun getTeams(): Team?

    // returns row id of inserted item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTeam(courier: Team?): Long

    // returns number of rows affected
    @Query("DELETE FROM team")
    fun deleteTeams(): Int

}