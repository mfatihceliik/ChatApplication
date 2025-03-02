package com.mfatihceliik.chatapplication.util

import java.util.Date
import java.util.Calendar

fun calculateDuration(duration: Long): String {
    val seconds = duration / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60

    return if(seconds < 60) {
        String.format("%2d:%02d", minutes, seconds)
    }else {
        String.format("%02d:%02d", minutes, remainingSeconds)
    }
}

fun lastMessageTime(date: Date?): String {

    val calendar = Calendar.getInstance()
    calendar.time = date ?: Date(System.currentTimeMillis())
    val hour = calendar.get(Calendar.HOUR)
    val minute = calendar.get(Calendar.MINUTE)

    val mHour = if (hour < 10) "0$hour" else hour
    val mMinute = if(minute < 10) "0$minute" else minute

    return "$mHour:$mMinute"
}

fun searchDayOfWeek(day: Int): String {
    return when(day) {
        Calendar.MONDAY -> return "Monday"
        Calendar.TUESDAY -> return "Tuesday"
        Calendar.WEDNESDAY -> return "Wednesday"
        Calendar.THURSDAY -> return "Thursday"
        Calendar.FRIDAY -> return "Friday"
        Calendar.SATURDAY -> return "Saturday"
        Calendar.SUNDAY -> return "Sunday"
        else -> "Unexpected"
    }
}

fun searchMonthOfYear(month: Int): String {
    return when(month) {
        Calendar.JANUARY -> return "January"
        Calendar.FEBRUARY -> return "February"
        Calendar.MARCH -> return "March"
        Calendar.APRIL -> return "April"
        Calendar.MAY -> return "May"
        Calendar.JUNE -> return "June"
        Calendar.JULY -> return "July"
        Calendar.AUGUST -> return "August"
        Calendar.SEPTEMBER -> return "September"
        Calendar.OCTOBER -> return "October"
        Calendar.NOVEMBER  -> return "November"
        Calendar.DECEMBER  -> return "December"
        else -> "Unexpected"
    }
}