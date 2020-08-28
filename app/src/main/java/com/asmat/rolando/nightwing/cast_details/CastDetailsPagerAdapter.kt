package com.asmat.rolando.nightwing.cast_details

import androidx.fragment.app.Fragment
import com.asmat.rolando.nightwing.ui.common.BaseSectionsPagerAdapter

class CastDetailsPagerAdapter(private val uiModel: CastDetailsUiModel, fragment: Fragment): BaseSectionsPagerAdapter(fragment) {

    override val fragments: Array<Fragment>
        get() {
            val personDetailsFragment = PersonDetailsFragment.newInstance(uiModel.personInfoUiModel)
            val personMovieCreditsFragment = CastMovieCreditsFragment.newInstance(uiModel.personID)
            return arrayOf(personDetailsFragment, personMovieCreditsFragment)
        }

}