package com.team.focusstudy.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.team.focusstudy.core.database.entity.FocusSessionEntity
import com.team.focusstudy.core.model.session.SessionStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusSessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: FocusSessionEntity)

    @Update
    suspend fun update(session: FocusSessionEntity)

    @Query("SELECT * FROM focus_session WHERE id = :sessionId LIMIT 1")
    suspend fun getSession(sessionId: String): FocusSessionEntity?

    @Query(
        """
        SELECT * FROM focus_session
        WHERE user_id = :userId
        ORDER BY start_time DESC
        LIMIT :limit
        """
    )
    fun observeRecentSessions(userId: String, limit: Int = 30): Flow<List<FocusSessionEntity>>

    @Query(
        """
        SELECT * FROM focus_session
        WHERE user_id = :userId
          AND start_time BETWEEN :startMillis AND :endMillis
        ORDER BY start_time DESC
        """
    )
    suspend fun getSessionsInRange(userId: String, startMillis: Long, endMillis: Long): List<FocusSessionEntity>

    @Query(
        """
        SELECT COUNT(*) FROM focus_session
        WHERE user_id = :userId
          AND strftime('%Y-%m-%d', start_time / 1000, 'unixepoch', 'localtime') = :statDate
        """
    )
    suspend fun countSessionsByDate(userId: String, statDate: String): Int

    @Query(
        """
        SELECT COUNT(*) FROM focus_session
        WHERE user_id = :userId
          AND session_status = :status
          AND strftime('%Y-%m-%d', start_time / 1000, 'unixepoch', 'localtime') = :statDate
        """
    )
    suspend fun countSessionsByDateAndStatus(userId: String, statDate: String, status: SessionStatus): Int

    @Query(
        """
        SELECT IFNULL(SUM(actual_minutes), 0) FROM focus_session
        WHERE user_id = :userId
          AND strftime('%Y-%m-%d', start_time / 1000, 'unixepoch', 'localtime') = :statDate
        """
    )
    suspend fun sumActualMinutesByDate(userId: String, statDate: String): Int
}