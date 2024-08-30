package com.example.kmp.core.platform.date

import kotlinx.datetime.Clock

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect class DateTimeConverter() {

    fun millisToFormattedDate(millis: Long, pattern: String = "dd/MM/yyyy"): String

    fun stringDateToMillis(dateStr: String?): Long?

    fun utcDateToFormattedDate(utcDate: String?, pattern: String = "dd/MM/yyyy"): String?

    fun utcDateToYearMonthDay(utcDate: String?): String?

    fun millisToUTCFormattedDate(dateMillis: Long?): String?

    /**
     * @param fromDate utc formated day
     * @param toDate utc formated day
     */
    fun dayDifference(fromDate: String, toDate: String): Int?

    fun dayDifferenceToCurrentDate(fromDate: String, currentDateInMillis: Long = Clock.System.now().toEpochMilliseconds()): Int?

    fun currentDate(): Long
}
