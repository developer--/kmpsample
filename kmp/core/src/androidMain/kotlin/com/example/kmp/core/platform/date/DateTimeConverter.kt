package com.example.kmp.core.platform.date

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.byUnicodePattern
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.ceil

actual class DateTimeConverter {
    actual fun millisToFormattedDate(millis: Long, pattern: String): String {
        val date = Date(millis)
        val outFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return outFormat.format(date)
    }

    actual  fun stringDateToMillis(dateStr: String?): Long? {
        try {
            if (dateStr.isNullOrEmpty()) return null
            val dateFormatter = SimpleDateFormat(PATTERN, Locale.getDefault())
            dateFormatter.timeZone = TimeZone.getTimeZone("UTC")
            return dateFormatter.parse(dateStr)?.time
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    actual  fun utcDateToFormattedDate(utcDate: String?, pattern: String): String? {
        if (utcDate == null) return null
        val dateTimeOffset = DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET
            .parse(utcDate)
        val localDate = dateTimeOffset.toLocalDateTime()
        val formatter = LocalDateTime.Format {
            byUnicodePattern(pattern)
        }
        return formatter.format(localDate)
//        if (utcDate == null) return null
//        val millis = stringDateToMillis(utcDate) ?: return null
//        return millisToFormattedDate(millis, pattern)
    }

    /**
     * @param [utcDate] 2023-09-07T00:00:00.000Z
     * @return yyyy-MM-dd 2023-09-07
     */
    actual  fun utcDateToYearMonthDay(utcDate: String?): String? {
        return try {
            if (utcDate == null) null
            else {
                utcDateToFormattedDate(utcDate, "yyyy-MM-dd")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            utcDate
        }
    }

    actual  fun millisToUTCFormattedDate(dateMillis: Long?): String? {
        if (dateMillis == null) return null
        val dateFormatter =
            SimpleDateFormat(PATTERN, Locale.getDefault())
        dateFormatter.timeZone = TimeZone.getTimeZone("UTC")
        val date = Date(dateMillis)
        return dateFormatter.format(date)
    }

    actual  fun currentDate(): Long {
        return System.currentTimeMillis()
    }

    companion object {
        const val PATTERN = "yyyy-MM-dd'T'HH:mm:ss"
    }

    /**
     * @param fromDate utc formated day
     * @param toDate utc formated day
     */
    actual  fun dayDifference(fromDate: String, toDate: String): Int? {
        val dateFormatter = SimpleDateFormat(PATTERN, Locale.getDefault())
        val dateFrom = dateFormatter.parse(fromDate) ?: return null
        val dateTo = dateFormatter.parse(toDate) ?: return null
        if (dateTo.after(dateFrom)) {
            val diffInMillis = dateTo.time - dateFrom.time
            val secondsInMilli = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24
            return ceil(diffInMillis.toDouble() / daysInMilli).toInt()
        }
        return null
    }

    actual fun dayDifferenceToCurrentDate(fromDate: String, currentDateInMillis: Long): Int? {
        try {
            val dateFormatter = SimpleDateFormat(PATTERN, Locale.getDefault())
            val dateFrom = dateFormatter.parse(fromDate) ?: return null
            val diffInMillis = dateFrom.time - currentDateInMillis
            val secondsInMilli = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24
            return (diffInMillis / daysInMilli).toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
