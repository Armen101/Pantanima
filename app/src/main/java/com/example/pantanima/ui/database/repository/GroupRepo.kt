package com.example.pantanima.ui.database.repository

import com.example.pantanima.ui.Constants
import com.example.pantanima.ui.database.dao.GroupDao
import com.example.pantanima.ui.database.entity.Group
import com.example.pantanima.ui.helpers.GamePrefs

object GroupRepo {

    private lateinit var dao: GroupDao

    fun injectDao(dao: GroupDao) {
        this.dao = dao
    }

    fun insertInitialGroups() {
        val groups = arrayListOf<Group>()
        addArmenianInitialGroupNames(groups)
        addRussianInitialGroupNames(groups)
        addEnglishInitialGroupNames(groups)
        dao.insert(groups)
    }

    private fun addArmenianInitialGroupNames(list: MutableList<Group>) {
        val names = listOf(
            "հյուսիս",
            "գաղափար",
            "ճարպոտ",
            "առյուծ",
            "մաճառ",
            "հրաշք",
            "մռայլ",
            "ռազմիկ"
        )
        for (item in names) {
            val group = Group(item)
            group.language = Constants.LANGUAGE_AM
            list.add(group)
        }
    }

    private fun addRussianInitialGroupNames(list: MutableList<Group>) {
        val names = listOf(
            "грибок",
            "гардероб",
            "буханка",
            "водка",
            "сироп"
        )
        for (item in names) {
            val group = Group(item)
            group.language = Constants.LANGUAGE_RU
            list.add(group)
        }
    }

    private fun addEnglishInitialGroupNames(list: MutableList<Group>) {
        val names = listOf(
            "pigs",
            "bosses",
            "employers",
            "senator",
            "rumbles",
            "sweets"
        )
        for (item in names) {
            val group = Group(item)
            group.language = Constants.LANGUAGE_EN
            list.add(group)
        }
    }

    fun getGroups() = dao.getAll(GamePrefs.LANGUAGE, GamePrefs.ASSORTMENT_GROUPS_COUNT)

    fun updateLastUsedTime(groups: List<Group>) {
        for (group in groups) {
            group.lastUsedTime = System.currentTimeMillis()
        }
        dao.update(groups)
    }

    fun getNamesOfGroups(groups: List<Group>): ArrayList<String> {
        val names = arrayListOf<String>()
        for (group in groups) {
            names.add(group.value)
        }
        return names
    }
}