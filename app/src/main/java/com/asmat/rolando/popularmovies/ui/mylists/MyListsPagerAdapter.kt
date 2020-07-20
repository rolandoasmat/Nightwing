package com.asmat.rolando.popularmovies.ui.mylists

import androidx.fragment.app.Fragment
import com.asmat.rolando.popularmovies.ui.common.BaseSectionsPagerAdapter
import com.asmat.rolando.popularmovies.ui.favoritemovies.FavoriteMoviesGridFragment
import com.asmat.rolando.popularmovies.ui.moviegrid.BaseMovieGridFragment
import com.asmat.rolando.popularmovies.ui.watchlatermovies.WatchLaterMoviesGridFragment

class MyListsPagerAdapter(fragment: Fragment, val callbacks: BaseMovieGridFragment.MovieGridCallbacks) : BaseSectionsPagerAdapter(fragment) {

    override val fragments: Array<Fragment>
        get() {
            val favorites = FavoriteMoviesGridFragment(callbacks)
            val watchLater = WatchLaterMoviesGridFragment(callbacks)
            return arrayOf(favorites, watchLater)
        }
}
