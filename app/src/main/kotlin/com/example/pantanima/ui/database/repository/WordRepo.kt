package com.example.pantanima.ui.database.repository

import com.example.pantanima.ui.database.entity.Word

interface WordRepo {

    fun updateLastUsedTime(words: List<Word>)

    fun getWords() : MutableList<Word>

}