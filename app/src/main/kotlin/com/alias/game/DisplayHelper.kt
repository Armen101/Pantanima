package com.example.pantanima.ui

import android.content.res.Resources

object DisplayHelper {

    fun Int.dpToPx() = (this * Resources.getSystem().displayMetrics.density).toInt()

    fun Int.pxToDp() = (this / Resources.getSystem().displayMetrics.density).toInt()

    fun displayHeight() = Resources.getSystem().displayMetrics.heightPixels

    fun displayWidth() = Resources.getSystem().displayMetrics.widthPixels

}