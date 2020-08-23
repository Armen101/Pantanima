package com.example.pantanima.ui.database.entity

import androidx.databinding.ObservableBoolean
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "enWords")
data class Word(@PrimaryKey var word: String, var lastUsedTime: Long) {
    @Ignore
    var isActive = ObservableBoolean(true)
}