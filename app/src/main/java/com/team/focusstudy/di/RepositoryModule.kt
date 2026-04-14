package com.team.focusstudy.di

import com.team.focusstudy.core.data.repository.AdviceRepository
import com.team.focusstudy.core.data.repository.AdviceRepositoryImpl
import com.team.focusstudy.core.data.repository.FocusSessionRepository
import com.team.focusstudy.core.data.repository.FocusSessionRepositoryImpl
import com.team.focusstudy.core.data.repository.ProfileRepository
import com.team.focusstudy.core.data.repository.ProfileRepositoryImpl
import com.team.focusstudy.core.data.repository.ReminderRepository
import com.team.focusstudy.core.data.repository.ReminderRepositoryImpl
import com.team.focusstudy.core.data.repository.StatsRepository
import com.team.focusstudy.core.data.repository.StatsRepositoryImpl
import com.team.focusstudy.core.data.repository.TaskRepository
import com.team.focusstudy.core.data.repository.TaskRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Binds
    @Singleton
    abstract fun bindFocusSessionRepository(impl: FocusSessionRepositoryImpl): FocusSessionRepository

    @Binds
    @Singleton
    abstract fun bindStatsRepository(impl: StatsRepositoryImpl): StatsRepository

    @Binds
    @Singleton
    abstract fun bindAdviceRepository(impl: AdviceRepositoryImpl): AdviceRepository

    @Binds
    @Singleton
    abstract fun bindReminderRepository(impl: ReminderRepositoryImpl): ReminderRepository
}