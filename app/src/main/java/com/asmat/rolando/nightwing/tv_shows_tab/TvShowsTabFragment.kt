package com.asmat.rolando.nightwing.tv_shows_tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.asmat.rolando.nightwing.HomeDirections
import com.asmat.rolando.nightwing.NightwingApplication
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.home_tab.HomeTabFragmentDirections
import com.asmat.rolando.nightwing.ui.row_view.RowView
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_tv_shows_tab.*
import javax.inject.Inject

class TvShowsTabFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val viewModel: TvShowsTabViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as NightwingApplication).component().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tv_shows_tab, container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRows()
    }

    private fun setupRows() {
        popularTvShowsRow.configure(title = "Popular", seeAllButtonEnabled = true, callback = object: RowView.Callback{
            override fun onSeeAllClicked() {
                val action = HomeTabFragmentDirections.actionGlobalActionToPopularTvShowsFragment()
                findNavController().navigate(action)
            }
            override fun onCardClicked(id: Int) = navigateToTvShowDetails(id)
        })
        topRatedTvShowsRow.configure(title = "Top Rated", seeAllButtonEnabled = true, callback = object: RowView.Callback{
            override fun onSeeAllClicked() {
                val action = HomeTabFragmentDirections.actionGlobalActionToTopRatedTvShowsFragment()
                findNavController().navigate(action)
            }
            override fun onCardClicked(id: Int) = navigateToTvShowDetails(id)
        })
        onTheAirTvShowsRow.configure(title = "On The Air", seeAllButtonEnabled = true, callback = object: RowView.Callback{
            override fun onSeeAllClicked() {
                val action = HomeTabFragmentDirections.actionGlobalActionToOnTheAirTvShowsFragment()
                findNavController().navigate(action)
            }
            override fun onCardClicked(id: Int) = navigateToTvShowDetails(id)
        })
    }

    private fun navigateToTvShowDetails(id: Int) {
        val action = HomeDirections.actionGlobalActionToMovieDetailsScreen(id)
        findNavController().navigate(action)
    }

    private fun observeViewModel() {
        viewModel.popularTvShows.observe(viewLifecycleOwner) {
            popularTvShowsRow.setData(it)
        }
        viewModel.topRatedTvShows.observe(viewLifecycleOwner) {
            topRatedTvShowsRow.setData(it)
        }
        viewModel.onTheAirTvShows.observe(viewLifecycleOwner) {
            onTheAirTvShowsRow.setData(it)
        }
    }
}