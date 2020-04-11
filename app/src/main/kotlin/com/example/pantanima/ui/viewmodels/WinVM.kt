package com.example.pantanima.ui.viewmodels

import android.app.Application
import androidx.databinding.ObservableField
import com.example.pantanima.ui.adapters.WinAdapter
import com.example.pantanima.ui.models.Group

class WinVM(app: Application, groups: List<Group>) : BaseVM(app) {

    val adapter = ObservableField(WinAdapter())
    val winGroupName = ObservableField("")

    init {
        val sortedList = toSort(groups)
        adapter.get()?.data = sortedList
        winGroupName.set(sortedList[0].name)
    }

    fun playAgain() {

    }

    fun exit() {

    }

    private fun toSort(groups: List<Group>): List<Group> {
        return groups.asSequence()
            .sortedBy { it.getAnsweredCount() }
            .toMutableList()
            .reversed()
    }
}