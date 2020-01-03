package com.example.pantanima.ui.binding

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.pantanima.ui.customviews.SwipeRevealLayout
import com.example.pantanima.ui.customviews.VerticalSliderView

@BindingAdapter("android:visibility")
fun View.setVisibility(value: Boolean) {
    visibility = if (value) View.VISIBLE else View.GONE
}

@BindingAdapter("scroll")
fun SwipeRevealLayout.listener(v: View) {
    setSwipeListener(object : SwipeRevealLayout.SimpleSwipeListener() {
        override fun onSlide(view: SwipeRevealLayout?, slideOffset: Float) {
            super.onSlide(view, slideOffset)
            v.alpha = slideOffset
            val nexX = v.width - (v.width * slideOffset)
            v.animate().translationX(-nexX).duration = 0
            v.scaleX = slideOffset
            v.scaleY = slideOffset
        }

        override fun onClosed(view: SwipeRevealLayout?) {
            super.onClosed(view)
            v.alpha = 0.0f
            v.scaleX = 0.0f
            v.scaleY = 0.0f
            v.animate().translationX(v.width.toFloat()).duration = 0
        }

        override fun onOpened(view: SwipeRevealLayout?) {
            super.onOpened(view)
            v.alpha = 1.0f
            v.scaleX = 1.0f
            v.scaleY = 1.0f
            v.animate().translationX(0f).duration = 0
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
