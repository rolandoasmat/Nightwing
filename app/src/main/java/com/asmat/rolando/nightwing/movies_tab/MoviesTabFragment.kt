package com.asmat.rolando.nightwing.movies_tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.asmat.rolando.nightwing.NightwingApplication
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.home_tab.HomeTabFragmentDirections
import com.asmat.rolando.nightwing.ui.row_view.RowView
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_movies_tab.*
import javax.inject.Inject

class MoviesTabFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val popularMoviesViewModel: PopularMoviesRowViewModel by viewModels { viewModelFactory }
    private val topRatedMoviesViewModel: TopRatedMoviesRowViewModel by viewModels { viewModelFactory }
    private val nowPlayingMoviesViewModel: NowPlayingMoviesRowViewModel by viewModels { viewModelFactory }
    private val upcomingMoviesViewModel: UpcomingMoviesRowViewModel by viewModels { viewModelFactory }

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
        popularMoviesRow.configure(title = "Popular Movies", callback = object: RowView.Callback {
            override fun onSeeAllClicked() {
                val action = HomeTabFragmentDirections.actionGlobalActionToPopularMoviesGrid()
                findNavController().navigate(action)
            }
            override fun onCardClicked(id: Int, view: View) {
                // Do shared element transition
                val action = HomeTabFragmentDirections.globalActionHomeTabToMovieDetailsScreen(id)
                val name = "movie_grid_to_details"
                view.findViewById<ImageView>(R.id.itemRowCardImage)?.let { imageView ->
                    ViewCompat.setTransitionName(imageView, name)
                    val extras = FragmentNavigatorExtras(imageView to name)
                    findNavController().navigate(action, extras)
                }
            }
            override fun onRetry() {
                popularMoviesViewModel.load()
            }
        })
        popularMoviesViewModel.load()

        topRatedMoviesRow.configure(title = "Top Rated", callback = object: RowView.Callback {
            override fun onSeeAllClicked() {
                val action = HomeTabFragmentDirections.actionGlobalActionToTopRatedMoviesGrid()
                findNavController().navigate(action)
            }
            override fun onCardClicked(id: Int, view: View) {
                val action = HomeTabFragmentDirections.globalActionHomeTabToMovieDetailsScreen(id)
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
            override fun onCardClicked(id: Int, view: View) {
                val action = HomeTabFragmentDirections.globalActionHomeTabToMovieDetailsScreen(id)
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
            override fun onCardClicked(id: Int, view: View) {
                val action = HomeTabFragmentDirections.globalActionHomeTabToMovieDetailsScreen(id)
                findNavController().navigate(action)
            }
            override fun onRetry() = upcomingMoviesViewModel.load()
        })
        upcomingMoviesViewModel.load()
    }

    private fun observeLiveData() {
        popularMoviesRow.observe(popularMoviesViewModel, viewLifecycleOwner)
        topRatedMoviesRow.observe(topRatedMoviesViewModel, viewLifecycleOwner)
        nowPlayingMoviesRow.observe(nowPlayingMoviesViewModel, viewLifecycleOwner)
        upcomingMoviesRow.observe(upcomingMoviesViewModel, viewLifecycleOwner)
    }
}