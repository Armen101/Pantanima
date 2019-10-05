package com.example.pantanima.ui.binding

import android.graphics.Color
import android.graphics.PointF
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.utils.MiscUtils
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.ColorFilter
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.airbnb.lottie.TextDelegate
import com.airbnb.lottie.value.LottieFrameInfo
import com.airbnb.lottie.value.SimpleLottieValueCallback


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
