package com.asmat.rolando.popularmovies.ui.mylists

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.ui.common.BaseSectionsPagerAdapter
import com.asmat.rolando.popularmovies.ui.fragments.FavoriteMoviesGridFragment
import com.asmat.rolando.popularmovies.ui.fragments.WatchLaterMoviesGridFragment

class MyListsPagerAdapter(fm: FragmentManager, context: Context) : BaseSectionsPagerAdapter(fm, context) {

    override val fragments: Array<Fragment>
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
