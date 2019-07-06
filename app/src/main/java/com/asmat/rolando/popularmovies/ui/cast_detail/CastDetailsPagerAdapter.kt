package com.asmat.rolando.popularmovies.ui.cast_detail

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.ui.adapters.pager.BaseSectionsPagerAdapter

class CastDetailsPagerAdapter(private val uiModel: PersonDetailsUiModel, fm: FragmentManager, context: Context) : BaseSectionsPagerAdapter(fm, context) {

    override val fragments: Array<Fragment>
        get() {
            val personDetailsFragment = PersonDetailsFragment.newInstance(uiModel)
            val personMovieCreditsFragment = PersonMovieCreditsFragment.newInstance("", "")
            return arrayOf(personDetailsFragment, personMovieCreditsFragment)
        }

    override val pageTitles: Array<String>
        get() {
            val info = getString(R.string.info)
            val movieCredits = getString(R.string.movie_credits)
            return arrayOf(info, movieCredits)
        }

}