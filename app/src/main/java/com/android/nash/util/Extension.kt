package com.android.nash.util

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.nash.data.NashDate
import com.android.nash.util.DateUtil.Companion.DATE_FORMAT_SHOWN
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


fun ImageView.loadUrl(url: CharSequence?, @DrawableRes placeholder: Int) {
    val requestOptions: RequestOptions = RequestOptions().placeholder(placeholder)
    Glide.with(context).load(url).apply(requestOptions).into(this)
}

fun TextView.htmlFormat(text: String?) {
    this.text = StringUtil.fromHtml(text)
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun NashDate.convertToString(): String {
    val formatter = SimpleDateFormat(DATE_FORMAT_SHOWN, Locale.US)
    val calendar = Calendar.getInstance()

    calendar.set(Calendar.DAY_OF_MONTH, this.day)
    calendar.set(Calendar.MONTH, this.month)
    calendar.set(Calendar.YEAR, this.year)
    return formatter.format(calendar.time)
}

fun AppCompatActivity.dismissKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm.isAcceptingText)
        imm.hideSoftInputFromWindow(this.currentFocus.windowToken, 0)
}

fun EditText.validateNullOrBlank(errorMessage: String) {
    if (this.text.toString().isBlank())
        this.error = errorMessage
    else
        this.error = null
}
