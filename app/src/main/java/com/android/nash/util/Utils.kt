package com.android.nash.util

import android.content.Context
import android.util.DisplayMetrics

object Utils {
    fun dpToPx(context: Context, dp: Int) = Math.round(dp *
            (context.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
}