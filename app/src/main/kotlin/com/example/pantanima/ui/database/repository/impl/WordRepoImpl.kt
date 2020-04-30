package com.example.pantanima.ui.database.repository.impl

import com.example.pantanima.ui.helpers.GamePrefs
import com.example.pantanima.ui.database.dao.WordDao
import com.example.pantanima.ui.database.entity.Word
import com.example.pantanima.ui.database.repository.WordRepo

class WordRepoImpl(private val dao: WordDao) : WordRepo {

    override fun getWords() = dao.getWords(GamePrefs.ASSORTMENT_WORDS_COUNT)

    override fun updateLastUsedTime(words: List<Word>) {
        for (noun in words) {
            noun.lastUsedTime = System.currentTimeMillis()
        }
        dao.update(words)
    }
}