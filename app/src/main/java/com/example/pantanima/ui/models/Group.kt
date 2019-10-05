package com.example.pantanima.ui.models

class Group(var name: String) {

    var roundAnsweredCount: Int = 0
    var statistics: MutableList<Int> = ArrayList()
        private set

    fun saveStatistics() {
        val ss = roundAnsweredCount
        statistics.add(ss)
        roundAnsweredCount = 0
    }

    fun incAnsweredCount() {
        roundAnsweredCount = roundAnsweredCount.inc()
    }

    fun decAnsweredCount() {
        roundAnsweredCount = roundAnsweredCount.dec()
    }

}