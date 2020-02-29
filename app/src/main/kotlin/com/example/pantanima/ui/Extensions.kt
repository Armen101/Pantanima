package com.example.pantanima.ui

import androidx.databinding.ObservableField

fun <T> ObservableField<T>.getIfNotNull(linking: (T) -> Unit) {
    val value = get()
    if (value != null) {
        linking(value)
    }
}
