package com.example.pantanima.ui.customviews

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
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

    private var chooserBtnStartMargin = 30.0f
    private var yDelta: Int = 0
    private var variantsTvSize = 30f
    private var chooserBtnSize = 60f
    private var variantsTvPadding = 10
    private var variantsTvColor = Color.BLACK
    private var chooserBtnColor = Color.RED

    lateinit var variantsContainer: LinearLayout
    lateinit var chooserButton: Button

    var listTv: MutableList<TextView> = ArrayList()

    var listStr: List<String> = ArrayList()
        set(value) {
            field = value
            for (text in field) {
                listTv.add(drawNewTv(text))
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
                chooserBtnStartMargin = getDimension(
                    R.styleable.VerticalSliderView_sliderChooserBtnStartMargin,
                    chooserBtnStartMargin
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
        lp.marginStart = chooserBtnStartMargin.toInt()
        lp.addRule(CENTER_VERTICAL, TRUE)
        chooserButton.layoutParams = lp
        addView(chooserButton)
    }

    private fun drawNewTv(str: String): TextView {
        return TextView(context).apply {
            text = str
            textSize = variantsTvSize
            setTextColor(variantsTvColor)
            setPadding(variantsTvPadding, variantsTvPadding, variantsTvPadding, variantsTvPadding)
            variantsContainer.addView(this)
        }
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

                            val btnHalfHeight = (view.bottom - view.top) / 2
                            val binMidCoordinates = newY + btnHalfHeight
                            val posAndScale = getCurrentFocusedPosition(binMidCoordinates.toInt())
                            val index = posAndScale.first
                            val scaleXY = posAndScale.second
                            if (index != -1) {
                                val tv = listTv[index]
                                tv.scaleX = 1 + (scaleXY / 4)
                                tv.scaleY = 1 + (scaleXY / 4)
                                tv.translationX = scaleXY * 30
                            }
                        }
                    }
                }
            }
            invalidate()
            true
        }
    }

    private fun getCurrentFocusedPosition(chooserMid: Int): Pair<Int, Float> {
        val totalHeight = variantsContainer.height
        val oneItemHeight = totalHeight / listStr.size
        for (position in listStr.indices) {
            val top = height - (height - position * oneItemHeight)
            val bottom = top + oneItemHeight
            if (chooserMid in (top + 1) until bottom) {
                val scalePercent = getItemScale(top, bottom, chooserMid)
                return Pair(position, scalePercent)
            }
        }
        return Pair(-1, 0f) //default
    }

    private fun getItemScale(top: Int, bottom: Int, chooserMid: Int): Float {
        val height = bottom - top
        val itemMid = height / 2

        val rawMid = bottom - itemMid
        val diff = if (rawMid > chooserMid) {  //up from mid
            rawMid - chooserMid
        } else {  //bottom from mid
            chooserMid - rawMid
        }

        val scalePercent = itemMid - diff
        val result = scalePercent * 100.0f / itemMid
        return result * 0.01f
    }
}