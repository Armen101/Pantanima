package com.example.pantanima.ui.database.dao

import androidx.room.*
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
        SELECT * FROM nounTable
        WHERE language = :language
        ORDER BY lastUsedTime *1 ASC
        LIMIT :count 
        """
    )
    fun getAll(language: String, count: Int): MutableList<Noun>

    @Query(
        """
        SELECT * FROM nounTable
        WHERE language = :language AND level = :level
        ORDER BY lastUsedTime *1 ASC
        LIMIT :count
        """
    )
    fun getAll(level: Level, language: String, count: Int): Single<MutableList<Noun>>

    @Update
    fun update(nouns: List<Noun>)

}