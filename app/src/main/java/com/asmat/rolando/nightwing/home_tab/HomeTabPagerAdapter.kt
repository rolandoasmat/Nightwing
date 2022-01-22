package com.asmat.rolando.nightwing.home_tab

import androidx.fragment.app.Fragment
import com.asmat.rolando.nightwing.movies_tab.MoviesTabFragment
import com.asmat.rolando.nightwing.popular_people_tab.PopularPeopleTabFragment
import com.asmat.rolando.nightwing.tv_shows_tab.TvShowsTabFragment
import com.asmat.rolando.nightwing.ui.common.BaseSectionsPagerAdapter

class HomeTabPagerAdapter(fragment: Fragment): BaseSectionsPagerAdapter(fragment) {

    private val moviesFragment = MoviesTabFragment()
    private val tvShowsTabFragment = TvShowsTabFragment()
    private val popularPeopleGridFragment = PopularPeopleTabFragment()

    override val fragments: Array<Fragment>
        get() {
            return arrayOf(moviesFragment, tvShowsTabFragment, popularPeopleGridFragment)
        }
}
