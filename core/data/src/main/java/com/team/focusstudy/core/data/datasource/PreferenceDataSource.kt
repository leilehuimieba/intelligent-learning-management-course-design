package com.team.focusstudy.core.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.team.focusstudy.core.data.repository.ActiveSession
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun isOnboardingDone(): Boolean {
        return dataStore.data.first()[KEY_ONBOARDING_DONE] ?: false
    }

    suspend fun setOnboardingDone(done: Boolean) {
        dataStore.edit { pref ->
            pref[KEY_ONBOARDING_DONE] = done
        }
    }

    suspend fun currentUserId(): String? {
        return dataStore.data.first()[KEY_CURRENT_USER_ID]
    }

    suspend fun setCurrentUserId(userId: String) {
        dataStore.edit { pref ->
            pref[KEY_CURRENT_USER_ID] = userId
        }
    }

    suspend fun saveActiveSession(activeSession: ActiveSession?) {
        dataStore.edit { pref ->
            if (activeSession == null) {
                pref.remove(KEY_ACTIVE_SESSION)
            } else {
                pref[KEY_ACTIVE_SESSION] = encodeActiveSession(activeSession)
            }
        }
    }

    suspend fun getActiveSession(): ActiveSession? {
        val raw = dataStore.data.first()[KEY_ACTIVE_SESSION] ?: return null
        return decodeActiveSession(raw)
    }

    private fun encodeActiveSession(value: ActiveSession): String {
        val task = value.taskId ?: ""
        return listOf(
            value.sessionId,
            value.userId,
            task,
            value.startTime.toString(),
            value.plannedMinutes.toString(),
            value.pauseDurationMillis.toString(),
            value.pauseCount.toString(),
            value.interruptCount.toString()
        ).joinToString("|")
    }

    private fun decodeActiveSession(raw: String): ActiveSession? {
        val parts = raw.split("|")
        if (parts.size != 8) return null
        return runCatching {
            ActiveSession(
                sessionId = parts[0],
                userId = parts[1],
                taskId = parts[2].ifBlank { null },
                startTime = parts[3].toLong(),
                plannedMinutes = parts[4].toInt(),
                pauseDurationMillis = parts[5].toLong(),
                pauseCount = parts[6].toInt(),
                interruptCount = parts[7].toInt()
            )
        }.getOrNull()
    }

    private companion object {
        val KEY_ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
        val KEY_CURRENT_USER_ID = stringPreferencesKey("current_user_id")
        val KEY_ACTIVE_SESSION = stringPreferencesKey("active_session")
    }
}