package com.example.pantanima.ui.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.pantanima.ui.database.DbConstants
import com.example.pantanima.ui.database.converters.LevelConverter
import com.example.pantanima.ui.enums.Level

@Entity(tableName = DbConstants.NOUN_TABLE_NAME)
data class Noun(@PrimaryKey var value: String) {

    var language: String = ""

    @TypeConverters(LevelConverter::class)
    var level: Level = Level.Medium

    override fun toString(): String {
        return "Noun(value = '$value', language = '$language', level = $level)"
    }
}