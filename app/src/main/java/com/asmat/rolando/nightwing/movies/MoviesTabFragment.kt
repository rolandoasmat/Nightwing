package com.asmat.rolando.nightwing.movies

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
import com.asmat.rolando.nightwing.home.HomeFragmentDirections
import com.asmat.rolando.nightwing.movie_details.MovieCardUIModel
import com.asmat.rolando.nightwing.movie_details.MoviesLinearAdapter
import com.asmat.rolando.nightwing.ui.common.BaseLinearAdapter
import com.asmat.rolando.nightwing.ui.movie_row.MovieRowView
import com.asmat.rolando.nightwing.ui.nowplayingmovies.NowPlayingMoviesViewModel
import com.asmat.rolando.nightwing.ui.popularmovies.PopularMoviesViewModel
import com.asmat.rolando.nightwing.ui.topratedmovies.TopRatedMoviesViewModel
import com.asmat.rolando.nightwing.ui.upcomingmovies.UpcomingMoviesViewModel
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.movies_tab_fragment.*
import javax.inject.Inject

class MoviesTabFragment: Fragment(), BaseLinearAdapter.Callback<MovieCardUIModel> {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val popularMoviesViewModel: PopularMoviesViewModel by viewModels { viewModelFactory }
    private val topRatedMoviesViewModel: TopRatedMoviesViewModel by viewModels { viewModelFactory }
    private val nowPlayingMoviesViewModel: NowPlayingMoviesViewModel by viewModels { viewModelFactory }
    private val upcomingMoviesViewModel: UpcomingMoviesViewModel by viewModels { viewModelFactory }

    private val popularMoviesAdapter = MoviesLinearAdapter(this)
    private val topRatedMoviesAdapter = MoviesLinearAdapter(this)
    private val nowPlayingMoviesAdapter = MoviesLinearAdapter(this)
    private val upcomingMoviesAdapter = MoviesLinearAdapter(this)

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
        popularMoviesRow.setTitle("Popular")
        popularMoviesRow.setAdapter(popularMoviesAdapter)
        popularMoviesRow.setCallback(object: MovieRowView.Callback {
            override fun onSeeAllClicked() {
                val action = HomeFragmentDirections.actionGlobalActionToPopularMoviesGrid()
                findNavController().navigate(action)
            }
        })
        popularMoviesViewModel.load()

        topRatedMoviesRow.setTitle("Top Rated")
        topRatedMoviesRow.setAdapter(topRatedMoviesAdapter)
        topRatedMoviesRow.setCallback(object: MovieRowView.Callback {
            override fun onSeeAllClicked() {
                val action = HomeFragmentDirections.actionGlobalActionToTopRatedMoviesGrid()
                findNavController().navigate(action)
            }
        })
        topRatedMoviesViewModel.load()

        nowPlayingMoviesRow.setTitle("Now Playing")
        nowPlayingMoviesRow.setAdapter(nowPlayingMoviesAdapter)
        nowPlayingMoviesRow.setCallback(object: MovieRowView.Callback {
            override fun onSeeAllClicked() {
                val action = HomeFragmentDirections.actionGlobalActionToNowPlayingMoviesGrid()
                findNavController().navigate(action)
            }
        })
        nowPlayingMoviesViewModel.load()

        upcomingMoviesRow.setTitle("Upcoming")
        upcomingMoviesRow.setAdapter(upcomingMoviesAdapter)
        upcomingMoviesRow.setCallback(object: MovieRowView.Callback {
            override fun onSeeAllClicked() {
                val action = HomeFragmentDirections.actionGlobalActionToUpcomingMoviesGrid()
                findNavController().navigate(action)
            }
        })
        upcomingMoviesViewModel.load()
    }

    private fun observeLiveData() {
        popularMoviesViewModel.uiModels.observe(viewLifecycleOwner) {
            popularMoviesAdapter.data = it.map { movieGridItem -> MovieCardUIModel(movieGridItem.id, movieGridItem.posterURL, movieGridItem.title) }
        }
        topRatedMoviesViewModel.uiModels.observe(viewLifecycleOwner) {
            topRatedMoviesAdapter.data = it.map { movieGridItem -> MovieCardUIModel(movieGridItem.id, movieGridItem.posterURL, movieGridItem.title) }
        }
        nowPlayingMoviesViewModel.uiModels.observe(viewLifecycleOwner) {
            nowPlayingMoviesAdapter.data = it.map { movieGridItem -> MovieCardUIModel(movieGridItem.id, movieGridItem.posterURL, movieGridItem.title) }
        }
        upcomingMoviesViewModel.uiModels.observe(viewLifecycleOwner) {
            upcomingMoviesAdapter.data = it.map { movieGridItem -> MovieCardUIModel(movieGridItem.id, movieGridItem.posterURL, movieGridItem.title) }
        }
    }

    override fun cardClicked(item: MovieCardUIModel) {
        val action = HomeFragmentDirections.actionGlobalActionToMovieDetailsScreen(item.id)
        findNavController().navigate(action)
    }
}