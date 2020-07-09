package com.asmat.rolando.popularmovies.ui.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

abstract class BaseSectionsPagerAdapterFromActivity internal constructor(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    //region Abstract
    internal abstract val fragments: Array<Fragment>

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]

    //endregion

}
