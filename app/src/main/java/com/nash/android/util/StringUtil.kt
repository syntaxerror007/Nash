package com.nash.android.util

import android.text.Html
import android.text.SpannableString
import android.text.Spanned

object StringUtil {

    fun fromHtml(string: String?): Spanned {
        return when {
            string.isNullOrEmpty() -> SpannableString("")
            AppUtil.isAndroidN() -> Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY, null, HtmlTagHandler())
            else -> Html.fromHtml(string, null, HtmlTagHandler())
        }
    }
}
