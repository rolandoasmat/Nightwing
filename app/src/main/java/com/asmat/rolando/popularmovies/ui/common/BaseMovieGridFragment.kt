package com.asmat.rolando.popularmovies.ui.common

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asmat.rolando.popularmovies.MovieNightApplication
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.extensions.gone
import com.asmat.rolando.popularmovies.extensions.visible
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.repositories.PeopleRepository
import com.asmat.rolando.popularmovies.ui.moviedetails.MovieDetailActivity
import com.asmat.rolando.popularmovies.ui.moviedetails.MovieDetailsUIModel
import com.asmat.rolando.popularmovies.utilities.NetworkUtils
import com.asmat.rolando.popularmovies.utilities.ViewUtils
import kotlinx.android.synthetic.main.fragment_movie_grid.*
import kotlinx.android.synthetic.main.no_internet.*
import javax.inject.Inject

abstract class BaseMovieGridFragment : androidx.fragment.app.Fragment() {

    @Inject
    lateinit var moviesRepository: MoviesRepository

    @Inject
    lateinit var peopleRepository: PeopleRepository

    protected var moviesGridAdapter: MoviesGridAdapter? = null

    abstract val viewModel: BaseMovieGridViewModel

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
        moviesGridAdapter = MoviesGridAdapter(object: BaseMoviesGridAdapter.Callback {
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
        checkInternet()
    }

    private fun checkInternet() {
        val context = this.context ?: return
        if (!NetworkUtils.isOnline(context)) {
            // User has no internet
            moviesRecyclerView?.gone()
            noInternetLayout?.visible()
        } else {
            // Internet connection established
            moviesRecyclerView?.visible()
            noInternetLayout?.gone()
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
                        showMovieDetailScreen(event.uiModel)
                    }
                }
            }
        })
    }

    private fun showMovieDetailScreen(data: MovieDetailsUIModel) {
        val context = context ?: return
        val destinationClass = MovieDetailActivity::class.java
        val intentToStartDetailActivity = Intent(context, destinationClass)
        intentToStartDetailActivity.putExtra(MovieDetailActivity.INTENT_EXTRA_MOVIE_DATA, data)
        startActivity(intentToStartDetailActivity)
    }

    private fun handleError(error: Throwable) {
        val message = error.message ?: "Whoops, error fetching movies."
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast.show()
        Log.e("PaginatedMovieGridFragment", error.toString())
    }
}