package com.example.pantanima.ui

import com.example.pantanima.ui.models.Group

class GroupManager(private val names: ArrayList<String>?) {

    var groups: MutableList<Group> = ArrayList()

    private var currentGroup: Group? = null

    init {
        prepareGroups()
    }

    private fun prepareGroups() {
        names?.forEach {
            groups.add(Group(it))
        }
        currentGroup = groups[0]
    }

    fun incAnsweredCount() = currentGroup?.incAnsweredCount()

    fun decAnsweredCount() = currentGroup?.decAnsweredCount()

    fun switchGroup() {
        currentGroup?.saveStatistics()
        val currentGroupIndex = groups.indexOf(currentGroup)
        currentGroup = if (currentGroupIndex + 1 == groups.size) {
            groups[0]
        } else {
            groups[currentGroupIndex + 1]
        }
    }

    fun resetState() {
        groups.clear()
        prepareGroups()
    }


}