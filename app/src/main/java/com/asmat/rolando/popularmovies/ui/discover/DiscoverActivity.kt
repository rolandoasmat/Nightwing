package com.asmat.rolando.popularmovies.ui.discover


import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.ui.common.BaseSectionsPagerAdapter
import com.asmat.rolando.popularmovies.ui.mylists.DiscoverPagerAdapter
import com.asmat.rolando.popularmovies.ui.common.BaseActivity

class DiscoverActivity : BaseActivity() {

    override val activityTitle: String
        get() = getString(R.string.discover)

    override val pagerAdapter: BaseSectionsPagerAdapter
        get() = DiscoverPagerAdapter(supportFragmentManager, this)
}