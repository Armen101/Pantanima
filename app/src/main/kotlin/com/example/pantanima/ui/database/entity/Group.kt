package com.example.pantanima.ui.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pantanima.ui.database.DbConstants

@Entity(tableName = DbConstants.GROUP_TABLE_NAME)
class Group(@PrimaryKey var value: String){

    var lastUsedTime: Long = 0

    var language: String = ""

    override fun toString(): String {
        return "Group(value='$value', lastUsedTime=$lastUsedTime, language='$language')"
    }

}