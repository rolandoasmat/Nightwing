package com.asmat.rolando.nightwing.ui.mylists

import androidx.fragment.app.Fragment
import com.asmat.rolando.nightwing.saved_movies.SavedMoviesFragment
import com.asmat.rolando.nightwing.saved_tv_shows.SavedTvShowsFragment
import com.asmat.rolando.nightwing.ui.common.BaseSectionsPagerAdapter

class MyListsPagerAdapter(fragment: Fragment) : BaseSectionsPagerAdapter(fragment) {

    override val fragments: Array<Fragment>
        get() {
            val savedMovies = SavedMoviesFragment()
            val savedTvShows = SavedTvShowsFragment()
            return arrayOf(savedMovies, savedTvShows)
        }
}
