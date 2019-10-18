package com.example.pantanima.ui.adapters

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

object ScrollHelper {

    fun getSnapPosition(snagHelper: SnapHelper, recyclerView: RecyclerView): Int {
        val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
        val snapView = snagHelper.findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
        return layoutManager.getPosition(snapView)
    }

    fun attachSnapHelperWithListener(
        rv: RecyclerView,
        snapHelper: SnapHelper,
        behavior: SnapOnScrollListener.Behavior = SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
        onSnapPositionChangeListener: OnSnapPositionChangeListener
    ) {
        snapHelper.attachToRecyclerView(rv)
        val snapOnScrollListener =
            SnapOnScrollListener(snapHelper, behavior, onSnapPositionChangeListener)
        rv.addOnScrollListener(snapOnScrollListener)
    }

    fun playScrollSound(context: Context) {
        val resID = context.resources?.getIdentifier(
            "scroll_sound_effect",
            "raw", context.packageName
        )
        vibrate(context)
        resID?.let {
            val clickPlayer = MediaPlayer.create(context, it)
            clickPlayer?.start()
        }
    }

    @Suppress("DEPRECATION")
    private fun vibrate(context: Context) {
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        if (Build.VERSION.SDK_INT >= 26) {
            val effect: VibrationEffect = VibrationEffect.createOneShot(2, VibrationEffect.DEFAULT_AMPLITUDE)
            v?.vibrate(effect)
        } else {
            v?.vibrate(2)
        }
    }

}