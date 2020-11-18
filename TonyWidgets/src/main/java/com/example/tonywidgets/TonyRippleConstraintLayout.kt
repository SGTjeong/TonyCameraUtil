package com.example.tonywidgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import java.lang.Exception

open class TonyRippleConstraintLayout : ConstraintLayout {
    private val TAG = this.javaClass.simpleName

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        init(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        init(context, attrs)
    }

    private lateinit var tonyRippleCanvas : TonyRippleCanvas
    private var willDraw : Boolean = false
    private var rippleColor : Int = DEFAULT_COLOR
    private var rippleRadius : Int = DEFAULT_RADIUS

    private fun init(context : Context, attrs : AttributeSet?){
        setWillNotDraw(false)
        loadAttributes(context, attrs)
    }

    private fun loadAttributes(context: Context, attrs: AttributeSet?) {
        val attrArr = context.obtainStyledAttributes(attrs, R.styleable.TonyRippleConstraintLayout, 0, 0)
        try{
            willDraw = attrArr.getBoolean(R.styleable.TonyRippleConstraintLayout_willDraw, false)
            rippleColor = attrArr.getColor(R.styleable.TonyRippleConstraintLayout_rippleColor, TonyRippleConstraintLayout.DEFAULT_COLOR)
            rippleRadius = attrArr.getDimensionPixelSize(R.styleable.TonyRippleConstraintLayout_rippleRadius, DEFAULT_RADIUS)
        } catch (e : Exception){
            Log.e("$TAG", e.toString())
        } finally {
            attrArr.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        addPointerView(context)
    }

    private fun addPointerView(context: Context) {
        tonyRippleCanvas = TonyRippleCanvas(context).also {
            it.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            it.setColor(rippleColor)
            it.setRadius(rippleRadius)
        }
        addView(tonyRippleCanvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(willDraw) {
            tonyRippleCanvas.drawPointers(event)
        }
        return true
    }



    companion object{
        val DEFAULT_COLOR = Color.WHITE
        val DEFAULT_RADIUS = 100
    }
}