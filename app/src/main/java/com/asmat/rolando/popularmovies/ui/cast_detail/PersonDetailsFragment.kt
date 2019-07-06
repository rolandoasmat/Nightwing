package com.asmat.rolando.popularmovies.ui.cast_detail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.ui.transformations.RoundedTransformation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.person_details.*

private const val ARG_PHOTO_URL = "ARG_PHOTO_URL"
private const val ARG_HOMETOWN = "ARG_HOMETOWN"
private const val ARG_BIRTHDATE = "ARG_BIRTHDATE"
private const val ARG_DEATHDATE = "ARG_DEATHDATE"
private const val ARG_BIO = "ARG_BIO"

class PersonDetailsFragment : Fragment() {

    companion object {
        fun newInstance(uiModel: PersonDetailsUiModel) =
                PersonDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PHOTO_URL, uiModel.photoURL)
                        putString(ARG_HOMETOWN, uiModel.hometown)
                        putString(ARG_BIRTHDATE, uiModel.birthdate)
                        putString(ARG_DEATHDATE, uiModel.deathdate)
                        putString(ARG_BIO, uiModel.bio)
                    }
                }
    }

    private val photoURL: String by lazy { arguments?.getString(ARG_PHOTO_URL, "") ?: "" }
    private val homeTownText: String by lazy { arguments?.getString(ARG_HOMETOWN, "") ?: "" }
    private val birthdateText: String by lazy { arguments?.getString(ARG_BIRTHDATE, "") ?: "" }
    private val deathdateText: String by lazy { arguments?.getString(ARG_DEATHDATE, "") ?: "" }
    private val bioText: String by lazy { arguments?.getString(ARG_BIO, "") ?: "" }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.person_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    private fun updateUI() {
        Picasso.with(activity)
                .load(photoURL)
                .resize(342, 513)
                .centerCrop()
                .transform(RoundedTransformation(50, 0))
                .error(R.drawable.person)
                .into(poster)
        homeTown?.text = homeTownText
        birthdate?.text = birthdateText
        deathdate?.text = deathdateText
        biography?.text = bioText
    }

}