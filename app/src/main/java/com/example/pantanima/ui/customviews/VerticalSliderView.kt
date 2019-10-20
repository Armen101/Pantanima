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
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.pantanima.ui.adapters.ScrollHelper
import timber.log.Timber
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt


class VerticalSliderView : RelativeLayout {

    private var cursorInitialIndex: Int = 1
    private var cursorBtnStartMargin = 0.0f
    private var yDelta: Int = 0
    private var tvSize = 30f
    private var cursorBtnSize = 60f
    private var tvPadding = 10
    private var tvColor = Color.BLACK
    private var cursorBtnColor = Color.RED
    private var focusedTv: TextView? = null

    private lateinit var variantsContainer: LinearLayout
    private lateinit var cursorBtn: Button
    private var cursorIndex = cursorInitialIndex
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
                val tv = drawNewTv(field[index], index)
                variantsContainer.addView(tv)
                listTv.add(tv)
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
        drawVerticalListContainer()
        drawCursorButton()
    }

    private fun init(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.VerticalSliderView, 0, 0
        ).apply {
            try {
                tvSize =
                    getDimension(R.styleable.VerticalSliderView_cursorVariantTvSize, tvSize)
                tvPadding = getDimension(
                    R.styleable.VerticalSliderView_cursorVariantTvPadding,
                    tvPadding.toFloat()
                ).toInt()
                tvColor = getColor(R.styleable.VerticalSliderView_cursorVariantTvColor, tvColor)
                cursorBtnColor =
                    getColor(R.styleable.VerticalSliderView_cursorChooserBtnColor, cursorBtnColor)
                cursorBtnSize = getDimension(
                    R.styleable.VerticalSliderView_cursorChooserBtnSize,
                    cursorBtnSize
                )
                cursorInitialIndex = getInt(
                    R.styleable.VerticalSliderView_cursorInitialIndex, cursorInitialIndex
                )
            } finally {
                recycle()
            }
        }
    }

    private fun drawVerticalListContainer() {
        variantsContainer = LinearLayout(context)
        variantsContainer.id = R.id.automatic
        variantsContainer.orientation = LinearLayout.VERTICAL
        val lp = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        variantsContainer.layoutParams = lp
        addView(variantsContainer)
    }

    private fun drawCursorButton() {
        cursorBtn = Button(context)

        val drawable = ContextCompat.getDrawable(context, R.drawable.start_round_button_bg)
        drawable?.let {
            val drawableCompat = DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(drawableCompat, cursorBtnColor)
            cursorBtn.background = drawable
        }

        cursorBtn.setOnTouchListener(onTouchListener())
        val lp = LayoutParams(cursorBtnSize.toInt(), cursorBtnSize.toInt())
        lp.addRule(END_OF, variantsContainer.id)
        lp.marginStart = cursorBtnStartMargin.toInt()
        cursorBtn.layoutParams = lp
        addView(cursorBtn)
    }

    private fun drawNewTv(str: String, index: Int): TextView {
        return TextView(context).apply {
            text = str
            textSize = tvSize
            val color = if (cursorInitialIndex != index) {
                Color.BLACK
            } else {
                tvColor
            }
            setTextColor(color)

            val typeface = ResourcesCompat.getFont(context, R.font.caviar_dreams_bold)
            setTypeface(typeface)

            setOnClickListener {
                moveCursorTo(index)
                ScrollHelper.playScrollSound(context)
                toFocus(index)
            }

            setPadding(
                tvPadding,
                tvPadding / 2,
                tvPadding * 3,
                tvPadding / 2
            )

            val lp = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            layoutParams = lp
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
                            Timber.d("cursorMid   : $cursorMid  ---------------------------")

                            val posAndScale = getCurrentFocusedPosition(cursorMid.toInt())
                            val index = posAndScale.first
                            val scaleXY = posAndScale.second
                            if (index != -1) {
                                if (cursorIndex != index) {
                                    cursorIndex = index
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
        val color = manipulateColor(tvColor, scale)
        focusedTv = listTv[index]
        focusedTv?.apply {
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
            setTextColor(Color.BLACK)
        }
    }

    private fun moveCursorTo(index: Int) {
        val cursorItemHalfHeight = (cursorBtn.bottom - cursorBtn.top) / 2
        if (index == -1) {
            return
        }
        val currentItemY = index * oneItemHeight
        val currentItemMid = currentItemY + (oneItemHeight / 2)
        val cursorMid = cursorBtn.y - (cursorBtn.height / 2)
        val diff = when {
            cursorMid > currentItemMid -> -(cursorMid - currentItemMid) //to Up
            currentItemMid > cursorMid -> currentItemMid - cursorMid    //to Down
            else -> return                                              //cursor is in center
        }

        val translationY = cursorMid + diff

        Timber.d("cursorMid      : $cursorMid ------------------")
        Timber.d("diff           : $diff")
        Timber.d("itemIndex      : $index")
        Timber.d("oneItemHeight  : $oneItemHeight")
        Timber.d("currentItemMid : $currentItemMid")
        Timber.d("translationY   : $translationY")

        cursorBtn.animate().translationY(translationY - cursorItemHalfHeight).duration = 50
    }

    private fun cursorToCenter(cursorMid: Float) {
        val itemIndex = getCurrentFocusedPosition(cursorMid.toInt()).first
        Timber.d("cursorBtn.y      : ${cursorBtn.y} ---------")
        Timber.d("cursorBtn.height : ${cursorBtn.height}")
        Timber.d("cursorMid        : $cursorMid")
        moveCursorTo(itemIndex)
        toFocus(itemIndex)
    }

    private fun getCurrentFocusedPosition(cursorMid: Int): Pair<Int, Float> {
        Timber.d("cursorMid: $cursorMid  --------------------")
        for (position in listStr.indices) {
            val top = height - (height - (position * oneItemHeight))
            val bottom = top + oneItemHeight

            Timber.d("top: $top, bottom: $bottom")

            if (cursorMid in top + 1 until bottom) {
                var scalePercent = getItemScale(top, bottom, cursorMid)
                if (scalePercent < 0f) {
                    scalePercent = 0f
                }
                Timber.d("position: $position, scalePercent : $scalePercent")
                return Pair(position, scalePercent)
            }
        }
        Timber.d("return def values -> Pair(-1, 0f)")
        return Pair(-1, 0f) //default
    }

    private fun getItemScale(top: Int, bottom: Int, chooserMid: Int): Float {
        val height = bottom - top
        val itemMid = height / 2

        val rawMid = bottom - itemMid
        val diff = max(rawMid, chooserMid) - min(rawMid, chooserMid)
        val scalePercent = itemMid - diff
        val result = scalePercent * 100.0f / itemMid
        return result * 0.01f
    }
}