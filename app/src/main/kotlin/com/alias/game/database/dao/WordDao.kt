package com.example.pantanima.ui.database.dao

import androidx.room.*
import com.example.pantanima.ui.database.entity.Word

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(words: List<Word>)

    @Query(
        """
        SELECT * FROM enWords
        ORDER BY lastUsedTime *1 ASC
        LIMIT :count 
        """
    )
    fun getWords(count: Int): MutableList<Word>

    @Update
    fun update(words: List<Word>)

}