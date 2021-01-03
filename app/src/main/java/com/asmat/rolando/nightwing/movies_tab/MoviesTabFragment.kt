package com.asmat.rolando.nightwing.movies_tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.asmat.rolando.nightwing.NightwingApplication
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.home_tab.HomeTabFragmentDirections
import com.asmat.rolando.nightwing.ui.row_view.RowViewItemUiModel
import com.asmat.rolando.nightwing.ui.row_view.RowView
import com.asmat.rolando.nightwing.ui.nowplayingmovies.NowPlayingMoviesViewModel
import com.asmat.rolando.nightwing.ui.popularmovies.PopularMoviesViewModel
import com.asmat.rolando.nightwing.ui.topratedmovies.TopRatedMoviesViewModel
import com.asmat.rolando.nightwing.ui.upcomingmovies.UpcomingMoviesViewModel
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.movies_tab_fragment.*
import javax.inject.Inject

class MoviesTabFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val popularMoviesViewModel: PopularMoviesViewModel by viewModels { viewModelFactory }
    private val topRatedMoviesViewModel: TopRatedMoviesViewModel by viewModels { viewModelFactory }
    private val nowPlayingMoviesViewModel: NowPlayingMoviesViewModel by viewModels { viewModelFactory }
    private val upcomingMoviesViewModel: UpcomingMoviesViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as? NightwingApplication)?.component()?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.movies_tab_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRows()
        observeLiveData()
    }

    private fun setUpRows() {
        popularMoviesRow.configure(title = "Popular", callback = object: RowView.Callback {
            override fun onSeeAllClicked() {
                val action = HomeTabFragmentDirections.actionGlobalActionToPopularMoviesGrid()
                findNavController().navigate(action)
            }
            override fun onCardClicked(id: Int) {
                val action = HomeTabFragmentDirections.actionGlobalActionToMovieDetailsScreen(id)
                findNavController().navigate(action)
            }
        })
        popularMoviesViewModel.load()

        topRatedMoviesRow.configure(title = "Top Rated", callback = object: RowView.Callback {
            override fun onSeeAllClicked() {
                val action = HomeTabFragmentDirections.actionGlobalActionToTopRatedMoviesGrid()
                findNavController().navigate(action)
            }
            override fun onCardClicked(id: Int) {
                val action = HomeTabFragmentDirections.actionGlobalActionToMovieDetailsScreen(id)
                findNavController().navigate(action)
            }
        })
        topRatedMoviesViewModel.load()

        nowPlayingMoviesRow.configure(title = "Now Playing", callback =  object: RowView.Callback {
            override fun onSeeAllClicked() {
                val action = HomeTabFragmentDirections.actionGlobalActionToNowPlayingMoviesGrid()
                findNavController().navigate(action)
            }
            override fun onCardClicked(id: Int) {
                val action = HomeTabFragmentDirections.actionGlobalActionToMovieDetailsScreen(id)
                findNavController().navigate(action)
            }
        })
        nowPlayingMoviesViewModel.load()

        upcomingMoviesRow.configure(title = "Upcoming", callback = object: RowView.Callback {
            override fun onSeeAllClicked() {
                val action = HomeTabFragmentDirections.actionGlobalActionToUpcomingMoviesGrid()
                findNavController().navigate(action)
            }
            override fun onCardClicked(id: Int) {
                val action = HomeTabFragmentDirections.actionGlobalActionToMovieDetailsScreen(id)
                findNavController().navigate(action)
            }
        })
        upcomingMoviesViewModel.load()
    }

    private fun observeLiveData() {
        popularMoviesViewModel.uiModels.observe(viewLifecycleOwner) {
            val data = it.map { movieGridItem -> RowViewItemUiModel(movieGridItem.id, movieGridItem.posterURL, movieGridItem.title) }
            popularMoviesRow.setData(data)
        }
        topRatedMoviesViewModel.uiModels.observe(viewLifecycleOwner) {
            val data = it.map { movieGridItem -> RowViewItemUiModel(movieGridItem.id, movieGridItem.posterURL, movieGridItem.title) }
            topRatedMoviesRow.setData(data)
        }
        nowPlayingMoviesViewModel.uiModels.observe(viewLifecycleOwner) {
            val data = it.map { movieGridItem -> RowViewItemUiModel(movieGridItem.id, movieGridItem.posterURL, movieGridItem.title) }
            nowPlayingMoviesRow.setData(data)
        }
        upcomingMoviesViewModel.uiModels.observe(viewLifecycleOwner) {
            val data = it.map { movieGridItem -> RowViewItemUiModel(movieGridItem.id, movieGridItem.posterURL, movieGridItem.title) }
            upcomingMoviesRow.setData(data)
        }
    }
}