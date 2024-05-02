package com.dothebestmayb.nbc_search.data.network

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateAdapter {

    private val parseFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.KOREA)

    @ToJson
    fun toJson(datetime: Date): String {
        return formatDate(datetime)
    }

    @FromJson
    fun fromJson(datetime: String): Date {
        return parseDate(datetime)
    }

    private fun parseDate(dateString: String): Date {
        return parseFormat.parse(dateString)
    }

    private fun formatDate(date: Date): String {
        return parseFormat.format(date)
    }
}