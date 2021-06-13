package com.asmat.rolando.nightwing.tv_season_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asmat.rolando.nightwing.NightwingApplication
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_tv_season_details.*
import javax.inject.Inject

class TvSeasonDetailsFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    val viewModel: TvSeasonDetailsViewModel by viewModels { viewModelFactory }

    val args: TvSeasonDetailsFragmentArgs by navArgs()

    private var tvSeasonEpisodesAdapter: TvSeasonEpisodesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tv_season_details, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as NightwingApplication).component().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvSeasonEpisodesAdapter = TvSeasonEpisodesAdapter()
        tvEpisodesRecyclerView?.adapter = tvSeasonEpisodesAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        tvEpisodesRecyclerView?.layoutManager = layoutManager
        observeViewModel()
        val tvShowId = args.tvShowIdArg
        val tvSeasonNumber = args.tvShowSeasonNumberArg
        viewModel.fetchEpisodes(tvShowId, tvSeasonNumber)
    }

    private fun observeViewModel() {
        viewModel.episodes.observe(viewLifecycleOwner) {
            tvSeasonEpisodesAdapter?.data = it
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tvSeasonEpisodesAdapter = null
    }

}