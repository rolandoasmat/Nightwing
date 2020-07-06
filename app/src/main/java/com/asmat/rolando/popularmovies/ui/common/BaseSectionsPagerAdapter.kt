package com.asmat.rolando.popularmovies.ui.common

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

abstract class BaseSectionsPagerAdapter internal constructor(fragment: Fragment) : FragmentStateAdapter(fragment) {

    //region Abstract
    internal abstract val fragments: Array<Fragment>

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]

    //endregion

}
