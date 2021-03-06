package com.example.tonycamerautil

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.tonywidgets.TonyRecordActionListener
import com.example.tonywidgets.TonyRecordButton


class SimpleActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         *  callback for record event
         * */
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

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}