package com.android.nash.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build

object AppUtil{


    fun isAndroidL(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    fun isAndroidM(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    fun isAndroidN(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }

    fun isAndroidJ_MR1(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
    }

    fun copyToClipboard(context: Context, label: String, text: String) {
        if (StringUtil.isNullOrEmpty(label) || StringUtil.isNullOrEmpty(text)) {
            return
        }

        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)

        if (clipboard != null) {
            clipboard.primaryClip = clip
        }
    }
}