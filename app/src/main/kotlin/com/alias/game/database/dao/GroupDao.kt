package com.example.pantanima.ui.database.dao

import androidx.room.*
import com.example.pantanima.ui.database.entity.Group

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(nouns: List<Group>)

    @Query(
        """
        SELECT * FROM enGroups
        ORDER BY lastUsedTime *1 ASC
        LIMIT :count
        """
    )
    fun getAll(count: Int): MutableList<Group>

    @Query(
        """
        SELECT * FROM enGroups
        WHERE name NOT IN (:withoutList)
        ORDER BY lastUsedTime *1 ASC
        LIMIT :count
        """
    )
    fun getAll(withoutList: List<String>, count: Int): MutableList<Group>

    @Update
    fun update(groups: List<Group>)

    @Update
    fun update(group: Group)
}