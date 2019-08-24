package com.asmat.rolando.popularmovies.ui.moviegrid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.asmat.rolando.popularmovies.MovieNightApplication
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.extensions.gone
import com.asmat.rolando.popularmovies.extensions.visible
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.repositories.PeopleRepository
import com.asmat.rolando.popularmovies.ui.moviedetails.MovieDetailActivity
import com.asmat.rolando.popularmovies.utilities.NetworkUtils
import com.asmat.rolando.popularmovies.utilities.ViewUtils
import kotlinx.android.synthetic.main.fragment_movie_grid.*
import kotlinx.android.synthetic.main.retry_layout.*
import javax.inject.Inject

abstract class BaseMovieGridFragment : androidx.fragment.app.Fragment() {

    @Inject
    lateinit var moviesRepository: MoviesRepository

    @Inject
    lateinit var peopleRepository: PeopleRepository

    private var moviesGridAdapter: MoviesGridAdapter? = null

    abstract val viewModel: BaseMovieGridViewModel

    open val requiresInternet = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as MovieNightApplication).component().inject(this)
        observeViewModel()
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
        moviesGridAdapter = MoviesGridAdapter(object : BaseMoviesGridAdapter.Callback {
            override fun itemPressed(index: Int) {
                viewModel.itemPressed(index)
            }
        })
        moviesRecyclerView?.adapter = moviesGridAdapter
        moviesRecyclerView?.isNestedScrollingEnabled = false

        retryButton?.setOnClickListener {
            viewModel.load()
        }
        viewModel.load()
    }

    override fun onResume() {
        super.onResume()
        if (requiresInternet) {
            checkInternet()
        }
    }

    private fun checkInternet() {
        val context = this.context ?: return
        if (!NetworkUtils.isOnline(context)) {
            // User has no internet
            moviesRecyclerView?.gone()
            retryLayout?.visible()
        } else {
            // Internet connection established
            moviesRecyclerView?.visible()
            retryLayout?.gone()
        }
    }

    private fun observeViewModel() {
        viewModel.moviesUIModels.observe(this, Observer { movies ->
            moviesGridAdapter?.setMovies(movies)
        })
        viewModel.error.observe(this, Observer { error ->
            error?.let {
                handleError(it)
            }
        })
        viewModel.navigationEvent.observe(this, Observer { navigationEvent ->
            navigationEvent?.let { event ->
                when (event) {
                    is BaseMovieGridViewModel.NavigationEvent.ShowMovieDetailScreen -> {
                        showMovieDetailScreen()
                    }
                }
            }
        })
    }

    private fun showMovieDetailScreen() {
        val context = context ?: return
        val destinationClass = MovieDetailActivity::class.java
        val intentToStartDetailActivity = Intent(context, destinationClass)
        startActivity(intentToStartDetailActivity)
    }

    private fun handleError(error: Throwable?) {
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