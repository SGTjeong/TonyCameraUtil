package com.example.tonywidgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class PointerView : View {
    private var list : MutableList<Pair<Float,Float>>? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e("WONSIK", "onTouchEvent in PointerView")
        event?: return super.onTouchEvent(event)
        return false
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
        Log.e("WONSIK", "onDraw")
        super.onDraw(canvas)
        list?.let {
            val itr = it.iterator()
            var pair: Pair<Float, Float>? = null
            while (itr.hasNext()) {
                pair = itr.next()
                canvas?.drawCircle(
                    pair.first,
                    pair.second,
                    40f,
                    Paint().apply { color = Color.BLACK })
            }
        }
    }
}