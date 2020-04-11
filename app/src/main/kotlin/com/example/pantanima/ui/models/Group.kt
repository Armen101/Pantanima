package com.example.pantanima.ui.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Group(
    var name: String,
    var roundAnsweredCount: Int = 0,
    var statistics: MutableList<Int> = ArrayList()
) : Parcelable {

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

    fun getAnsweredCount(): Int {
        var total = 0
        statistics.forEach { roundScore ->
            total += roundScore
        }
        return total
    }

}