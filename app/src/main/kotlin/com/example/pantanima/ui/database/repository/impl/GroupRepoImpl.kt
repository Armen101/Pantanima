package com.example.pantanima.ui.database.repository.impl

import com.example.pantanima.ui.database.dao.GroupDao
import com.example.pantanima.ui.database.entity.Group
import com.example.pantanima.ui.database.repository.GroupRepo
import com.example.pantanima.ui.helpers.GamePrefs

class GroupRepoImpl (private val dao: GroupDao): GroupRepo {

    override fun getGroups() = dao.getAll(GamePrefs.ASSORTMENT_GROUPS_COUNT)

    override fun getGroups(withoutList: List<String>): MutableList<Group>
            = dao.getAll(withoutList, GamePrefs.ASSORTMENT_GROUPS_COUNT)

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
            names.add(group.name)
        }
        return names
    }
}