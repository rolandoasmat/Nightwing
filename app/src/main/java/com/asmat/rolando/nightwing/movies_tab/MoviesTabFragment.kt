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
import com.asmat.rolando.nightwing.ui.topratedmovies.TopRatedMoviesViewModel
import com.asmat.rolando.nightwing.ui.upcomingmovies.UpcomingMoviesViewModel
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_movies_tab.*
import javax.inject.Inject

class MoviesTabFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val popularMoviesRowViewModel: PopularMoviesRowViewModel by viewModels { viewModelFactory }
    private val topRatedMoviesViewModel: TopRatedMoviesViewModel by viewModels { viewModelFactory }
    private val nowPlayingMoviesViewModel: NowPlayingMoviesViewModel by viewModels { viewModelFactory }
    private val upcomingMoviesViewModel: UpcomingMoviesViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as? NightwingApplication)?.component()?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movies_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRows()
        observeLiveData()
    }

    private fun configureRows() {
        popularMoviesRow.configure(title = "Popular", callback = object: RowView.Callback {
            override fun onSeeAllClicked() {
                val action = HomeTabFragmentDirections.actionGlobalActionToPopularMoviesGrid()
                findNavController().navigate(action)
            }
            override fun onCardClicked(id: Int) {
                val action = HomeTabFragmentDirections.actionGlobalActionToMovieDetailsScreen(id)
                findNavController().navigate(action)
            }
            override fun onRetry() {
                popularMoviesRowViewModel.load()
            }
        })
        popularMoviesRowViewModel.load()

        topRatedMoviesRow.configure(title = "Top Rated", callback = object: RowView.Callback {
            override fun onSeeAllClicked() {
                val action = HomeTabFragmentDirections.actionGlobalActionToTopRatedMoviesGrid()
                findNavController().navigate(action)
            }
            override fun onCardClicked(id: Int) {
                val action = HomeTabFragmentDirections.actionGlobalActionToMovieDetailsScreen(id)
                findNavController().navigate(action)
            }
            override fun onRetry() = topRatedMoviesViewModel.load()
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
            override fun onRetry() = nowPlayingMoviesViewModel.load()
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
            override fun onRetry() = upcomingMoviesViewModel.load()
        })
        upcomingMoviesViewModel.load()
    }

    private fun observeLiveData() {
        observePopularMoviesViewModel()
        observeTopRatedMoviesViewModel()
        observeNowPlayingMoviesViewModel()
        observeUpcomingMoviesViewModel()
    }

    private fun observePopularMoviesViewModel() {
//        popularMoviesViewModel.uiModels.observe(viewLifecycleOwner) {
//            val data = it.map { movieGridItem -> RowViewItemUiModel(movieGridItem.id, movieGridItem.posterURL, movieGridItem.title) }
//            popularMoviesRow.setData(data)
//        }
//        popularMoviesViewModel.loading.observe(viewLifecycleOwner) {
//            popularMoviesRow.setLoading(it == true)
//        }
//        popularMoviesViewModel.error.observe(viewLifecycleOwner) {
//            popularMoviesRow.setRetry(it != null)
//        }
        popularMoviesRowViewModel.rowViewUiModel.observe(viewLifecycleOwner) {
            popularMoviesRow.setData(it.items)
        }
    }

    private fun observeTopRatedMoviesViewModel() {
        topRatedMoviesViewModel.uiModels.observe(viewLifecycleOwner) {
            val data = it.map { movieGridItem -> RowViewItemUiModel(movieGridItem.id, movieGridItem.posterURL, movieGridItem.title) }
            topRatedMoviesRow.setData(data)
        }
        topRatedMoviesViewModel.loading.observe(viewLifecycleOwner) {
            topRatedMoviesRow.setLoading(it == true)
        }
        topRatedMoviesViewModel.error.observe(viewLifecycleOwner) {
            topRatedMoviesRow.setRetry(it != null)
        }
    }

    private fun observeNowPlayingMoviesViewModel() {
        nowPlayingMoviesViewModel.uiModels.observe(viewLifecycleOwner) {
            val data = it.map { movieGridItem -> RowViewItemUiModel(movieGridItem.id, movieGridItem.posterURL, movieGridItem.title) }
            nowPlayingMoviesRow.setData(data)
        }
        nowPlayingMoviesViewModel.loading.observe(viewLifecycleOwner) {
            nowPlayingMoviesRow.setLoading(it == true)
        }
        nowPlayingMoviesViewModel.error.observe(viewLifecycleOwner) {
            nowPlayingMoviesRow.setRetry(it != null)
        }
    }

    private fun observeUpcomingMoviesViewModel() {
        upcomingMoviesViewModel.uiModels.observe(viewLifecycleOwner) {
            val data = it.map { movieGridItem -> RowViewItemUiModel(movieGridItem.id, movieGridItem.posterURL, movieGridItem.title) }
            upcomingMoviesRow.setData(data)
        }
        upcomingMoviesViewModel.loading.observe(viewLifecycleOwner) {
            upcomingMoviesRow.setLoading(it == true)
        }
        upcomingMoviesViewModel.error.observe(viewLifecycleOwner) {
            upcomingMoviesRow.setRetry(it != null)
        }
    }
}