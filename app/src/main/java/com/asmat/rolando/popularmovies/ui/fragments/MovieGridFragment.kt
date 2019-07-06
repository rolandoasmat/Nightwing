package com.asmat.rolando.popularmovies.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.extensions.gone
import com.asmat.rolando.popularmovies.extensions.visible
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.model.mappers.MovieMapper
import com.asmat.rolando.popularmovies.ui.moviedetails.MovieDetailActivity
import com.asmat.rolando.popularmovies.ui.common.MovieAdapterOnClickHandler
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.ui.discover.RequestType
import com.asmat.rolando.popularmovies.networking.the.movie.db.TheMovieDBClient
import com.asmat.rolando.popularmovies.ui.discover.MoviesGridAdapter
import com.asmat.rolando.popularmovies.utilities.NetworkUtils
import com.asmat.rolando.popularmovies.utilities.ViewUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MovieGridFragment : Fragment(), MovieAdapterOnClickHandler, View.OnClickListener {

    private var moviesGridAdapter: MoviesGridAdapter? = null
    private var mContext: Context? = null
    private var fetchMoviesCallback: Single<MoviesResponse>? = null
    private lateinit var typeOfMovies: RequestType
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
            moviesGridAdapter = MoviesGridAdapter(this)
            mMoviesGrid?.setHasFixedSize(true)
            mMoviesGrid?.layoutManager = mMoviesGridLayoutManager
            mMoviesGrid?.adapter = moviesGridAdapter
            fetchMovies(page)
        } else {
            mMoviesGridLayoutManager = GridLayoutManager(mContext, ViewUtils.calculateNumberOfColumns(mContext!!))
            mMoviesGrid?.layoutManager = mMoviesGridLayoutManager
            mMoviesGrid?.adapter = moviesGridAdapter
        }
        mMoviesGrid?.addOnScrollListener(createScrollListener(mMoviesGridLayoutManager))
        mMoviesGrid?.isNestedScrollingEnabled = false

        val retryButton = rootView.findViewById<Button>(R.id.retry_button)
        retryButton.setOnClickListener(this)

        return rootView
    }

    override fun onResume() {
        super.onResume()
        checkInternet()
    }

    private fun checkInternet() {
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

    fun setTypeOfMovies(typeOfMovies: RequestType) {
        this.typeOfMovies = typeOfMovies
    }

    private fun createScrollListener(layoutManager: GridLayoutManager): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0 && !fetchingMovies) { // User is scrolling down
                    val positionOfLastItem = layoutManager.itemCount - 1
                    val currentPositionOfLastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    if (currentPositionOfLastVisibleItem >= positionOfLastItem - 5) {
                        fetchMovies(page)
                    }
                }
            }
        }
    }

    private fun fetchMovies(page: Int) {
        fetchingMovies = true
        fetchMoviesRequest(page)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ result ->
                    val movies = result.results.map { MovieMapper.from(it) }
                    if (!movies.isEmpty()) {
                        moviesGridAdapter?.addMovies(movies)
                        this.page++
                    }
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

    private val client = TheMovieDBClient()

    private fun fetchMoviesRequest(page: Int): Single<MoviesResponse> {
        return when (typeOfMovies) {
            RequestType.MOST_POPULAR -> client.getPopularMovies(page)
            RequestType.TOP_RATED -> client.getTopRatedMovies(page)
            RequestType.NOW_PLAYING -> client.getNowPlayingMovies(page)
            RequestType.UPCOMING -> client.getUpcomingMovies(page)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.retry_button -> {
                checkInternet()
                fetchMovies(page)
            }
        }
    }

    private fun handleError(error: Throwable) {
        val message = error.message ?: "Whoops, error fetching movies."
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast.show()
        Log.e("MovieGridFragment", error.toString())
    }
}