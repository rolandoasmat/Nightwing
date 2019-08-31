package com.asmat.rolando.popularmovies.ui.moviegrid

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.asmat.rolando.popularmovies.MovieNightApplication
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.extensions.gone
import com.asmat.rolando.popularmovies.extensions.visible
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.repositories.PeopleRepository
import com.asmat.rolando.popularmovies.ui.moviedetails.MovieDetailActivity
import com.asmat.rolando.popularmovies.utilities.ViewUtils
import kotlinx.android.synthetic.main.fragment_movie_grid.*
import kotlinx.android.synthetic.main.retry_layout.*
import javax.inject.Inject

abstract class BaseMovieGridFragment : androidx.fragment.app.Fragment() {

    @Inject
    lateinit var moviesRepository: MoviesRepository

    @Inject
    lateinit var peopleRepository: PeopleRepository

    private var moviesGridAdapter: BaseMoviesGridAdapter? = null

    abstract val viewModel: BaseMovieGridViewModel

    open val requiresInternet = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as MovieNightApplication).component().inject(this)
        observeViewModel()
        viewModel.load()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = context ?: return
        val numOfColumns = ViewUtils.calculateNumberOfColumns(context)
        val layoutManager = GridLayoutManager(context, numOfColumns)
        moviesRecyclerView?.layoutManager = layoutManager
        moviesGridAdapter = BaseMoviesGridAdapter(object : BaseMoviesGridAdapter.Callback {
            override fun itemPressed(index: Int) {
                viewModel.itemPressed(index)
            }
        })
        moviesRecyclerView?.adapter = moviesGridAdapter
        moviesRecyclerView?.isNestedScrollingEnabled = false

        retryButton?.setOnClickListener {
            viewModel.load()
        }

        refreshUI()
        viewModel.load()
    }

    private fun refreshUI() {
        renderMoviesUIModels(viewModel.moviesUIModels.value)
        renderError(viewModel.error.value)
        handleNavigationEvent(viewModel.navigationEvent.value)
    }

    private fun observeViewModel() {
        viewModel.moviesUIModels.observe(this, Observer { movies ->
            renderMoviesUIModels(movies)
        })
        viewModel.error.observe(this, Observer { error ->
            renderError(error)
        })
        viewModel.navigationEvent.observe(this, Observer { navigationEvent ->
            handleNavigationEvent(navigationEvent)
        })
    }

    private fun renderMoviesUIModels(movies: List<MovieGridItemUiModel>?) {
        movies?.let {
            moviesGridAdapter?.setMovies(it)
        }
    }

    private fun handleNavigationEvent(navigationEvent: BaseMovieGridViewModel.NavigationEvent?) {
        navigationEvent?.let { event ->
            when (event) {
                is BaseMovieGridViewModel.NavigationEvent.ShowMovieDetailScreen -> {
                    showMovieDetailScreen()
                }
            }
        }
    }

    private fun showMovieDetailScreen() {
        val context = context ?: return
        val destinationClass = MovieDetailActivity::class.java
        val intentToStartDetailActivity = Intent(context, destinationClass)
        startActivity(intentToStartDetailActivity)
    }

    private fun renderError(error: Throwable?) {
        error?.let {
            val message = getString(R.string.generic_error)
            updateRetryLayout(message)
        } ?: run {
            updateRetryLayout(null)
        }
    }

    /**
     * Sets the message of the retry layout and updates its visibility
     */
    private fun updateRetryLayout(message: String?) {
        retryMessageTextView?.text = message
        if (message?.isEmpty() == false) {
            retryLayout?.visible()
        } else {
            retryLayout?.gone()
        }
    }
}