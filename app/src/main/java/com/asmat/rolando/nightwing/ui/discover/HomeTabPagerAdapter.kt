package com.asmat.rolando.nightwing.ui.discover

import androidx.fragment.app.Fragment
import com.asmat.rolando.nightwing.movies_tab.MoviesTabFragment
import com.asmat.rolando.nightwing.popular_people_tab.PopularPeopleTabFragment
import com.asmat.rolando.nightwing.tv_shows_tab.TvShowsTabFragment
import com.asmat.rolando.nightwing.ui.common.BaseSectionsPagerAdapter

class HomeTabPagerAdapter(fragment: Fragment): BaseSectionsPagerAdapter(fragment) {

    override val fragments: Array<Fragment>
        get() {
            val moviesFragment = MoviesTabFragment()
            val tvShowsTabFragment = TvShowsTabFragment()
            val popularPeopleGridFragment = PopularPeopleTabFragment()
            return arrayOf(moviesFragment, tvShowsTabFragment, popularPeopleGridFragment)
        }
}
