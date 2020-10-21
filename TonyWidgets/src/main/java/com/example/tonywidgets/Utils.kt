package com.example.tonyrecordbutton

import android.content.Context
import kotlin.math.roundToInt

internal fun Context.dpToPx(dp: Int): Int {
    val density = resources.displayMetrics.density
    return (dp.toFloat() * density).roundToInt()
}