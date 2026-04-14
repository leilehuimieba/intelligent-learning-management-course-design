package com.team.focusstudy.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team.focusstudy.core.database.entity.DistractionEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DistractionEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: DistractionEventEntity)

    @Query("SELECT * FROM distraction_event WHERE session_id = :sessionId ORDER BY event_time DESC")
    fun observeSessionEvents(sessionId: String): Flow<List<DistractionEventEntity>>

    @Query("SELECT COUNT(*) FROM distraction_event WHERE session_id = :sessionId")
    suspend fun countBySession(sessionId: String): Int

    @Query(
        """
        SELECT COUNT(*) FROM distraction_event e
        INNER JOIN focus_session s ON e.session_id = s.id
        WHERE s.user_id = :userId
          AND strftime('%Y-%m-%d', e.event_time / 1000, 'unixepoch', 'localtime') = :statDate
        """
    )
    suspend fun countByDate(userId: String, statDate: String): Int
}
