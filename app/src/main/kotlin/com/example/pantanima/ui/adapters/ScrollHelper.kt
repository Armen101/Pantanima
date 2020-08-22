package com.example.pantanima.ui.adapters

import android.content.Context
import android.media.AudioManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import android.media.ToneGenerator
import android.os.*
import timber.log.Timber
import java.lang.Exception

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

    private fun createPlayer() {
        try {
            val gen = ToneGenerator(AudioManager.STREAM_MUSIC, 50)
            gen.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_INTERGROUP, 5)
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                gen.release()
            }, 100L)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun playScrollSound(context: Context) {
        vibrate(context)
        createPlayer()
    }

    @Suppress("DEPRECATION")
    private fun vibrate(context: Context) {
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        if (Build.VERSION.SDK_INT >= 26) {
            val effect: VibrationEffect =
                VibrationEffect.createOneShot(2, VibrationEffect.DEFAULT_AMPLITUDE)
            v?.vibrate(effect)
        } else {
            v?.vibrate(2)
        }
    }

}