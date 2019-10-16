package com.example.pantanima.ui.binding

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.pantanima.ui.customviews.VerticalSliderView


@BindingAdapter("android:visibility")
fun View.setVisibility(value: Boolean) {
    visibility = if (value) View.VISIBLE else View.GONE
}

@BindingAdapter("android:textChangeObserver")
fun EditText.textListener(value: ObservableField<String>) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {
            value.set(newText.toString())
        }

        override fun afterTextChanged(p0: Editable?) {

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
