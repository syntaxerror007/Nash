package com.android.nash.util

import android.support.annotation.DrawableRes
import android.text.Spannable
import android.widget.ImageView
import android.widget.TextView
import com.android.nash.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


fun ImageView.loadUrl(url: CharSequence?, @DrawableRes placeholder: Int) {
    val requestOptions: RequestOptions = RequestOptions().placeholder(placeholder)
    Glide.with(context).load(url).apply(requestOptions).into(this)
}

fun TextView.htmlFormat(text: String?) {
    this.text = StringUtil.fromHtml(text)
}
