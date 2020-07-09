package com.asmat.rolando.popularmovies.ui.castdetails

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.asmat.rolando.popularmovies.ui.castdetails.personinfo.PersonDetailsFragment
import com.asmat.rolando.popularmovies.ui.castdetails.personmoviecredits.PersonMovieCreditsFragment
import com.asmat.rolando.popularmovies.ui.common.BaseSectionsPagerAdapterFromActivity

class CastDetailsPagerAdapter(private val uiModel: CastDetailsUiModel, fragmentActivity: FragmentActivity) : BaseSectionsPagerAdapterFromActivity(fragmentActivity) {

    override val fragments: Array<Fragment>
        get() {
            val personDetailsFragment = PersonDetailsFragment.newInstance(uiModel.personInfoUiModel)
            val personMovieCreditsFragment = PersonMovieCreditsFragment.newInstance(uiModel.personID)
            return arrayOf(personDetailsFragment, personMovieCreditsFragment)
        }

}