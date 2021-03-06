package com.lamti.kingsclock.ui.uistate

import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class Timer(private val startTime: Long) {

    var formattedTime by mutableStateOf(formatTime(startTime))
    var timeMillis by mutableStateOf(startTime)

    private val _isTimerFinished = MutableStateFlow(false)
    val isTimerFinished: StateFlow<Boolean> = _isTimerFinished.asStateFlow()

    private var coroutineScope = CoroutineScope(Dispatchers.Main)
    private var isActive = false

    private var lastTimestamp = 0L

    fun start() {
        if (isActive) return

        coroutineScope.launch {
            lastTimestamp = System.currentTimeMillis()
            this@Timer.isActive = true
            while (this@Timer.isActive) {
                delay(10L)
                timeMillis -= System.currentTimeMillis() - lastTimestamp
                lastTimestamp = System.currentTimeMillis()

                formattedTime = if (timeMillis <= 0) {
                    pause()
                    _isTimerFinished.update { true }
                    "time's up".uppercase()
                } else {
                    formatTime(timeMillis)
                }
            }
        }
    }

    fun addIncrement(increment: Int) {
        timeMillis += increment * 1000
    }

    fun pause() {
        isActive = false
    }

    fun reset() {
        coroutineScope.cancel()
        coroutineScope = CoroutineScope(Dispatchers.Main)
        timeMillis = startTime
        lastTimestamp = 0L
        formattedTime = formatTime(startTime)
        isActive = false
        _isTimerFinished.update { false }
    }

    private fun formatTime(timeMillis: Long): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val localDateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timeMillis),
                ZoneId.systemDefault()
            )
            val formatter = DateTimeFormatter.ofPattern(
                "mm:ss.S",
                Locale.getDefault()
            )
            localDateTime.format(formatter)
        } else {
            timeMillis.toString()
        }
    }
}
