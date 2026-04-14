package com.team.focusstudy.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.team.focusstudy.core.database.converter.EnumConverters
import com.team.focusstudy.core.database.dao.AdviceLogDao
import com.team.focusstudy.core.database.dao.AppSettingDao
import com.team.focusstudy.core.database.dao.DailyStatDao
import com.team.focusstudy.core.database.dao.DistractionEventDao
import com.team.focusstudy.core.database.dao.FocusSessionDao
import com.team.focusstudy.core.database.dao.ReminderRuleDao
import com.team.focusstudy.core.database.dao.StudyTaskDao
import com.team.focusstudy.core.database.dao.UserProfileDao
import com.team.focusstudy.core.database.entity.AdviceLogEntity
import com.team.focusstudy.core.database.entity.AppSettingEntity
import com.team.focusstudy.core.database.entity.DailyStatEntity
import com.team.focusstudy.core.database.entity.DistractionEventEntity
import com.team.focusstudy.core.database.entity.FocusSessionEntity
import com.team.focusstudy.core.database.entity.ReminderRuleEntity
import com.team.focusstudy.core.database.entity.StudyTaskEntity
import com.team.focusstudy.core.database.entity.UserProfileEntity

@Database(
    entities = [
        UserProfileEntity::class,
        StudyTaskEntity::class,
        FocusSessionEntity::class,
        DistractionEventEntity::class,
        DailyStatEntity::class,
        AdviceLogEntity::class,
        ReminderRuleEntity::class,
        AppSettingEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(EnumConverters::class)
abstract class FocusStudyDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun studyTaskDao(): StudyTaskDao
    abstract fun focusSessionDao(): FocusSessionDao
    abstract fun distractionEventDao(): DistractionEventDao
    abstract fun dailyStatDao(): DailyStatDao
    abstract fun adviceLogDao(): AdviceLogDao
    abstract fun reminderRuleDao(): ReminderRuleDao
    abstract fun appSettingDao(): AppSettingDao
}