package com.example.pantanima.ui

import android.content.res.Resources

object DisplayHelper {

    fun dpToPx(dp: Int) = (dp * Resources.getSystem().displayMetrics.density).toInt()

    fun pxToDp(px: Int) = (px / Resources.getSystem().displayMetrics.density).toInt()

    fun displayHeight() = Resources.getSystem().displayMetrics.heightPixels

    fun displayWidth() = Resources.getSystem().displayMetrics.widthPixels

}