package com.example.pantanima.ui.binding

import android.view.View
import androidx.databinding.BindingAdapter


@BindingAdapter("android:visibility")
fun View.setVisibility(value: Boolean) {
    visibility = if (value) View.VISIBLE else View.GONE
}