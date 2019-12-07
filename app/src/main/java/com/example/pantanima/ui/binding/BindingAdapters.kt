package com.example.pantanima.ui.binding

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.pantanima.R
import com.example.pantanima.ui.customviews.VerticalSliderView
import com.google.android.material.textfield.TextInputEditText


@BindingAdapter("android:visibility")
fun View.setVisibility(value: Boolean) {
    visibility = if (value) View.VISIBLE else View.GONE
}

@BindingAdapter(
    value = ["android:textChangeObserver", "android:textIsMandatory"],
    requireAll = false
)
fun TextInputEditText.textListener(value: ObservableField<String>, textIsMandatory: Boolean?) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(newText: CharSequence?, start: Int, before: Int, count: Int) {
            value.set(newText.toString())
        }

        override fun afterTextChanged(p0: Editable?) {
            val count = p0!!.length
            error = if (textIsMandatory != null && textIsMandatory && count == 0) {
                context.getString(R.string.group_name_min_limit_desc)
            } else {
                null
            }
        }
    })
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
