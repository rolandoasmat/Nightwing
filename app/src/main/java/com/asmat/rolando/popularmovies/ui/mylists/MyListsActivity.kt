package com.asmat.rolando.popularmovies.ui.mylists


import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.ui.common.BaseSectionsPagerAdapter
import com.asmat.rolando.popularmovies.ui.common.BaseActivity

class MyListsActivity : BaseActivity() {

    override val activityTitle: String
        get() = getString(R.string.my_lists)

    override val pagerAdapter: BaseSectionsPagerAdapter
        get() = MyListsPagerAdapter(supportFragmentManager, this)
}

