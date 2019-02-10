package com.nash.android.util

import android.app.Dialog
import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.nash.android.data.NashDate
import com.nash.android.util.DateUtil.Companion.DATE_FORMAT_SHOWN
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


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
    calendar.set(Calendar.MONTH, this.month - 1)
    calendar.set(Calendar.YEAR, this.year)
    return formatter.format(calendar.time)
}


fun NashDate.convertToCalendar(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.set(this.year, this.month - 1, this.day, 0, 0)
    return calendar
}

fun Calendar.toNashDate(): NashDate {
    return NashDate(
            this.get(Calendar.DAY_OF_MONTH),
            this.get(Calendar.MONTH) + 1,
            this.get(Calendar.YEAR),
            this.get(Calendar.HOUR),
            this.get(Calendar.MINUTE))
}

fun AppCompatActivity.dismissKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm.isAcceptingText)
        imm.hideSoftInputFromWindow(this.currentFocus.windowToken, 0)
}

fun Dialog.dismissKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm.isAcceptingText)
        imm.hideSoftInputFromWindow(this.currentFocus.windowToken, 0)
}

fun EditText.validateNullOrBlank(errorMessage: String) {
    if (this.text.toString().isBlank())
        this.error = errorMessage
    else
        this.error = null
}


fun Long.convertToPrice(): String {
    val symbol = DecimalFormatSymbols()
    symbol.decimalSeparator = ','
    symbol.groupingSeparator = '.'
    val decimalFormat = DecimalFormat("#,###.###", symbol)
    return "Rp. ${decimalFormat.format(this)}"
}

fun Int.convertToPrice(): String {
    val symbol = DecimalFormatSymbols()
    symbol.decimalSeparator = ','
    symbol.groupingSeparator = '.'
    val decimalFormat = DecimalFormat("#,###.###", symbol)
    return "Rp. ${decimalFormat.format(this)}"
}


fun View.setVisible(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun Boolean.toYesNo(): String {
    return if (this) "YES" else "NO"
}

fun String.isValidEmail(): Boolean {
    val VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    val matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(this)
    return matcher.find()
}