package com.team.focusstudy.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team.focusstudy.core.database.entity.DailyStatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyStatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(stat: DailyStatEntity)

    @Query("SELECT * FROM daily_stat WHERE user_id = :userId AND stat_date = :statDate LIMIT 1")
    fun observeByDate(userId: String, statDate: String): Flow<DailyStatEntity?>

    @Query("SELECT * FROM daily_stat WHERE user_id = :userId AND stat_date = :statDate LIMIT 1")
    suspend fun getByDate(userId: String, statDate: String): DailyStatEntity?

    @Query(
        """
        SELECT * FROM daily_stat
        WHERE user_id = :userId
          AND stat_date BETWEEN :fromDate AND :toDate
        ORDER BY stat_date DESC
        """
    )
    fun observeRange(userId: String, fromDate: String, toDate: String): Flow<List<DailyStatEntity>>

    @Query(
        """
        SELECT * FROM daily_stat
        WHERE user_id = :userId
          AND stat_date BETWEEN :fromDate AND :toDate
        ORDER BY stat_date DESC
        """
    )
    suspend fun getRange(userId: String, fromDate: String, toDate: String): List<DailyStatEntity>
}
