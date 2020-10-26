package com.example.tonywidgets

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

class TonyCameraLayout : ConstraintLayout{
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    override fun onFinishInflate() {
        super.onFinishInflate()

        var recordLock : TonyRecordLock? = null
        var recordButton : TonyRecordButton? = null

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


    }
}