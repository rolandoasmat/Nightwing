package com.asmat.rolando.popularmovies.ui.castdetails

import androidx.fragment.app.Fragment
import com.asmat.rolando.popularmovies.ui.common.BaseSectionsPagerAdapter
import com.asmat.rolando.popularmovies.ui.castdetails.personinfo.PersonDetailsFragment
import com.asmat.rolando.popularmovies.ui.castdetails.personmoviecredits.PersonMovieCreditsFragment

class CastDetailsPagerAdapter(private val uiModel: CastDetailsUiModel, fragment: Fragment) : BaseSectionsPagerAdapter(fragment) {

    override val fragments: Array<Fragment>
        get() {
            val personDetailsFragment = PersonDetailsFragment.newInstance(uiModel.personInfoUiModel)
            val personMovieCreditsFragment = PersonMovieCreditsFragment.newInstance(uiModel.personID)
            return arrayOf(personDetailsFragment, personMovieCreditsFragment)
        }

}