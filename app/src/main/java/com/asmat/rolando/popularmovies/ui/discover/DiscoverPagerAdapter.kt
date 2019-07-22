package com.asmat.rolando.popularmovies.ui.discover

import android.content.Context
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.ui.common.MovieGridFragment
import com.asmat.rolando.popularmovies.ui.common.BaseSectionsPagerAdapter

/**
 * Adapter that shows the Popular, Top Rated, Now Playing, and Coming Soon grids.
 */
class DiscoverPagerAdapter(fm: androidx.fragment.app.FragmentManager, context: Context) : BaseSectionsPagerAdapter(fm, context) {

    override val fragments: Array<androidx.fragment.app.Fragment>
        get() {
            val popular = MovieGridFragment()
            popular.setTypeOfMovies(RequestType.MOST_POPULAR)
            val topRated = MovieGridFragment()
            topRated.setTypeOfMovies(RequestType.TOP_RATED)
            val nowPlaying = MovieGridFragment()
            nowPlaying.setTypeOfMovies(RequestType.NOW_PLAYING)
            val upcoming = MovieGridFragment()
            upcoming.setTypeOfMovies(RequestType.UPCOMING)
            return arrayOf(popular, topRated, nowPlaying, upcoming)
        }

    override val pageTitles: Array<String>
        get() {
            val popular = getString(R.string.most_popular)
            val topRated = getString(R.string.top_rated)
            val nowPlaying = getString(R.string.now_playing)
            val upcoming = getString(R.string.coming_soon)
            return arrayOf(popular, topRated, nowPlaying, upcoming)
        }
}
