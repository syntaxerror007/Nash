package com.android.nash.util

import android.text.Html
import android.text.SpannableString
import android.text.Spanned

object StringUtil {

    fun fromHtml(string: String?): Spanned {
        return when {
            isNullOrEmpty(string) -> SpannableString("")
            AppUtil.isAndroidN() -> Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY, null, HtmlTagHandler())
            else -> Html.fromHtml(string, null, HtmlTagHandler())
        }
    }


    fun isNullOrEmpty(str: String?): Boolean {
        return str == null || str.trim { it <= ' ' }.isEmpty()
    }
}
