package com.asmat.rolando.popularmovies.ui.discover

import android.content.Context
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.ui.common.BaseSectionsPagerAdapter
import com.asmat.rolando.popularmovies.ui.nowplayingmovies.NowPlayingMoviesFragment
import com.asmat.rolando.popularmovies.ui.popularmovies.PopularMoviesFragment
import com.asmat.rolando.popularmovies.ui.topratedmovies.TopRatedMoviesFragment
import com.asmat.rolando.popularmovies.ui.upcomingmovies.UpcomingMoviesFragment

/**
 * Adapter that shows the Popular, Top Rated, Now Playing, and Coming Soon grids.
 */
class DiscoverPagerAdapter(fm: androidx.fragment.app.FragmentManager, context: Context) : BaseSectionsPagerAdapter(fm, context) {

    override val fragments: Array<androidx.fragment.app.Fragment>
        get() {
            val popularMoviesFragment = PopularMoviesFragment()
            val topRatedMoviesFragment = TopRatedMoviesFragment()
            val nowPlayingMoviesFragment = NowPlayingMoviesFragment()
            val upcomingMoviesFragment = UpcomingMoviesFragment()
            return arrayOf(popularMoviesFragment, topRatedMoviesFragment, nowPlayingMoviesFragment, upcomingMoviesFragment)
        }

    override val pageTitles: Array<String>
        get() {
            val popular = getString(R.string.most_popular)
            val topRated = getString(R.string.top_rated)
            val nowPlaying = getString(R.string.now_playing)
            val upcoming = getString(R.string.upcoming)
            return arrayOf(popular, topRated, nowPlaying, upcoming)
        }
}
