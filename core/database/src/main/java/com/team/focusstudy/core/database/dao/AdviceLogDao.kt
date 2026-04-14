package com.team.focusstudy.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team.focusstudy.core.database.entity.AdviceLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AdviceLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(advice: AdviceLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(adviceList: List<AdviceLogEntity>)

    @Query(
        """
        SELECT * FROM advice_log
        WHERE user_id = :userId
          AND stat_date = :statDate
        ORDER BY priority ASC, created_at DESC
        """
    )
    fun observeByDate(userId: String, statDate: String): Flow<List<AdviceLogEntity>>

    @Query(
        """
        SELECT * FROM advice_log
        WHERE user_id = :userId
          AND stat_date BETWEEN :fromDate AND :toDate
          AND (:isRead IS NULL OR is_read = :isRead)
        ORDER BY stat_date DESC, priority ASC
        """
    )
    fun observeByRange(userId: String, fromDate: String, toDate: String, isRead: Boolean?): Flow<List<AdviceLogEntity>>

    @Query("UPDATE advice_log SET is_read = :isRead WHERE id = :adviceId")
    suspend fun updateRead(adviceId: String, isRead: Boolean)

    @Query("DELETE FROM advice_log WHERE user_id = :userId AND stat_date = :statDate")
    suspend fun deleteByDate(userId: String, statDate: String)
}