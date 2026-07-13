package com.example.myweatherapp.util

expect object DateTimeUtil {
    fun formatTimestamp(timestamp: Long): String
}
