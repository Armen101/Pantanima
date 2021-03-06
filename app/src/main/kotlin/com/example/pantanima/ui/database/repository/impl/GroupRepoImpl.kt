package com.example.pantanima.ui.database.repository.impl

import com.example.pantanima.ui.Constants
import com.example.pantanima.ui.database.dao.GroupDao
import com.example.pantanima.ui.database.entity.Group
import com.example.pantanima.ui.database.repository.GroupRepo
import com.example.pantanima.ui.helpers.GamePrefs

class GroupRepoImpl (private val dao: GroupDao): GroupRepo {

    override fun insertInitialGroups() {
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

    override fun getGroups() = dao.getAll(GamePrefs.LANGUAGE, GamePrefs.ASSORTMENT_GROUPS_COUNT)

    override fun getGroups(withoutList: List<String>): MutableList<Group>
            = dao.getAll(GamePrefs.LANGUAGE, withoutList, GamePrefs.ASSORTMENT_GROUPS_COUNT)

    override fun updateLastUsedTime(groups: List<Group>) {
        groups.forEach {
            it.lastUsedTime = System.currentTimeMillis()
        }
        dao.update(groups)
    }

    override fun updateLastUsedTime(group: Group) {
        group.lastUsedTime = System.currentTimeMillis()
        dao.update(group)
    }

    override fun getNamesOfGroups(groups: List<Group>): ArrayList<String> {
        val names = arrayListOf<String>()
        for (group in groups) {
            names.add(group.value)
        }
        return names
    }
}