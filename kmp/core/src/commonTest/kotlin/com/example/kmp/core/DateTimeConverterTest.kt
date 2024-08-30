package com.example.kmp.core

import com.example.kmp.core.platform.date.DateTimeConverter
import kotlin.test.*

class DateTimeConverterTest {

    private lateinit var sut: DateTimeConverter

    @BeforeTest
    fun setUp() {
        sut = DateTimeConverter()
    }

    @Test
    fun millisToFormattedDate_withData_returnsNotEmpty() {
        val result = sut.millisToFormattedDate(DATE_MILLIS)
        assertTrue(result.isNotEmpty(), "millisToFormattedDate returns not empty string date")
    }

    @Test
    fun millisToFormattedDate_withData_returnsDateAsPatternFormat() {
        val result = sut.millisToFormattedDate(DATE_MILLIS)
        assertTrue(
            result.split("/").size == 3, "millisToFormattedDate returns " +
                    "default formatted string date"
        )
    }

    @Test
    fun stringDateToMillis_withNotCorrectData_returnsNull() {
        val result = sut.stringDateToMillis("not/date/str")
        assertTrue(result == null)
        val result2 = sut.stringDateToMillis("2023-09-09T00:00:00.000Z")
        assertNotNull(result2)
    }

    @Test
    fun stringDateToMillis_withConvertedDate_returnsSameDateAsMillis() {
        val converted = sut.millisToUTCFormattedDate(DATE_MILLIS)
        assertNotNull(converted)
        val millisStr = sut.stringDateToMillis(converted)?.toString()
        assertNotNull(millisStr)
        val subStrOriginalMillis =
            DATE_MILLIS.toString().substring(0, DATE_MILLIS.toString().length - 3)
        val subStrMillis = millisStr.substring(0, millisStr.length - 3)
        assertEquals(
            subStrMillis, subStrOriginalMillis, "stringDateToMillis returns " +
                    "same date as millis (last three numbers where cut)"
        )
    }

    @Test
    fun currentDate_returnsPositiveNumber() {
        val currDate = sut.currentDate()
        assertTrue(currDate > 0)
    }

    @Test
    fun millisToUTCFormattedDate_returns2021Year() {
        val currDate = sut.millisToUTCFormattedDate(DATE_MILLIS_1)
        assertNotNull(currDate)
        val array = currDate.split("-")
        val year = array[0]
        val month = array[1]
        assertEquals(year, "2021")
        assertEquals(month, "06")
    }

    @Test
    fun millisToFormattedDate_withTimePattern_returnsCorrectData() {
        val result = sut.millisToFormattedDate(DATE_MILLIS, pattern = "HH:mm")
        assertTrue(result.isNotEmpty())
        assertEquals(result, "13:32")
    }

    @Test
    fun utcDateToFormattedDate_withTimePattern_returnsCorrectData() {
        val result = sut.utcDateToFormattedDate(DATE_IN_UTC, pattern = "HH:mm")
        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals(result, "11:58")
    }

    @Test
    fun testDayDifferenceToCurrentDate() {
        val result =
            sut.dayDifferenceToCurrentDate(DATE_2_IN_UTC, currentDateInMillis = DATE_2_IN_MILLIS)
        assertEquals(-1, result)
    }

    @Test
    fun testDayDifferences() {
        val result =
            sut.dayDifference(DATE_2_IN_UTC, DATE_3_IN_UTC)
        assertEquals(2, result)
    }

    companion object {
        // UTC -> Sun May 30 2021 09:32:38 && to local -> Sun May 30 2021 13:32:38
        const val DATE_MILLIS = 1622367158975L
        const val DATE_MILLIS_1 = 1624868806334L // 28/06/2021
        const val DATE_IN_UTC = "2021-01-08T11:58:18.110Z"
        const val DATE_2_IN_MILLIS = 1724662653061 // 26 Aug 2024
        const val DATE_2_IN_UTC = "2024-08-25T11:58:18.110Z"
        const val DATE_3_IN_UTC = "2024-08-27T11:58:18.110Z"
    }
}
