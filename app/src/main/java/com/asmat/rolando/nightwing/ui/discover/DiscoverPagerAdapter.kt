package com.asmat.rolando.nightwing.ui.discover

import androidx.fragment.app.Fragment
import com.asmat.rolando.nightwing.movies.MoviesTabFragment
import com.asmat.rolando.nightwing.popular_people.PopularPeopleGridFragment
import com.asmat.rolando.nightwing.ui.common.BaseSectionsPagerAdapter
import com.asmat.rolando.nightwing.ui.topratedmovies.TopRatedMoviesFragment

class DiscoverPagerAdapter(fragment: Fragment): BaseSectionsPagerAdapter(fragment) {

    override val fragments: Array<Fragment>
        get() {
            val moviesFragment = MoviesTabFragment()
            val topRatedMoviesFragment = TopRatedMoviesFragment()
            val popularPeopleGridFragment = PopularPeopleGridFragment()
            return arrayOf(moviesFragment, topRatedMoviesFragment, popularPeopleGridFragment)
        }
}
