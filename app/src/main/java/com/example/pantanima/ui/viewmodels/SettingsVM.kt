package com.example.pantanima.ui.viewmodels

import android.view.View
import androidx.databinding.ObservableField
import com.example.pantanima.ui.DisplayHelper
import com.example.pantanima.ui.activities.NavActivity
import com.example.pantanima.ui.adapters.SliderAdapter
import com.example.pantanima.ui.adapters.SliderLayoutManager
import com.example.pantanima.ui.customviews.VerticalSliderView
import timber.log.Timber
import java.lang.ref.WeakReference

class SettingsVM(activity: WeakReference<NavActivity>) : BaseVM(activity) {

    val adapterStartEndPadding = DisplayHelper.displayWidth() / 2 - DisplayHelper.dpToPx(40)

    var timeLayoutManager = ObservableField(SliderLayoutManager(activity.get()))
    private val timePikerData = getTimePickerData()
    var timeSliderAdapter = ObservableField(SliderAdapter(timePikerData))
    var timeInitialPosition = 7
    var timeChooseText = ObservableField<String>(timePikerData[timeInitialPosition])

    var scoreLayoutManager = ObservableField(SliderLayoutManager(activity.get()))
    private val scorePikerData = getScorePickerData()
    var scoreSliderAdapter = ObservableField(SliderAdapter(scorePikerData))
    var scoreInitialPosition = 80
    var scoreChooseText = ObservableField<String>(scorePikerData[scoreInitialPosition])
    var modePositionChangeListener = object : VerticalSliderView.OnCursorPositionChangeListener {
        override fun onChanged(newPosition: Int) {
            Timber.d("newPosition : $newPosition")
        }
    }

    init {
        timeLayoutManager.get()?.scrollToPosition(timeInitialPosition)
        scoreLayoutManager.get()?.scrollToPosition(scoreInitialPosition)
        initTimePicker()
        initScorePicker()
    }

    private fun initScorePicker() {

        scoreLayoutManager.get()?.apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    scoreChooseText.set(scorePikerData[layoutPosition])
                }
            }
        }

        scoreSliderAdapter.get()?.apply {
            callback = object : SliderAdapter.Callback {
                override fun onItemClicked(view: View) {
                    //todo
                }
            }
        }
    }

    fun getModePickerData(): ArrayList<String> {
        val data = ArrayList<String>()
        data.add("light")
        data.add("medium")
        data.add("hard")
        data.add("famous")
        return data
    }

    private fun getTimePickerData(): ArrayList<String> {
        val data = ArrayList<String>()
        for (x in 2..16) {
            data.add((x * 10).toString())
        }
        return data
    }

    private fun getScorePickerData(): ArrayList<String> {
        val data = ArrayList<String>()
        for (x in 20..160) {
            data.add(x.toString())
        }
        return data
    }

    private fun initTimePicker() {

        timeLayoutManager.get()?.apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    timeChooseText.set(timePikerData[layoutPosition])
                }
            }
        }

        timeSliderAdapter.get()?.apply {
            callback = object : SliderAdapter.Callback {
                override fun onItemClicked(view: View) {
                    //todo
                }
            }
        }
    }

    fun onConfirmClick() {
        //todo save settings
        activity?.onBackPressed()
    }
}