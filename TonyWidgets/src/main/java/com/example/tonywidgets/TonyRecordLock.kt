package com.example.tonywidgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.lang.Exception

class TonyRecordLock : View {
    private lateinit var layoutBehavior : String
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        if(context == null || attrs == null) return
        loadAttributes(context, attrs)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        if(context == null || attrs == null) return
        loadAttributes(context, attrs)
    }

    private fun loadAttributes(context: Context, attrs: AttributeSet) {
        val attrArr = context.obtainStyledAttributes(attrs, R.styleable.TonyRecordLock, 0, 0)
        try{
            //layoutBehavior = attrArr.getString(R.styleable.TonyRecordLock_layout_behavior)?:""
        } catch (e : Exception){
            Log.e("TonyPhonePicker", e.toString())
        } finally {
            attrArr.recycle()
        }
    }

    fun getLayoutBehavior() = layoutBehavior

}