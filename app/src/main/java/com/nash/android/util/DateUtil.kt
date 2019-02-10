package com.nash.android.util

import com.nash.android.data.NashDate
import java.text.SimpleDateFormat
import java.util.*

class DateUtil {
    companion object {
        const val DATE_FORMAT_SHOWN = "dd MMM yyyy"
        fun convertShownDateToNashDate(shownDate: String): NashDate {
            val format = SimpleDateFormat(DATE_FORMAT_SHOWN, Locale.US)
            val date = format.parse(shownDate)
            val calendar = Calendar.getInstance()
            calendar.time = date
            return NashDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR))
        }

        fun convertNashDateToCalendar(nashDate: NashDate): Calendar {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_MONTH, nashDate.day)
            calendar.set(Calendar.MONTH, nashDate.month)
            calendar.set(Calendar.YEAR, nashDate.year)
            return calendar
        }
    }

}