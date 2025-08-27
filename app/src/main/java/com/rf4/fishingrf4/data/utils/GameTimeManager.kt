package com.rf4.fishingrf4.data.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Duration
import java.time.LocalTime

object GameTimeManager {

    private const val REAL_MINUTES_PER_GAME_DAY = 60.0
    private const val GAME_HOURS_PER_DAY = 24.0

    private val _gameTime = MutableStateFlow(LocalTime.of(0, 0))
    val gameTime = _gameTime.asStateFlow()

    private var timeOffset = Duration.ZERO

    suspend fun start() {
        while (true) {
            val realTime = LocalTime.now()
            val realSecondsFromMidnight = realTime.toSecondOfDay().toDouble()

            val gameMinutesPerRealMinute = GAME_HOURS_PER_DAY
            val gameSecondsFromMidnight_base = (realSecondsFromMidnight / 60.0) * (gameMinutesPerRealMinute * 60.0)

            val adjustedTimeInSeconds = (gameSecondsFromMidnight_base + timeOffset.seconds) % (GAME_HOURS_PER_DAY * 3600)

            _gameTime.value = LocalTime.ofSecondOfDay(adjustedTimeInSeconds.toLong())

            delay(1000)
        }
    }

    // ✅ CORRECTION de la faute de frappe ici
    fun setTimeOffset(offset: Duration) {
        timeOffset = offset
    }

    fun getTimeOfDay(time: LocalTime = _gameTime.value): String {
        return when (time.hour) {
            in 6..11 -> "Matinée"
            in 12..18 -> "Journée"
            in 19..21 -> "Soirée"
            else -> "Nuit"
        }
    }
}