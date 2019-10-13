package com.example.pantanima.ui.viewmodels

import android.view.View
import androidx.databinding.ObservableField
import com.example.pantanima.ui.DisplayHelper
import com.example.pantanima.ui.activities.NavActivity
import com.example.pantanima.ui.adapters.SliderAdapter
import com.example.pantanima.ui.adapters.SliderLayoutManager
import java.lang.ref.WeakReference

class SettingsVM(activity: WeakReference<NavActivity>) : BaseVM(activity) {

    private val timeLm = SliderLayoutManager(activity.get())
    var timeLayoutManager = ObservableField(timeLm)
    private val sliderAdapter = SliderAdapter()
    var timeSliderAdapter = ObservableField(sliderAdapter)
    val timeSliderAdapterPadding: Int = DisplayHelper.displayWidth() / 2 - DisplayHelper.dpToPx(40)
    var chooseTimeText = ObservableField<String>()

    private val data = getTimePickerData()

    init {
        setHorizontalPicker()
    }

    private fun getTimePickerData(): ArrayList<String> {
        val data = ArrayList<String>()
        for (x in 1..20) {
            data.add((x * 10).toString())
        }
        return data
    }

    private fun setHorizontalPicker() {

        timeLm.apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    chooseTimeText.set(data[layoutPosition])
                }
            }
        }

        sliderAdapter.apply {
            setData(data)
            callback = object : SliderAdapter.Callback {
                override fun onItemClicked(view: View) {
                   //todo
                }
            }
        }
    }
}