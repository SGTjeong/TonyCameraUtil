package com.example.tonywidgets

import android.view.MotionEvent

abstract class HandsFreeDetector {
    abstract fun isMatchingCondition(motionEvent: MotionEvent) : Boolean
}