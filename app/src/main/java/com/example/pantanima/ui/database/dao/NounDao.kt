package com.example.pantanima.ui.database.dao

import androidx.room.*
import com.example.pantanima.ui.database.DbConstants
import com.example.pantanima.ui.database.converters.LevelConverter
import com.example.pantanima.ui.database.entity.Noun
import com.example.pantanima.ui.enums.Level
import io.reactivex.Single

@Dao
@TypeConverters(LevelConverter::class)
interface NounDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(nouns: List<Noun>)

    @Query(
        """
        SELECT * FROM ${DbConstants.NOUN_TABLE_NAME}
        WHERE language = :language
        LIMIT :offset,:count
        """
    )
    fun getAll(language: String, count: Int, offset: Int): Single<List<Noun>>

    @Query(
        """
        SELECT * FROM ${DbConstants.NOUN_TABLE_NAME}
        WHERE language = :language AND level = :level
        LIMIT :offset,:count
        """
    )
    fun getAll(level: Level, language: String, count: Int, offset: Int): Single<List<Noun>>

}