package com.example.tonywidgets

interface TonyHandsFreeListener {
    fun onHandsFree()
    fun onHandsFreeReady()
    fun onHandsFreeNotReady()
    fun onStartRecord()
    fun onFinishRecord()
}