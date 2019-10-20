package com.example.pantanima.ui.customviews

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.pantanima.R
import android.view.MotionEvent
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.pantanima.ui.adapters.ScrollHelper
import kotlin.math.roundToInt


class VerticalSliderView : RelativeLayout {

    private var cursorInitialIndex: Int = 1
    private var chooserBtnStartMargin = 0.0f
    private var yDelta: Int = 0
    private var variantsTvSize = 30f
    private var chooserBtnSize = 60f
    private var variantsTvPadding = 10
    private var variantsTvColor = Color.BLACK
    private var chooserBtnColor = Color.RED
    private var variantsTvInitialAlpha = 0.75f
    private var focusedVariant: TextView? = null

    private lateinit var variantsContainer: LinearLayout
    lateinit var cursorButton: Button
    private var previousIndex = cursorInitialIndex


    private var oneItemHeight: Int = 0
        get() {
            if (field <= 0) {
                val totalHeight = variantsContainer.height
                field = totalHeight / listStr.size
            }
            return field
        }

    var listTv: MutableList<TextView> = ArrayList()

    var listStr: List<String> = ArrayList()
        set(value) {
            field = value
            for (index in field.indices) {
                listTv.add(drawNewTv(field[index], index))
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
                    getDimension(R.styleable.VerticalSliderView_cursorVariantTvSize, variantsTvSize)
                variantsTvPadding = getDimension(
                    R.styleable.VerticalSliderView_cursorVariantTvPadding,
                    variantsTvPadding.toFloat()
                ).toInt()
                variantsTvColor =
                    getColor(R.styleable.VerticalSliderView_cursorVariantTvColor, variantsTvColor)
                chooserBtnColor =
                    getColor(R.styleable.VerticalSliderView_cursorChooserBtnColor, chooserBtnColor)
                chooserBtnSize = getDimension(
                    R.styleable.VerticalSliderView_cursorChooserBtnSize,
                    chooserBtnSize
                )
                cursorInitialIndex = getInt(
                    R.styleable.VerticalSliderView_cursorInitialIndex, cursorInitialIndex
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
        cursorButton = Button(context)

        val drawable = ContextCompat.getDrawable(context, R.drawable.start_round_button_bg)
        drawable?.let {
            val drawableCompat = DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(drawableCompat, chooserBtnColor)
            cursorButton.background = drawable
        }

        cursorButton.setOnTouchListener(onTouchListener())
        val lp = LayoutParams(chooserBtnSize.toInt(), chooserBtnSize.toInt())
        lp.addRule(END_OF, variantsContainer.id)
        lp.marginStart = chooserBtnStartMargin.toInt()
        cursorButton.layoutParams = lp
        addView(cursorButton)
    }

    private fun drawNewTv(str: String, index: Int): TextView {
        return TextView(context).apply {
            text = str
            textSize = variantsTvSize
            val color = if (cursorInitialIndex != index) {
                Color.BLACK
            } else {
                variantsTvColor
            }
            setTextColor(color)
            alpha = variantsTvInitialAlpha

            val typeface = ResourcesCompat.getFont(context, R.font.caviar_dreams_bold)
            setTypeface(typeface)

            setOnClickListener {
                moveCursorTo(index)
                ScrollHelper.playScrollSound(context)
                toFocus(index)
            }

            setPadding(
                variantsTvPadding,
                variantsTvPadding / 2,
                variantsTvPadding * 3,
                variantsTvPadding / 2
            )

            val lp = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            layoutParams = lp

            variantsContainer.addView(this)
        }
    }


    private fun onTouchListener(): OnTouchListener {
        var cursorMid = 0f
        return OnTouchListener { view, event ->
            val rawY = event.rawY.toInt()
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> yDelta = (view.y - rawY).toInt()
                MotionEvent.ACTION_UP -> cursorToCenter(cursorMid)
                MotionEvent.ACTION_POINTER_DOWN -> {
                }
                MotionEvent.ACTION_POINTER_UP -> {
                }
                MotionEvent.ACTION_MOVE -> {
                    val transY = rawY.toFloat()
                    val newY = transY + yDelta
                    if (newY > 0) {
                        if (view.height + newY < bottom) {
                            view.y = newY

                            val btnHalfHeight = (view.bottom - view.top) / 2
                            cursorMid = newY + btnHalfHeight
                            Log.d("actionMove", "cursorMid   : $cursorMid  ---------------------------")

                            val posAndScale = getCurrentFocusedPosition(cursorMid.toInt())
                            val index = posAndScale.first
                            val scaleXY = posAndScale.second
                            if (index != -1) {
                                if (previousIndex != index) {
                                    previousIndex = index
                                    ScrollHelper.playScrollSound(view.context)
                                }
                                toFocus(index, scaleXY)
                            }
                        }
                    }
                }
            }
            invalidate()
            true
        }
    }

    private fun manipulateColor(color: Int, factor: Float): Int {
        val a = Color.alpha(color)
        val r = (Color.red(color).toFloat().roundToInt() * factor).toInt()
        val g = (Color.green(color).toFloat().roundToInt() * factor).toInt()
        val b = (Color.blue(color).toFloat().roundToInt() * factor).toInt()
        return Color.argb(a, r, g, b)
    }

    private fun toFocus(index: Int, scale: Float = 1f) {
        val color = manipulateColor(variantsTvColor, scale)
        focusedVariant = listTv[index]
        focusedVariant?.apply {
            setTextColor(color)
            scaleX = 1 + (scale / 4)
            scaleY = 1 + (scale / 4)
            translationX = scale * 35
        }
        for (i in listTv.indices) {
            if (i != index) {
                toSecondary(listTv[i])
            }
        }
    }

    private fun toSecondary(tv: TextView) {
        tv.apply {
            scaleX = 1f
            scaleY = 1f
            translationX = 0f
            alpha = variantsTvInitialAlpha
            setTextColor(Color.BLACK)
        }
    }

    private fun moveCursorTo(index: Int) {
        val cursorItemHalfHeight = (cursorButton.bottom - cursorButton.top) / 2
        if (index == -1) {
            return
        }
        val currentItemMid = ((index + 1) * oneItemHeight) - (oneItemHeight / 2)

        val cursorMid = cursorButton.y - (cursorButton.height / 2)
        val translationDiff = if (cursorMid > currentItemMid) { //to Up
            -(cursorMid - currentItemMid)
        } else { //to Down or center
            (currentItemMid - cursorMid)
        }

        val translationY = cursorMid + translationDiff

        Log.d("moveCursorTo", "cursorMid      : $cursorMid ---------------------------")
        Log.d("moveCursorTo", "itemIndex      : $index")
        Log.d("moveCursorTo", "oneItemHeight  : $oneItemHeight")
        Log.d("moveCursorTo", "currentItemMid : $currentItemMid")
        Log.d("moveCursorTo", "translationY   : $translationY")

        cursorButton.animate().translationY(translationY - cursorItemHalfHeight).duration = 50
    }

    private fun cursorToCenter(cursorMid: Float) {
        val itemIndex = getCurrentFocusedPosition(cursorMid.toInt()).first
        Log.d("cursorToCenter", "cursorButton.y      : ${cursorButton.y} ---------------------------")
        Log.d("cursorToCenter", "cursorButton.height : ${cursorButton.height}")
        Log.d("cursorToCenter", "cursorMid           : $cursorMid")
        moveCursorTo(itemIndex)
    }

    private fun getCurrentFocusedPosition(cursorMid: Int): Pair<Int, Float> {
        Log.d("getPosition", "cursorMid   : $cursorMid  ---------------------------")
        for (position in listStr.indices) {
            val top = height - (height - (position * oneItemHeight))
            val bottom = top + oneItemHeight

            Log.d("getPosition", "top          : $top")
            Log.d("getPosition", "bottom       : $bottom")

            if (cursorMid in top + 1 until bottom) {
                var scalePercent = getItemScale(top, bottom, cursorMid)
                if (scalePercent < 0f) {
                    scalePercent = 0f
                }
                Log.d("getPosition", "position     : $position")
                Log.d("getPosition", "scalePercent : $scalePercent")
                return Pair(position, scalePercent)
            }
        }
        Log.d("getPosition", "return def values -> Pair(-1, 0f)")

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