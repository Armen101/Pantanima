package com.example.pantanima.ui.database.converters

import androidx.room.TypeConverter
import com.example.pantanima.ui.enums.Level

class LevelConverter {

    @TypeConverter
    fun toInt(value: Level): Int {
        return when (value) {
            Level.Light -> Level.Light.level
            Level.Medium -> Level.Medium.level
            Level.Hard -> Level.Hard.level
        }
    }

    @TypeConverter
    fun toLevel(value: Int): Level {
        return when (value) {
            Level.Light.level -> Level.Light
            Level.Medium.level -> Level.Medium
            Level.Hard.level -> Level.Hard
            else -> Level.Medium
        }
    }
}