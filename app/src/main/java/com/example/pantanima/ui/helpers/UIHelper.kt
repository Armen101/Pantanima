package com.example.pantanima.ui.helpers

import android.os.Handler
import android.view.Gravity
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.example.pantanima.R
import io.github.douglasjunior.androidSimpleTooltip.ArrowDrawable
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip

object UIHelper {

    fun drawTooltip(view: View, @StringRes res: Int, asError: Boolean = false) {

        val arrowWidth = view.context.resources.getDimensionPixelSize(R.dimen._14sdp)
        val arrowHeight = view.context.resources.getDimensionPixelSize(R.dimen._10sdp)

        val color = ContextCompat.getColor(view.context, if (asError) {
            R.color.app_style_red
        } else {
            R.color.app_style_blue
        })

        val layoutRes = if (asError) {
            R.layout.custom_error_snack_bar
        } else {
            R.layout.custom_snack_bar
        }

        val br = SimpleTooltip.Builder(view.context)
            .anchorView(view)
            .gravity(Gravity.TOP)
            .arrowDirection(ArrowDrawable.BOTTOM)
            .dismissOnOutsideTouch(true)
            .text(res)
            .arrowWidth(arrowWidth.toFloat())
            .arrowHeight(arrowHeight.toFloat())
            .arrowColor(color)
            .showArrow(true)
            .dismissOnOutsideTouch(true)
            .dismissOnInsideTouch(true)
            .contentView(layoutRes)
            .animated(false)
            .padding(R.dimen._10sdp)
            .margin(R.dimen._1sdp)
            .transparentOverlay(true)
            .build()

        br.show()

        Handler().postDelayed({
            br.dismiss()
        }, 3000)

    }
}