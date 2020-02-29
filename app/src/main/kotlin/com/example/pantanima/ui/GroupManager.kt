package com.example.pantanima.ui

import com.example.pantanima.ui.models.Group

class GroupManager {

    var groups: MutableList<Group> = ArrayList()

    private var currentGroup: Group ?= null

    fun incAnsweredCount() = currentGroup?.incAnsweredCount()

    fun decAnsweredCount() = currentGroup?.decAnsweredCount()

    fun setGroup() {
        currentGroup = groups[0]
    }

    fun switchGroup() {
        currentGroup?.saveStatistics()
        val currentGroupIndex = groups.indexOf(currentGroup)
        currentGroup = if (currentGroupIndex + 1 == groups.size) {
            groups[0]
        } else {
            groups[currentGroupIndex + 1]
        }
    }

}