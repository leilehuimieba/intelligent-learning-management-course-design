package com.team.focusstudy.di

import android.content.Context
import androidx.room.Room
import com.team.focusstudy.core.database.DATABASE_NAME
import com.team.focusstudy.core.database.FocusStudyDatabase
import com.team.focusstudy.core.database.dao.AdviceLogDao
import com.team.focusstudy.core.database.dao.AppSettingDao
import com.team.focusstudy.core.database.dao.DailyStatDao
import com.team.focusstudy.core.database.dao.DistractionEventDao
import com.team.focusstudy.core.database.dao.FocusSessionDao
import com.team.focusstudy.core.database.dao.ReminderRuleDao
import com.team.focusstudy.core.database.dao.StudyTaskDao
import com.team.focusstudy.core.database.dao.UserProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFocusStudyDatabase(
        @ApplicationContext context: Context
    ): FocusStudyDatabase {
        return Room.databaseBuilder(context, FocusStudyDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserProfileDao(db: FocusStudyDatabase): UserProfileDao = db.userProfileDao()

    @Provides
    fun provideStudyTaskDao(db: FocusStudyDatabase): StudyTaskDao = db.studyTaskDao()

    @Provides
    fun provideFocusSessionDao(db: FocusStudyDatabase): FocusSessionDao = db.focusSessionDao()

    @Provides
    fun provideDistractionEventDao(db: FocusStudyDatabase): DistractionEventDao = db.distractionEventDao()

    @Provides
    fun provideDailyStatDao(db: FocusStudyDatabase): DailyStatDao = db.dailyStatDao()

    @Provides
    fun provideAdviceLogDao(db: FocusStudyDatabase): AdviceLogDao = db.adviceLogDao()

    @Provides
    fun provideReminderRuleDao(db: FocusStudyDatabase): ReminderRuleDao = db.reminderRuleDao()

    @Provides
    fun provideAppSettingDao(db: FocusStudyDatabase): AppSettingDao = db.appSettingDao()
}