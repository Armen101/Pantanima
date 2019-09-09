package com.example.pantanima.ui

import com.example.pantanima.ui.models.Group

object GroupManager {

    var groups: List<Group> = ArrayList()

    private lateinit var currentGroup: Group

    fun incAnsweredCount() = currentGroup.roundAnsweredCount.inc()

    fun decAnsweredCount() = currentGroup.roundAnsweredCount.dec()

    fun getAnsweredCount() = currentGroup.roundAnsweredCount

    fun switchGroup() {
        currentGroup.saveStatistics()
        val currentGroupIndex = groups.indexOf(currentGroup)
        currentGroup = if (currentGroupIndex + 1 == groups.size) {
            groups[0]
        } else {
            groups[currentGroupIndex + 1]
        }
    }

}