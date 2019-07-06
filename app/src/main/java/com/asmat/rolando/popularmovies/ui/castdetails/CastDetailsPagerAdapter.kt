package com.asmat.rolando.popularmovies.ui.castdetails

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.ui.common.BaseSectionsPagerAdapter
import com.asmat.rolando.popularmovies.ui.castdetails.personinfo.PersonDetailsFragment
import com.asmat.rolando.popularmovies.ui.castdetails.personinfo.PersonInfoUiModel
import com.asmat.rolando.popularmovies.ui.castdetails.personmoviecredits.PersonMovieCreditsFragment

class CastDetailsPagerAdapter(private val uiModel: PersonInfoUiModel, fm: FragmentManager, context: Context) : BaseSectionsPagerAdapter(fm, context) {

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