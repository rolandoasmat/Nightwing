package com.asmat.rolando.popularmovies.ui.activities


import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.ui.adapters.pager.BaseSectionsPagerAdapter
import com.asmat.rolando.popularmovies.ui.adapters.pager.DiscoverPagerAdapter

class DiscoverActivity : BaseActivity() {

    override val activityTitle: String
        get() = getString(R.string.discover)

    override val pagerAdapter: BaseSectionsPagerAdapter
        get() = DiscoverPagerAdapter(supportFragmentManager, this)
}