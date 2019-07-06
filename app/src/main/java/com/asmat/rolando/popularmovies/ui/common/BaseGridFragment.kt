package com.asmat.rolando.popularmovies.ui.common

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asmat.rolando.popularmovies.MovieNightApplication

import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.moviedetails.MovieDetailActivity
import com.asmat.rolando.popularmovies.utilities.ViewUtils
import kotlinx.android.synthetic.main.fragment_movie_grid.view.*
import javax.inject.Inject

abstract class BaseGridFragment : Fragment(), MovieAdapterOnClickHandler {

    @Inject
    lateinit var moviesRepository: MoviesRepository

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BaseMoviesGridAdapter

    internal abstract val movieSource: LiveData<List<Movie>>
    internal abstract fun getAdapter(handler: MovieAdapterOnClickHandler): BaseMoviesGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as MovieNightApplication).component().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false)
        recyclerView = rootView.rv_movie_grid
        adapter = getAdapter(this)
        recyclerView.adapter = adapter
        context?.let {
            val layoutManager = GridLayoutManager(it, ViewUtils.calculateNumberOfColumns(it))
            recyclerView.layoutManager = layoutManager
            recyclerView.setHasFixedSize(true)
        }
        return rootView
    }

    override fun onResume() {
        super.onResume()
        fetchMovies()
    }

    override fun onClick(movie: Movie) {
        val destinationClass = MovieDetailActivity::class.java
        val intentToStartDetailActivity = Intent(context, destinationClass)
        intentToStartDetailActivity.putExtra(MovieDetailActivity.INTENT_EXTRA_MOVIE_DATA, movie)
        startActivity(intentToStartDetailActivity)
    }

    private fun fetchMovies() {
        val liveData = movieSource
        liveData.observe(this, Observer<List<Movie>> { movies ->
            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            if (movies == null || movies.isEmpty()) {
                layoutManager.spanCount = 1 // 1 column needed for empty state layout
                adapter.setMovies(emptyList())
            } else {
                context?.let {
                    layoutManager.spanCount = ViewUtils.calculateNumberOfColumns(it)
                }
                adapter.setMovies(movies)
            }
        })
    }

}