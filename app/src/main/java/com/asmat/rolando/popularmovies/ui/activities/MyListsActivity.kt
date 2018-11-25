package com.asmat.rolando.popularmovies.ui.activities


import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.ui.adapters.pager.BaseSectionsPagerAdapter
import com.asmat.rolando.popularmovies.ui.adapters.pager.MyListsPagerAdapter

class MyListsActivity : BaseActivity() {

    override val activityTitle: String
        get() = getString(R.string.my_lists)

    override val pagerAdapter: BaseSectionsPagerAdapter
        get() = MyListsPagerAdapter(supportFragmentManager, this)
}

