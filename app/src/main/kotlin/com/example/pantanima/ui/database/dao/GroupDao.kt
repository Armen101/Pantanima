package com.example.pantanima.ui.database.dao

import androidx.room.*
import com.example.pantanima.ui.database.entity.Group

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(nouns: List<Group>)

    @Query(
        """
        SELECT * FROM groupTable
        WHERE language = :language
        ORDER BY lastUsedTime *1 ASC
        LIMIT :count 
        """
    )
    fun getAll(language: String, count: Int): MutableList<Group>

    @Query(
        """
        SELECT * FROM groupTable
        WHERE language = :language AND value NOT IN (:withoutList)
        ORDER BY lastUsedTime *1 ASC
        LIMIT :count 
        """
    )
    fun getAll(language: String, withoutList: List<String>, count: Int): MutableList<Group>

    @Update
    fun update(groups: List<Group>)

    @Update
    fun update(group: Group)
}