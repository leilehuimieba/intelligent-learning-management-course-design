package com.team.focusstudy.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team.focusstudy.core.database.entity.AppSettingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppSettingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(setting: AppSettingEntity)

    @Query("SELECT * FROM app_setting WHERE setting_key = :key LIMIT 1")
    suspend fun getByKey(key: String): AppSettingEntity?

    @Query("SELECT * FROM app_setting WHERE setting_key = :key LIMIT 1")
    fun observeByKey(key: String): Flow<AppSettingEntity?>
}