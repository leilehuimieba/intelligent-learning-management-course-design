package com.team.focusstudy.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.team.focusstudy.core.database.entity.ReminderRuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderRuleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rule: ReminderRuleEntity)

    @Update
    suspend fun update(rule: ReminderRuleEntity)

    @Query("DELETE FROM reminder_rule WHERE id = :ruleId")
    suspend fun delete(ruleId: String)

    @Query("SELECT * FROM reminder_rule WHERE user_id = :userId ORDER BY enabled DESC, hour ASC, minute ASC")
    fun observeByUser(userId: String): Flow<List<ReminderRuleEntity>>
}