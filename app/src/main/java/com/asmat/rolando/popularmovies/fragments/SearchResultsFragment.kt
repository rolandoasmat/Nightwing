package com.asmat.rolando.popularmovies.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.activities.MovieDetailActivity
import com.asmat.rolando.popularmovies.adapters.MoviesGridBaseAdapter
import com.asmat.rolando.popularmovies.database.Movie
import com.asmat.rolando.popularmovies.models.MovieAdapterOnClickHandler
import com.asmat.rolando.popularmovies.networking.models.MoviesResponse
import com.asmat.rolando.popularmovies.networking.TheMovieDBClient
import com.asmat.rolando.popularmovies.utilities.NetworkUtils
import com.asmat.rolando.popularmovies.utilities.ViewUtils
import io.reactivex.Single
import java.util.ArrayList

class SearchResultsFragment : Fragment(), MovieAdapterOnClickHandler {

    private var mMoviesGridAdapter: MoviesGridBaseAdapter? = null
    private var mContext: Context? = null
    private var fetchMoviesCallback: Single<MoviesResponse>? = null
    private var searchQuery: String = ""
    private var page: Int = 0
    private var mMoviesGrid: RecyclerView? = null
    private var mNoInternetView: LinearLayout? = null
    private var fetchingMovies = false

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false)
        mMoviesGrid = rootView.findViewById(R.id.rv_movie_grid)
        mNoInternetView = rootView.findViewById(R.id.no_internet_layout)
        val mMoviesGridLayoutManager: GridLayoutManager
        if (page == 1) {
            val numOfCol = ViewUtils.calculateNumberOfColumns(mContext!!)
            mMoviesGridLayoutManager = GridLayoutManager(mContext, numOfCol)
            mMoviesGridAdapter = MoviesGridBaseAdapter(this)
            mMoviesGrid?.setHasFixedSize(true)
            mMoviesGrid?.layoutManager = mMoviesGridLayoutManager
            mMoviesGrid?.adapter = mMoviesGridAdapter
            setFetchMoviesLoaderCallback()
            fetchMovies()
        } else {
            mMoviesGridLayoutManager = GridLayoutManager(mContext, ViewUtils.calculateNumberOfColumns(mContext!!))
            mMoviesGrid?.layoutManager = mMoviesGridLayoutManager
            mMoviesGrid?.adapter = mMoviesGridAdapter
        }
        mMoviesGrid?.addOnScrollListener(createScrollListener(mMoviesGridLayoutManager))
        mMoviesGrid?.isNestedScrollingEnabled = false
        return rootView
    }

    override fun onResume() {
        super.onResume()
        if (!NetworkUtils.isOnline(context!!)) {
            // User has no internet
            mMoviesGrid!!.visibility = View.GONE
            mNoInternetView!!.visibility = View.VISIBLE
        } else {
            // Internet connection established
            mMoviesGrid!!.visibility = View.VISIBLE
            mNoInternetView!!.visibility = View.GONE
        }
    }

    init {
        page = 1
    }

    fun setSearchQuery(searchQuery: String) {
        this.searchQuery = searchQuery
        mMoviesGridAdapter!!.setMovies(ArrayList())
        this.page = 1
        fetchMovies()
    }

    private fun createScrollListener(layoutManager: GridLayoutManager): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0 && !fetchingMovies) { // User is scrolling down
                    val positionOfLastItem = layoutManager.itemCount - 1
                    val currentPositionOfLastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    if (currentPositionOfLastVisibleItem >= positionOfLastItem - 5) {
                        fetchMovies()
                    }
                }
            }
        }
    }

    private fun fetchMovies() {
        fetchingMovies = true
        fetchMoviesCallback?.subscribe({ result ->
            val mapped = result.results.map { Movie(it)}
            mMoviesGridAdapter?.addMovies(mapped.toTypedArray())
            page++
            fetchingMovies = false
        }, { error ->
            // TODO handle error
            fetchingMovies = false
        })
    }

    override fun onClick(movie: Movie) {
        val destinationClass = MovieDetailActivity::class.java
        val intentToStartDetailActivity = Intent(mContext, destinationClass)
        intentToStartDetailActivity.putExtra(MovieDetailActivity.INTENT_EXTRA_MOVIE_ID, movie.id)
        startActivity(intentToStartDetailActivity)
    }

    private fun setFetchMoviesLoaderCallback() {
        fetchMoviesCallback = TheMovieDBClient.searchMovie(searchQuery, page)
    }
}