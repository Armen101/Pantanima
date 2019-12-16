package com.example.pantanima.ui.binding

import android.view.View
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.pantanima.R
import com.example.pantanima.ui.customviews.VerticalSliderView
import com.example.pantanima.ui.helpers.UIHelper

@BindingAdapter("android:visibility")
fun View.setVisibility(value: Boolean) {
    visibility = if (value) View.VISIBLE else View.GONE
}

@BindingAdapter("android:textChangeObserver")
fun EditText.textListener(value: ObservableField<String>) {
    doOnTextChanged { text, _, _, _ ->
            value.set(text.toString())
            if (text?.length == 0) {
                UIHelper.drawTooltip(this, R.string.group_name_max_limit_desc)
            }
        }
}

@BindingAdapter("android:lottie_play")
fun LottieAnimationView.play(boolean: Boolean) {
    if (boolean) {
        playAnimation()
    } else {
        cancelAnimation()
    }
}

@BindingAdapter("initialPosition")
fun RecyclerView.scrollTo(pos: Int) {
    scrollToPosition(pos)
}

@BindingAdapter("list")
fun VerticalSliderView.setList(list: List<String>) {
    listStr = list
}

@BindingAdapter("cursorPositionChangeListener")
fun VerticalSliderView.cursorPositionChangeListener(
    listener: VerticalSliderView.OnCursorPositionChangeListener
) {
    setOnCursorPositionChangedListener(listener)
}

@BindingAdapter("cursorInitialPosition")
fun VerticalSliderView.cursorInitialPosition(index: Int) {
    setCursorInitialIndex(index)
}
