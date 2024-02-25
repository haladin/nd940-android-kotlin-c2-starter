package com.udacity.asteroidradar.utils

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.Locale

val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

fun convertDateToLong(date: String): Long {
    return dateFormat.parse(date).time
}

fun convertLongToDate(time: Long): String {
    val date = Date(time)
    return dateFormat.format(date)
}

fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}

fun getWeekAsLong(): Long{
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_END_DATE_DAYS)

    return convertDateToLong(dateFormat.format(calendar.time))
}

fun getTodayAsLong(): Long{
    return convertDateToLong(getTodayFormatted())
}

fun getTodayFormatted(): String {
    val calendar = Calendar.getInstance()
    val currentTime = calendar.time

    return dateFormat.format(currentTime)
}

fun daysToGet(days: Int): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, days)

    return dateFormat.format(calendar.time)
}