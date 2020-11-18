package com.example.tonywidgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class TonyRippleCanvas : View {
    private val TAG = this.javaClass.simpleName
    private var list : MutableList<Pair<Float,Float>>? = null

    private var rippleColor = TonyRippleConstraintLayout.DEFAULT_COLOR
    private var rippleRadius = TonyRippleConstraintLayout.DEFAULT_RADIUS

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setColor(color : Int){
        this.rippleColor = color
    }

    fun setRadius(radius : Int){
        this.rippleRadius = radius
    }

    fun drawPointers(event : MotionEvent?){
        event?:return
        when(event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                drawCircle(event)
            }

            MotionEvent.ACTION_MOVE -> {
                drawCircle(event)
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                drawCircle(event)
            }

            MotionEvent.ACTION_UP -> {
                removeLastCircle(event)
            }

            MotionEvent.ACTION_POINTER_UP -> {

            }
        }
    }

    private fun removeLastCircle(event: MotionEvent) {
        list = mutableListOf()
        invalidate()
    }


    private fun drawCircle(event : MotionEvent) {
        Log.e("WONSIK", "drawCircle")
        list = mutableListOf()
        for(i in 0 until event.pointerCount){
            val x = event.getX(i)
            val y = event.getY(i)
            list?.add(Pair(x, y))
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(canvas == null || list == null) return

        val itr = list!!.iterator()
        var pair: Pair<Float, Float>?

        while (itr.hasNext()) {
            pair = itr.next()
            canvas?.drawCircle(
                pair.first, pair.second, rippleRadius.toFloat(), Paint().apply { color = rippleColor}
            )
        }
    }
}