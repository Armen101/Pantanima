package com.example.pantanima.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

object AppLinearSnapHelper {

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
}