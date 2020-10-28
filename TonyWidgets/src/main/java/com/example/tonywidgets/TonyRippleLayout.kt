package com.example.tonywidgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.MotionEventCompat

class TonyRippleLayout : ConstraintLayout {
    private lateinit var pointerView : PointerView
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init{
        setWillNotDraw(false)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        pointerView = PointerView(context).also {
            it.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }
        addView(pointerView)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.e("WONSIK", "dispatchTouchEvent in TonyRippleLayout")
        pointerView.drawPointers(ev)
        return super.dispatchTouchEvent(ev)
    }
}