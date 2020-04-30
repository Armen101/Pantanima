package com.example.pantanima.ui.database.repository

import com.example.pantanima.ui.database.entity.Group

interface GroupRepo {

    fun updateLastUsedTime(group: Group)

    fun updateLastUsedTime(groups: List<Group>)

    fun getGroups(): MutableList<Group>

    fun getGroups(withoutList: List<String>): MutableList<Group>

    fun getNamesOfGroups(groups: List<Group>): ArrayList<String>

}