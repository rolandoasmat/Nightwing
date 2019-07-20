package com.asmat.rolando.popularmovies.ui.mylists

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.ui.common.BaseSectionsPagerAdapter
import com.asmat.rolando.popularmovies.ui.fragments.FavoriteMoviesGridFragment
import com.asmat.rolando.popularmovies.ui.fragments.WatchLaterMoviesGridFragment

class MyListsPagerAdapter(fm: androidx.fragment.app.FragmentManager, context: Context) : BaseSectionsPagerAdapter(fm, context) {

    override val fragments: Array<androidx.fragment.app.Fragment>
        get() {
            val favorites = FavoriteMoviesGridFragment()
            val watchLater = WatchLaterMoviesGridFragment()
            return arrayOf(favorites, watchLater)
        }

    override val pageTitles: Array<String>
        get() {
            val popular = getString(R.string.favorites)
            val topRated = getString(R.string.watch_later)
            return arrayOf(popular, topRated)
        }
}
