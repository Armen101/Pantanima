package com.example.pantanima.ui.database.dao

import androidx.room.*
import com.example.pantanima.ui.database.DbConstants
import com.example.pantanima.ui.database.entity.Group
import io.reactivex.Single

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(nouns: List<Group>)

    @Query(
        """
        SELECT * FROM ${DbConstants.GROUP_TABLE_NAME}
        WHERE language = :language
        ORDER BY lastUsedTime *1 ASC
        LIMIT :count 
        """
    )
    fun getAll(language: String, count: Int): Single<MutableList<Group>>


    @Query(
        """
        SELECT * FROM ${DbConstants.GROUP_TABLE_NAME}
        WHERE language = :language AND value NOT IN (:withoutList)
        ORDER BY lastUsedTime *1 ASC
        LIMIT :count 
        """
    )
    fun getAll(language: String, withoutList: List<String>, count: Int): Single<MutableList<Group>>

    @Update
    fun update(groups: List<Group>)

    @Update
    fun update(group: Group)
}