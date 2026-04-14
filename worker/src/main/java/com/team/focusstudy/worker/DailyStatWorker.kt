package com.team.focusstudy.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.team.focusstudy.core.data.repository.ProfileRepository
import com.team.focusstudy.core.data.usecase.RecalculateDailyStatUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate

@HiltWorker
class DailyStatWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val profileRepository: ProfileRepository,
    private val recalculateDailyStatUseCase: RecalculateDailyStatUseCase
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val userId = profileRepository.currentUserId() ?: return Result.success()
        val date = inputData.getString("date") ?: LocalDate.now().toString()
        recalculateDailyStatUseCase(userId, date)
        return Result.success()
    }
}