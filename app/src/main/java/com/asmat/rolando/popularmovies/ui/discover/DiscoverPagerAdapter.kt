package com.asmat.rolando.popularmovies.ui.discover

import androidx.fragment.app.Fragment
import com.asmat.rolando.popularmovies.ui.common.BaseSectionsPagerAdapter
import com.asmat.rolando.popularmovies.ui.moviegrid.BaseMovieGridFragment
import com.asmat.rolando.popularmovies.ui.nowplayingmovies.NowPlayingMoviesFragment
import com.asmat.rolando.popularmovies.ui.popularmovies.PopularMoviesFragment
import com.asmat.rolando.popularmovies.ui.topratedmovies.TopRatedMoviesFragment
import com.asmat.rolando.popularmovies.ui.upcomingmovies.UpcomingMoviesFragment

/**
 * Adapter that shows the Popular, Top Rated, Now Playing, and Coming Soon grids.
 */
class DiscoverPagerAdapter(fragment: Fragment, val callbacks: BaseMovieGridFragment.MovieGridCallbacks) : BaseSectionsPagerAdapter(fragment) {

    override val fragments: Array<Fragment>
        get() {
            val popularMoviesFragment = PopularMoviesFragment(callbacks)
            val topRatedMoviesFragment = TopRatedMoviesFragment(callbacks)
            val nowPlayingMoviesFragment = NowPlayingMoviesFragment(callbacks)
            val upcomingMoviesFragment = UpcomingMoviesFragment(callbacks)
            return arrayOf(popularMoviesFragment, topRatedMoviesFragment, nowPlayingMoviesFragment, upcomingMoviesFragment)
        }
}
