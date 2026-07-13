package com.example.myweatherapp.util

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.dateWithTimeIntervalSince1970

actual object DateTimeUtil {
    actual fun formatTimestamp(timestamp: Long): String {
        val date = NSDate.dateWithTimeIntervalSince1970(timestamp.toDouble())
        val formatter = NSDateFormatter().apply {
            dateFormat = "EEE, MMM d, HH:mm"
        }
        return formatter.stringFromDate(date)
    }
}
