package com.example.pantanima.ui.customviews

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.pantanima.R
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.pantanima.ui.adapters.ScrollHelper
import kotlin.math.max
import kotlin.math.min


class VerticalSliderView : RelativeLayout {

    private var listener: OnCursorPositionChangeListener? = null
    private var cursorInitialIndex: Int = 1
    private var cursorBtnStartMargin = 0.0f
    private var yDelta: Int = 0
    private var tvSize = 30f
    private var cursorBtnSize = 60f
    private var tvPadding = 10
    private var tvColor = Color.BLUE
    private var cursorBtnColor = ContextCompat.getColor(context, R.color.app_style_green)
    private var focusedTv: TextView? = null
    private lateinit var variantsContainer: LinearLayout
    private lateinit var cursorView: View
    private var cursorIndex = cursorInitialIndex
    private var oneItemHeight: Int = 0
        get() {
            if (field <= 0) {
                val totalHeight = variantsContainer.height
                field = totalHeight / listStr.size
            }
            return field
        }
    private var listTv: MutableList<TextView> = ArrayList()
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

    fun setOnCursorPositionChangedListener(listener: OnCursorPositionChangeListener) {
        this.listener = listener
    }

    fun setCursorInitialIndex(index: Int) {
        this.cursorInitialIndex = index
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

    @SuppressLint("ClickableViewAccessibility")
    private fun drawCursorButton() {
        cursorView = View(context)

        val drawable = ContextCompat.getDrawable(context, R.drawable.circle_border_6sdp)
        drawable?.let {
//            val drawableCompat = DrawableCompat.wrap(drawable)
//            DrawableCompat.setTint(drawableCompat, cursorBtnColor)
            cursorView.background = drawable
        }

        cursorView.setOnTouchListener(onTouchListener())
        val lp = LayoutParams(cursorBtnSize.toInt(), cursorBtnSize.toInt())
        lp.addRule(END_OF, variantsContainer.id)
        lp.marginStart = cursorBtnStartMargin.toInt()
        cursorView.layoutParams = lp
        cursorView.visibility = View.INVISIBLE
        addView(cursorView)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val h = bottom - top
        oneItemHeight = h / listStr.size
        moveCursorTo(cursorInitialIndex)
        toFocus(cursorInitialIndex)
        super.onLayout(changed, left, top, right, bottom)
    }

    private fun drawNewTv(str: String, index: Int): TextView {
        return TextView(context).apply {
            text = str
            textSize = tvSize
            val typeface = ResourcesCompat.getFont(context, R.font.caviar_dreams_bold)
            setTypeface(typeface)

            setOnClickListener {
                if (cursorIndex != index) {
                    cursorIndex = index
                    moveCursorTo(index)
                    ScrollHelper.playScrollSound(context)
                    toFocus(index)
                    listener?.onChanged(index)
                }
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
                MotionEvent.ACTION_MOVE -> {
                    val transY = rawY.toFloat()
                    val newY = transY + yDelta
                    if (newY > 0) {
                        val viewBottomY = newY + view.height + top
                        if (viewBottomY < bottom) {
                            view.y = newY
                            val btnHalfHeight = (view.bottom - view.top) / 2
                            cursorMid = newY + btnHalfHeight

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
                            invalidate()
                        }
                    }
                }
            }
            true
        }
    }

    private fun manipulateColor(colorFrom: Int, colorTo: Int, factor: Float): Int {
        val a = Color.alpha(colorFrom)

        val redScalePair = getColorScale(Color.red(colorFrom), Color.red(colorTo))
        val greenScalePair = getColorScale(Color.green(colorFrom), Color.green(colorTo))
        val blueScalePair = getColorScale(Color.blue(colorFrom), Color.blue(colorTo))

        val r = (Color.red(colorFrom).toFloat() + (redScalePair * factor)).toInt()
        val g = (Color.green(colorFrom).toFloat() + (greenScalePair * factor)).toInt()
        val b = (Color.blue(colorFrom).toFloat() + (blueScalePair * factor)).toInt()

        return Color.argb(a, r, g, b)
    }

    private fun getColorScale(colorFrom: Int, colorTo: Int): Float {
        return if (colorFrom > colorTo) {
            -(colorFrom - colorTo).toFloat()
        } else {
            (colorTo - colorFrom).toFloat()
        }
    }

    private fun toFocus(index: Int, scale: Float = 1f) {
        for (i in listTv.indices) {
            if (i == index) {
                val color = manipulateColor(tvColor, cursorBtnColor, scale)
                focusedTv = listTv[index]
                focusedTv?.apply {
                    setTextColor(color)
                    scaleX = 1 + (scale / 4)
                    scaleY = 1 + (scale / 4)
                    translationX = scale * 35
                }
            } else {
                toSecondary(listTv[i])
            }
        }
    }

    private fun toSecondary(tv: TextView) {
        tv.apply {
            scaleX = 1f
            scaleY = 1f
            translationX = 0f
            setTextColor(tvColor)
        }
    }

    private fun moveCursorTo(index: Int) {
        val cursorItemHalfHeight = cursorBtnSize / 2
        val currentItemY = index * oneItemHeight
        val currentItemMid = currentItemY + (oneItemHeight / 2)
        val cursorMid = cursorView.y - (cursorView.height / 2)
        val diff = when {
            cursorMid > currentItemMid -> -(cursorMid - currentItemMid) //to Up
            currentItemMid > cursorMid -> currentItemMid - cursorMid    //to Down
            else -> return                                              //cursor is in center
        }

        val translationY = cursorMid + diff

        cursorView.animate()
            .translationY(translationY - cursorItemHalfHeight)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(p0: Animator?) {
                    if (cursorView.visibility != View.VISIBLE) {
                        cursorView.visibility = View.VISIBLE
                    }
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationStart(p0: Animator?) {
                }

                override fun onAnimationRepeat(p0: Animator?) {
                }
            })
            .duration = 0
    }

    private fun cursorToCenter(cursorMid: Float) {
        val itemIndex = getCurrentFocusedPosition(cursorMid.toInt()).first
        if (itemIndex != -1) {
            listener?.onChanged(itemIndex)
            moveCursorTo(itemIndex)
            toFocus(itemIndex)
        }
    }

    private fun getCurrentFocusedPosition(cursorMid: Int): Pair<Int, Float> {
        for (position in listStr.indices) {
            val top = height - (height - (position * oneItemHeight))
            val bottom = top + oneItemHeight

            if (cursorMid in top + 1 until bottom) {
                var scalePercent = getItemScale(top, bottom, cursorMid)
                if (scalePercent < 0f) {
                    scalePercent = 0f
                }
                return Pair(position, scalePercent)
            }
        }
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

    interface OnCursorPositionChangeListener {
        fun onChanged(newPosition: Int)
    }
}