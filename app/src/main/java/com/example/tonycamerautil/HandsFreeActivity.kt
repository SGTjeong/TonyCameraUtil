package com.example.tonycamerautil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import com.example.tonywidgets.HandsFreeDetector
import com.example.tonywidgets.TonyRecordActionListener
import com.example.tonywidgets.TonyRecordButton

class HandsFreeActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hands_free)

        val btn = findViewById<TonyRecordButton>(R.id.btn)
        btn.setActionListener(object : TonyRecordActionListener {
            override fun onCapture() {
                Log.e(TAG, "onCapture")
            }

            override fun onHandsFree() {
                Log.e(TAG, "onHandsFree")
            }

            override fun onStartRecord() {
                Log.e(TAG, "onStartRecord")
            }

            override fun onFinishRecord() {
                Log.e(TAG, "onFinishRecord")
            }
        })

        /**
         *  set hands-free condition
         * */
        btn.setHandsFreeDetector(object : HandsFreeDetector() {
            override fun isMatchingCondition(event: MotionEvent): Boolean {
                return (event.x < 0 && event.actionMasked == MotionEvent.ACTION_UP)
            }

            override fun isMatchingReadyCondition(event: MotionEvent): Boolean {
                return (event.x < 0 && event.actionMasked == MotionEvent.ACTION_MOVE)
            }
        })
    }
}