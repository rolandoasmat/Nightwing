package com.asmat.rolando.nightwing.utilities

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private const val DATE_FORMAT_ORIGINAL = "yyyy-MM-dd"
    private const val DATE_FORMAT_DESIRED = "MMMM dd, yyyy"

    /**
     * Formats a date from 'yyyy-MM-dd' to 'MMMM dd, yyyy' format.
     */
    fun formatDate(rawDate: String): String {
        return try {
            val date = transform(rawDate)
            val sdf = SimpleDateFormat(DATE_FORMAT_DESIRED)
            val formatted = sdf.format(date)
            formatted.substring(0, 1).toUpperCase() + formatted.substring(1)
        } catch (e: ParseException) {
            "Unable to parse date."
        }

    }

    /**
     * Transforms a string in the format 'yyyy-MM-dd' into a [Date] object
     */
    fun transform(rawDate: String): Date {
        val sdf = SimpleDateFormat(DATE_FORMAT_ORIGINAL)
        return sdf.parse(rawDate)
    }
}