package com.example.pantanima.ui.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pantanima.ui.database.dao.GroupDao
import com.example.pantanima.ui.database.dao.WordDao
import com.example.pantanima.ui.database.entity.Group
import com.example.pantanima.ui.database.entity.Word

@Database(entities = [Word::class, Group::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    abstract fun groupDao(): GroupDao

}