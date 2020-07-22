package com.asmat.rolando.popularmovies.ui.discover

import androidx.fragment.app.Fragment
import com.asmat.rolando.popularmovies.ui.common.BaseSectionsPagerAdapter
import com.asmat.rolando.popularmovies.ui.nowplayingmovies.NowPlayingMoviesFragment
import com.asmat.rolando.popularmovies.ui.popularmovies.PopularMoviesFragment
import com.asmat.rolando.popularmovies.ui.topratedmovies.TopRatedMoviesFragment
import com.asmat.rolando.popularmovies.ui.upcomingmovies.UpcomingMoviesFragment

/**
 * Adapter that shows the Popular, Top Rated, Now Playing, and Coming Soon grids.
 */
class DiscoverPagerAdapter(fragment: Fragment): BaseSectionsPagerAdapter(fragment) {

    override val fragments: Array<Fragment>
        get() {
            val popularMoviesFragment = PopularMoviesFragment()
            val topRatedMoviesFragment = TopRatedMoviesFragment()
            val nowPlayingMoviesFragment = NowPlayingMoviesFragment()
            val upcomingMoviesFragment = UpcomingMoviesFragment()
            return arrayOf(popularMoviesFragment, topRatedMoviesFragment, nowPlayingMoviesFragment, upcomingMoviesFragment)
        }
}
