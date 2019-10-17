package com.example.pantanima.ui.customviews

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.FocusFinder
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.pantanima.R
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat


class VerticalSliderView : RelativeLayout {

    private var yDelta: Int = 0
    private var variantsTvSize = 30f
    private var chooserBtnSize = 60f
    private var variantsTvPadding = 20
    private var variantsTvColor = Color.BLACK
    private var chooserBtnColor = Color.RED

    lateinit var variantsContainer: LinearLayout
    lateinit var chooserButton: Button

    var listStr: List<String> = ArrayList()
        set(value) {
            field = value
            for (text in field) {
                drawNewTv(text)
            }
        }

    constructor(context: Context) : super(context) {
        draw()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
        draw()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
        draw()
    }

    private fun draw() {
        drawVariantsContainer()
        drawChooserButton()
    }

    private fun init(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.VerticalSliderView, 0, 0
        ).apply {
            try {
                variantsTvSize =
                    getDimension(R.styleable.VerticalSliderView_sliderVariantTvSize, variantsTvSize)
                variantsTvPadding = getDimension(
                    R.styleable.VerticalSliderView_sliderVariantTvPadding,
                    variantsTvPadding.toFloat()
                ).toInt()
                variantsTvColor =
                    getColor(R.styleable.VerticalSliderView_sliderVariantTvColor, variantsTvColor)
                chooserBtnColor =
                    getColor(R.styleable.VerticalSliderView_sliderChooserBtnColor, chooserBtnColor)
                chooserBtnSize = getDimension(
                    R.styleable.VerticalSliderView_sliderChooserBtnSize,
                    chooserBtnSize
                )
            } finally {
                recycle()
            }
        }
    }

    private fun drawVariantsContainer() {
        variantsContainer = LinearLayout(context)
        variantsContainer.id = R.id.automatic
        variantsContainer.orientation = LinearLayout.VERTICAL
        val lp = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        variantsContainer.layoutParams = lp
        addView(variantsContainer)
    }

    private fun drawChooserButton() {
        chooserButton = Button(context)

        val drawable = ContextCompat.getDrawable(context, R.drawable.start_round_button_bg)
        drawable?.let {
            val drawableCompat = DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(drawableCompat, chooserBtnColor)
            chooserButton.background = drawable
        }

        chooserButton.setOnTouchListener(onTouchListener())
        val lp = LayoutParams(chooserBtnSize.toInt(), chooserBtnSize.toInt())
        lp.addRule(END_OF, variantsContainer.id)
        lp.addRule(CENTER_VERTICAL, TRUE)
        chooserButton.layoutParams = lp
        addView(chooserButton)
    }

    private fun drawNewTv(text: String) {
        val tv = TextView(context)
        tv.text = text
        tv.textSize = variantsTvSize
        tv.setTextColor(variantsTvColor)
        tv.setPadding(variantsTvPadding, variantsTvPadding, variantsTvPadding, variantsTvPadding)
        variantsContainer.addView(tv)
    }


    private fun onTouchListener(): OnTouchListener {
        return OnTouchListener { view, event ->
            val rawY = event.rawY.toInt()
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    yDelta = (view.y - rawY).toInt()
                }
                MotionEvent.ACTION_UP -> {
                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                }
                MotionEvent.ACTION_POINTER_UP -> {
                }
                MotionEvent.ACTION_MOVE -> {
                    val transY = rawY.toFloat()
                    val newY = transY + yDelta
                    if (newY > top) {
                        if (view.height + newY < bottom) {
                            view.y = newY
                            getCurrentFocusedPosition(newY)
                        }
                    }
                }
            }
            invalidate()
            true
        }
    }

    private fun getCurrentFocusedPosition(rawY: Float): Pair<Int, Float> {
        val totalHeight = variantsContainer.height
        val oneItemHeight = totalHeight / listStr.size
        for (i in listStr.indices) {
            val top = height - (height - i * oneItemHeight)
            val bottom = top + oneItemHeight
            if (top < rawY && bottom > rawY) {
                val scalePercent = getItemScale(top, bottom, rawY)
                Log.d("====p", "pos: $i, scalePercent $scalePercent")
                return Pair(i, 0f)
            }
        }
        return Pair(-1, 0f) //default
    }

    private fun getItemScale(top: Int, bottom: Int, rawY: Float): Float {

        val height = bottom - top
        val mid = height / 2
        val diff = Math.max(mid, rawY.toInt()) - Math.min(mid, rawY.toInt())

        var scalePercent = (diff * 100 / height) - 100

//        if (rawY > mid) {
//            scalePercent = 1 - scalePercent
//        }

        return scalePercent.toFloat()
    }
}