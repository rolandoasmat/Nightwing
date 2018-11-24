package com.asmat.rolando.popularmovies.utilities

import java.text.ParseException
import java.text.SimpleDateFormat

object DateUtils {

    private const val DATE_FORMAT_ORIGINAL = "yyyy-MM-dd"
    private const val DATE_FORMAT_DESIRED = "MMMM dd, yyyy"

    /**
     * Formats a date from 'yyyy-MM-dd' to 'MMMM dd, yyyy' format.
     */
    fun formatDate(rawDate: String): String {
        return try {
            var sdf = SimpleDateFormat(DATE_FORMAT_ORIGINAL)
            val date = sdf.parse(rawDate)
            sdf = SimpleDateFormat(DATE_FORMAT_DESIRED)
            val formatted = sdf.format(date)
            formatted.substring(0, 1).toUpperCase() + formatted.substring(1)
        } catch (e: ParseException) {
            "Unable to parse date."
        }

    }
}