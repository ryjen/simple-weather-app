package com.example.myweatherapp.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual object DateTimeUtil {
    actual fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp * 1000L)
        val sdf = SimpleDateFormat("EEE, MMM d, HH:mm", Locale.getDefault())
        return sdf.format(date)
    }
}
