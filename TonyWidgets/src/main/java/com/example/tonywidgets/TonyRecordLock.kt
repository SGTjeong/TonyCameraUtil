package com.example.tonywidgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import java.lang.Exception

class TonyRecordLock : androidx.appcompat.widget.AppCompatImageView, TonyHandsFreeListener{
    private lateinit var layoutBehavior : String
    private lateinit var drawableOff : Drawable
    private lateinit var drawableOn : Drawable

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        if(attrs == null) return
        loadAttributes(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        if(attrs == null) return
        loadAttributes(context, attrs)
    }


    private fun loadAttributes(context: Context, attrs: AttributeSet) {
        val attrArr = context.obtainStyledAttributes(attrs, R.styleable.TonyRecordLock, 0, 0)
        try{
            layoutBehavior = attrArr.getString(R.styleable.TonyRecordLock_record_lock_behavior)?:""
            drawableOn = attrArr.getDrawable(R.styleable.TonyRecordLock_drawableReady)?:ContextCompat.getDrawable(context, R.drawable.lock_on)!!
            drawableOff = attrArr.getDrawable(R.styleable.TonyRecordLock_drawableNotReady)?:ContextCompat.getDrawable(context, R.drawable.lock_off)!!
        } catch (e : Exception){
            Log.e("TonyRecordLock", e.toString())
        } finally {
            attrArr.recycle()
        }
    }

    fun getLayoutBehavior() = layoutBehavior

    override fun onFinishRecord() {
        visibility = View.GONE
    }

    override fun onStartRecord() {
        visibility = View.VISIBLE
        background = drawableOff
    }

    override fun onHandsFreeReady() {
        if (!isAttachedToWindow) return
        background = drawableOn
    }

    override fun onHandsFreeNotReady() {
        if(!isAttachedToWindow) return
        background = drawableOff
    }

    override fun onHandsFree() {
        if(!isAttachedToWindow) return
        visibility = View.GONE
    }
}