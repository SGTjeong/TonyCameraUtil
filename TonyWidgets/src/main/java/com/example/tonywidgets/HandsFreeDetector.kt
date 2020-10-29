package com.example.tonywidgets

import android.view.MotionEvent

abstract class HandsFreeDetector {
    abstract fun isMatchingCondition(motionEvent: MotionEvent) : Boolean
    open fun isMatchingReadyCondition(motionEvent: MotionEvent) : Boolean {
        return false
    }
}