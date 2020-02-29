package com.example.pantanima.ui.database.entity

import androidx.databinding.ObservableBoolean
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.pantanima.ui.database.DbConstants
import com.example.pantanima.ui.database.converters.LevelConverter
import com.example.pantanima.ui.enums.Level

@Entity(tableName = DbConstants.NOUN_TABLE_NAME)
data class Noun(@PrimaryKey var value: String) {

    var lastUsedTime: Long = 0

    var language: String = ""

    @TypeConverters(LevelConverter::class)
    var level: Level = Level.Medium

    @Ignore
    var isActive = ObservableBoolean(true)

    override fun toString(): String {
        return "Noun(value = '$value', language = '$language', level = $level)"
    }
}