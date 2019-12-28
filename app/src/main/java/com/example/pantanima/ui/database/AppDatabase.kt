package com.example.pantanima.ui.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pantanima.ui.database.dao.GroupDao
import com.example.pantanima.ui.database.dao.NounDao
import com.example.pantanima.ui.database.entity.Group
import com.example.pantanima.ui.database.entity.Noun

@Database(entities = [Noun::class, Group::class], version = DbConstants.DB_VERSION)
abstract class AppDatabase : RoomDatabase() {

    abstract fun nounDao(): NounDao

    abstract fun groupDao(): GroupDao

}