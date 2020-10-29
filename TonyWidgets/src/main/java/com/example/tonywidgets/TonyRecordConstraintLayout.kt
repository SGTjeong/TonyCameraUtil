package com.example.tonywidgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

class TonyRecordConstraintLayout : TonyRippleConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onFinishInflate() {
        super.onFinishInflate()

        var recordButton : TonyRecordButton? = null
        var recordLock : TonyRecordLock? = null

        for(i in 0 until childCount){
            val view = getChildAt(i)
            if(view is TonyRecordLock){
                if(view.getLayoutBehavior() == context.getString(R.string.record_lock_behavior)){
                    recordLock = view
                }
            } else if(view is TonyRecordButton){
                recordButton = view
            }
        }

        if(recordButton!=null && recordLock!=null){
            recordButton.attachLockView(recordLock)
            recordLock.visibility = View.GONE
        }

    }
}