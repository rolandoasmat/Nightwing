package com.asmat.rolando.popularmovies.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.extensions.gone
import com.asmat.rolando.popularmovies.extensions.visible
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.model.mappers.MovieMapper
import com.asmat.rolando.popularmovies.ui.moviedetails.MovieDetailActivity
import com.asmat.rolando.popularmovies.ui.common.MovieAdapterOnClickHandler
import com.asmat.rolando.popularmovies.networking.the.movie.db.TheMovieDBClient
import com.asmat.rolando.popularmovies.ui.common.MoviesGridAdapter
import com.asmat.rolando.popularmovies.utilities.NetworkUtils
import com.asmat.rolando.popularmovies.utilities.ViewUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList

// TODO reuse code that's in MovieGridFragment here
// TODO refactor this mess to mvvm
class SearchResultsFragment : androidx.fragment.app.Fragment(), MovieAdapterOnClickHandler {

    private var mMoviesGridAdapter: MoviesGridAdapter? = null
    private var mContext: Context? = null
    private var page: Int = 0
    private var mMoviesGrid: androidx.recyclerview.widget.RecyclerView? = null
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
        val mMoviesGridLayoutManager: androidx.recyclerview.widget.GridLayoutManager
        if (page == 1) {
            val numOfCol = ViewUtils.calculateNumberOfColumns(mContext!!)
            mMoviesGridLayoutManager = androidx.recyclerview.widget.GridLayoutManager(mContext, numOfCol)
            mMoviesGridAdapter = MoviesGridAdapter(this)
            mMoviesGrid?.setHasFixedSize(true)
            mMoviesGrid?.layoutManager = mMoviesGridLayoutManager
            mMoviesGrid?.adapter = mMoviesGridAdapter
        } else {
            mMoviesGridLayoutManager = androidx.recyclerview.widget.GridLayoutManager(mContext, ViewUtils.calculateNumberOfColumns(mContext!!))
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
            mMoviesGrid?.gone()
            mNoInternetView?.visible()
        } else {
            // Internet connection established
            mMoviesGrid?.visible()
            mNoInternetView?.gone()
        }
    }

    init {
        page = 1
    }

    fun setSearchQuery(searchQuery: String) {
        this.searchQuery = searchQuery
        mMoviesGridAdapter?.setMovies(ArrayList())
        this.page = 1
        fetchMovies(searchQuery)
    }

    private var searchQuery = ""

    private fun createScrollListener(layoutManager: androidx.recyclerview.widget.GridLayoutManager): androidx.recyclerview.widget.RecyclerView.OnScrollListener {
        return object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && !fetchingMovies) { // User is scrolling down
                    val positionOfLastItem = layoutManager.itemCount - 1
                    val currentPositionOfLastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    if (currentPositionOfLastVisibleItem >= positionOfLastItem - 5) {
                        fetchMovies(searchQuery)
                    }
                }
            }
        }
    }

    private val client = TheMovieDBClient()

    private fun fetchMovies(searchText: String) {
        fetchingMovies = true
        client.searchMovie(searchText, page)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ result ->
                    val movies = result.results.map { MovieMapper.from(it) }
                    mMoviesGridAdapter?.addMovies(movies)
                    page++
                    fetchingMovies = false
                },{ error ->
                    handleError(error)
                    fetchingMovies = false
                })
    }

    override fun onClick(movie: Movie) {
        val destinationClass = MovieDetailActivity::class.java
        val intentToStartDetailActivity = Intent(mContext, destinationClass)
        intentToStartDetailActivity.putExtra(MovieDetailActivity.INTENT_EXTRA_MOVIE_DATA, movie)
        startActivity(intentToStartDetailActivity)
    }

    private fun handleError(error: Throwable) {
        val message = error.message ?: "Whoops, error fetching movies."
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast.show()
        Log.e("MovieGridFragment", error.toString())
    }
}