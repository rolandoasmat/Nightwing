package com.asmat.rolando.nightwing.tv_show_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.tv_shows_tab.top_rated.TopRatedTvShowsViewModel
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory
import javax.inject.Inject

class TvShowDetailsFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    val viewMode: TopRatedTvShowsViewModel by viewModels { viewModelFactory }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tv_show_details, container, false)
    }


}