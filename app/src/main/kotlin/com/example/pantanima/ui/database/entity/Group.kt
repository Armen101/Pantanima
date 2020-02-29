package com.example.pantanima.ui.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groupTable")
data class Group(@PrimaryKey var value: String){

    var lastUsedTime: Long = 0

    var language: String = ""

}