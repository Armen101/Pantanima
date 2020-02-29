package com.example.pantanima.ui.database.repository

import com.example.pantanima.ui.database.entity.Noun

interface NounRepo {

    fun insertInitialNouns()

    fun updateLastUsedTime(nouns: List<Noun>)

    fun getNouns() : MutableList<Noun>

}