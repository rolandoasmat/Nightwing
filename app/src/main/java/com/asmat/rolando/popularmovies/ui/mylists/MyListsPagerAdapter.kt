package com.asmat.rolando.popularmovies.ui.mylists

import androidx.fragment.app.Fragment
import com.asmat.rolando.popularmovies.ui.common.BaseSectionsPagerAdapter
import com.asmat.rolando.popularmovies.ui.favoritemovies.FavoriteMoviesGridFragment
import com.asmat.rolando.popularmovies.ui.watchlatermovies.WatchLaterMoviesGridFragment

class MyListsPagerAdapter(fragment: Fragment) : BaseSectionsPagerAdapter(fragment) {

    override val fragments: Array<Fragment>
        get() {
            val favorites = FavoriteMoviesGridFragment()
            val watchLater = WatchLaterMoviesGridFragment()
            return arrayOf(favorites, watchLater)
        }
}
