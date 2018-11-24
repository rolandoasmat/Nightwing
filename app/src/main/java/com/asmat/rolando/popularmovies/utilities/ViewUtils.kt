package com.asmat.rolando.popularmovies.utilities

import android.content.Context
import android.content.res.Configuration

object ViewUtils {

    /**
     * Determine the number of columns to use in the Movies Grid
     * depending on orientation.
     */
    fun calculateNumberOfColumns(context: Context): Int {
        val orientation = context.resources.configuration.orientation
        return if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            3
        } else {
            2
        }

    }
}
