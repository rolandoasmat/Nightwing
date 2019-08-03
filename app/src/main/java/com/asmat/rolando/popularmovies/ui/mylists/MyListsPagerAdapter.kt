package com.asmat.rolando.popularmovies.ui.mylists

import android.content.Context
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.ui.common.BaseSectionsPagerAdapter
import com.asmat.rolando.popularmovies.ui.favoritemovies.FavoriteMoviesGridFragment
import com.asmat.rolando.popularmovies.ui.watchlatermovies.WatchLaterMoviesGridFragment

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
