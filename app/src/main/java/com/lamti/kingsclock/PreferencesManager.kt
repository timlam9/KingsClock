package com.lamti.kingsclock

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lamti.kingsclock.ui.uistate.ClockMode
import com.lamti.kingsclock.ui.uistate.UIState.Companion.INITIAL_INCREMENT
import com.lamti.kingsclock.ui.uistate.UIState.Companion.INITIAL_MINUTES
import com.lamti.kingsclock.ui.uistate.UIState.Companion.INITIAL_SECONDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class StoredClock(
    val minutes: Int,
    val seconds: Int,
    val increment: Int,
    val mode: ClockMode?,
)

class PreferencesManager(private val context: Context) {

    suspend fun saveClock(clock: StoredClock) {
        context.dataStore.edit { preferences ->
            preferences[CLOCK_MINUTES] = clock.minutes
            preferences[CLOCK_SECONDS] = clock.seconds
            preferences[CLOCK_INCREMENT] = clock.increment
            preferences[CLOCK_MODE] = clock.mode!!.name
        }
    }

    val storedClock: Flow<StoredClock> = context.dataStore.data
        .map { preferences ->
            preferences.run {
                StoredClock(
                    minutes = get(CLOCK_MINUTES) ?: INITIAL_MINUTES,
                    seconds = get(CLOCK_SECONDS) ?: INITIAL_SECONDS,
                    increment = get(CLOCK_INCREMENT) ?: INITIAL_INCREMENT,
                    mode = get(CLOCK_MODE).toClockMode()
                )
            }
        }


    companion object {

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("kingsClockDataStore")
        private val CLOCK_MINUTES = intPreferencesKey("clock_minutes")
        private val CLOCK_SECONDS = intPreferencesKey("clock_seconds")
        private val CLOCK_INCREMENT = intPreferencesKey("clock_increment")
        private val CLOCK_MODE = stringPreferencesKey("clock_mode")
    }
}

private fun String?.toClockMode(): ClockMode? = when (this) {
    ClockMode.Custom.name -> ClockMode.Custom
    ClockMode.Rapid.name -> ClockMode.Rapid
    ClockMode.Bullet.name -> ClockMode.Bullet
    ClockMode.Blitz.name -> ClockMode.Blitz
    ClockMode.Classical.name -> ClockMode.Classical
    else -> null
}
