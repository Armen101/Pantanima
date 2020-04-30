package com.example.pantanima.ui.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "enGroups")
data class Group(@PrimaryKey var name: String){

    var lastUsedTime: Long = 0

}