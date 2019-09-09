package com.example.pantanima.ui.models

class Group {

    var name: String = ""

    var roundAnsweredCount: Int = 0

    fun saveStatistics(){
        statistics.add(roundAnsweredCount)
        roundAnsweredCount = 0
    }

    private var statistics: MutableList<Int> = ArrayList()
}