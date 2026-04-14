package com.team.focusstudy.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.team.focusstudy.core.database.entity.StudyTaskEntity
import com.team.focusstudy.core.model.task.TaskStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyTaskDao {

    @Query(
        """
        SELECT * FROM study_task
        WHERE user_id = :userId
          AND deleted_at IS NULL
        ORDER BY CASE WHEN due_date IS NULL THEN 1 ELSE 0 END, due_date ASC, updated_at DESC
        """
    )
    fun observeTasks(userId: String): Flow<List<StudyTaskEntity>>

    @Query("SELECT * FROM study_task WHERE id = :taskId LIMIT 1")
    suspend fun getTask(taskId: String): StudyTaskEntity?

    @Query("SELECT * FROM study_task WHERE id = :taskId LIMIT 1")
    fun observeTask(taskId: String): Flow<StudyTaskEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: StudyTaskEntity)

    @Update
    suspend fun update(task: StudyTaskEntity)

    @Query("UPDATE study_task SET status = :status, updated_at = :updatedAt WHERE id = :taskId")
    suspend fun updateStatus(taskId: String, status: TaskStatus, updatedAt: Long)

    @Query(
        "UPDATE study_task SET actual_minutes = actual_minutes + :addedMinutes, last_session_at = :sessionAt, updated_at = :sessionAt WHERE id = :taskId"
    )
    suspend fun addActualMinutes(taskId: String, addedMinutes: Int, sessionAt: Long)

    @Query("UPDATE study_task SET deleted_at = :deletedAt, updated_at = :deletedAt, status = 'ARCHIVED' WHERE id = :taskId")
    suspend fun softDelete(taskId: String, deletedAt: Long)

    @Query(
        """
        SELECT COUNT(*) FROM study_task
        WHERE user_id = :userId
          AND deleted_at IS NULL
          AND status != 'ARCHIVED'
          AND (
            strftime('%Y-%m-%d', created_at / 1000, 'unixepoch', 'localtime') = :statDate
            OR strftime('%Y-%m-%d', due_date / 1000, 'unixepoch', 'localtime') = :statDate
          )
        """
    )
    suspend fun countPlannedTasksByDate(userId: String, statDate: String): Int

    @Query(
        """
        SELECT COUNT(*) FROM study_task
        WHERE user_id = :userId
          AND deleted_at IS NULL
          AND status = 'DONE'
          AND strftime('%Y-%m-%d', updated_at / 1000, 'unixepoch', 'localtime') = :statDate
        """
    )
    suspend fun countCompletedTasksByDate(userId: String, statDate: String): Int

    @Query(
        """
        SELECT * FROM study_task
        WHERE user_id = :userId
          AND deleted_at IS NULL
          AND status IN ('TODO', 'IN_PROGRESS')
          AND actual_minutes > 0
          AND estimated_minutes >= 30
        ORDER BY updated_at DESC
        LIMIT 5
        """
    )
    suspend fun getPotentialSplitTasks(userId: String): List<StudyTaskEntity>
}
