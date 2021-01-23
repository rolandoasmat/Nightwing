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
    fun formatDate(rawDate: String?): String {
        return rawDate?.let { rawDateString ->
            try {
                val date = transform(rawDateString)
                val sdf = SimpleDateFormat(DATE_FORMAT_DESIRED)
                val formatted = sdf.format(date)
                formatted.substring(0, 1).toUpperCase() + formatted.substring(1)
            } catch (e: Exception) {
                "Unable to parse date."
            }
        } ?: "Unable to parse date."
    }

    /**
     * Transforms a string in the format 'yyyy-MM-dd' into a [Date] object
     */
    fun transform(rawDate: String?): Date? {
        return rawDate?.let {
            val sdf = SimpleDateFormat(DATE_FORMAT_ORIGINAL)
            return try{
                sdf.parse(rawDate)
            } catch (e: Exception) {
                null
            }
        }
    }
}