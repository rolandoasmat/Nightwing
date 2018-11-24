package com.asmat.rolando.popularmovies.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.app.ShareCompat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.ui.adapters.CastLinearAdapter
import com.asmat.rolando.popularmovies.ui.adapters.ReviewsLinearAdapter
import com.asmat.rolando.popularmovies.ui.adapters.TrailersLinearAdapter
import com.asmat.rolando.popularmovies.database.DatabaseManager
import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.database.entities.WatchLaterMovie
import com.asmat.rolando.popularmovies.viewmodels.MovieDetailsViewModel
import com.squareup.picasso.Picasso

import java.util.ArrayList

import butterknife.BindView
import com.asmat.rolando.popularmovies.ui.adapters.AdapterOnClickHandler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MovieDetailActivity : AppCompatActivity() {

    @BindView(R.id.toolbarImage)
    internal var backdrop: ImageView? = null
    @BindView(R.id.iv_poster_thumbnail)
    internal var thumbnail: ImageView? = null
    @BindView(R.id.tv_release_date)
    internal var releaseDateLabel: TextView? = null
    @BindView(R.id.tv_movie_rating)
    internal var ratingLabel: TextView? = null
    @BindView(R.id.tv_synopsis_content)
    internal var synopsisLabel: TextView? = null

    // Trailers
    @BindView(R.id.rv_trailers)
    internal var trailers: RecyclerView? = null
    @BindView(R.id.pb_trailers_loading_bar)
    internal var trailersLoading: ProgressBar? = null
    @BindView(R.id.tv_no_trailers)
    internal var noTrailersLabel: TextView? = null
    @BindView(R.id.tv_error_trailers)
    internal var trailersErrorLabel: TextView? = null
    private var trailersLinearAdapter: TrailersLinearAdapter? = null

    // Cast
    @BindView(R.id.cast_recycler_view)
    internal var cast: RecyclerView? = null
    @BindView(R.id.cast_loading)
    internal var castLoading: ProgressBar? = null
    @BindView(R.id.cast_empty)
    internal var noCastLabel: TextView? = null
    @BindView(R.id.cast_error)
    internal var castErrorLabel: TextView? = null
    private var castLinearAdapter: CastLinearAdapter? = null

    // Reviews
    @BindView(R.id.rv_reviews)
    internal var reviews: RecyclerView? = null
    @BindView(R.id.pb_reviews_loading_bar)
    internal var reviewsLoading: ProgressBar? = null
    @BindView(R.id.tv_no_reviews)
    internal var noReviewsLabel: TextView? = null
    @BindView(R.id.tv_error_reviews)
    internal var reviewsErrorLabel: TextView? = null
    private var mReviewsLinearAdapter: ReviewsLinearAdapter? = null

    // Star
    @BindView(R.id.star)
    internal var star: ImageView? = null
    // Bookmark
    @BindView(R.id.bookmark)
    internal var bookmark: ImageView? = null

    // ViewModel
    private var viewModel: MovieDetailsViewModel? = null

    //region Adapter Callbacks
    private val trailerClickCallback = AdapterOnClickHandler<Video> { item ->
        val url = item.youTubeURL
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private val castClickCallback = AdapterOnClickHandler<Cast> { item ->
        val intent = Intent(this@MovieDetailActivity, CastDetailActivity::class.java)
        intent.putExtra(CastDetailActivity.EXTRA_CAST, item)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val intent = intent
        if (intent != null && intent.hasExtra(INTENT_EXTRA_MOVIE_ID)) {
            val movieID = intent.getIntExtra(INTENT_EXTRA_MOVIE_ID, 0)
            viewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel::class.java)
            viewModel?.init(movieID)
        }
        viewModel?.movie
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { movie ->
            updateMovieDetails(movie)
        }
        viewModel?.favoriteMovie?.observe(this, Observer { favoriteMovie -> this@MovieDetailActivity.updateStarIcon(favoriteMovie) })
        viewModel?.watchLaterMovie?.observe(this, Observer { watchLaterMovie -> this@MovieDetailActivity.updateBookmarkIcon(watchLaterMovie) })
        viewModel?.videos
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { videosResponse ->
            val videos = videosResponse.results.map { Video(it) }
            updateTrailers(videos)
        }
        viewModel?.credit
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { credit ->
            updateCredit(credit)
        }

        viewModel?.reviews
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { reviews ->
            updateReviews(reviews)
        }

        setup()
    }

    //endregion

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    //region User Actions

    fun onStar(view: View) {
        val movie = this@MovieDetailActivity.viewModel!!.movieSubject!!.getValue() ?: return
        val favoriteMovie = FavoriteMovie(movie!!.getId())
        if (this.viewModel!!.favoriteMovie!!.value == null) {
            // Add the Favorite movie
            DatabaseManager.INSTANCE.addMovie(movie)
            DatabaseManager.INSTANCE.addFavoriteMovie(favoriteMovie)
        } else {
            DatabaseManager.INSTANCE.deleteFavoriteMovie(favoriteMovie)
            // Check if it's a watch later movie, delete movie ref if it's not
            if (this.viewModel!!.watchLaterMovie!!.value == null) {
                DatabaseManager.INSTANCE.deleteMovie(movie)
            }
        }
    }

    fun onBookmark(view: View) {
        val movie = this@MovieDetailActivity.viewModel!!.movieSubject!!.getValue() ?: return
        val watchLaterMovie = WatchLaterMovie(movie!!.getId())
        if (this.viewModel!!.watchLaterMovie!!.value == null) {
            DatabaseManager.INSTANCE.addMovie(movie)
            DatabaseManager.INSTANCE.addWatchLaterMovie(watchLaterMovie)
        } else {
            DatabaseManager.INSTANCE.deleteWatchLaterMovie(watchLaterMovie)
            if (this.viewModel!!.favoriteMovie!!.value == null) {
                DatabaseManager.INSTANCE.deleteMovie(movie)
            }
        }
    }

    fun onShare(view: View) {
        val mimeType = "text/plain"
        val title = resources.getString(R.string.share_movie)
        val movie = viewModel!!.movieSubject!!.getValue()
        val movieTitle = movie!!.getTitle()
        var textToShare = resources.getString(R.string.check_out_movie) +
                " \"" + movieTitle + "\""
        val videos = trailersLinearAdapter!!.data
        if (videos != null && videos.size > 0) {
            textToShare += "\n" + videos[0].youTubeURL
        }

        val intent = ShareCompat.IntentBuilder.from(this)
                .setChooserTitle(title)
                .setType(mimeType)
                .setText(textToShare).intent
        startActivity(Intent.createChooser(intent, title))
    }

    //endregion

    /**
     * Private methods
     */

    //region Update UI

    private fun updateMovieDetails(movie: Movie?) {
        if (movie != null) {
            Picasso.with(this).load(movie.backdropURL).into(backdrop)
            Picasso.with(this).load(movie.posterURL).into(thumbnail)
            val collapsingToolbarLayout = findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbar)
            collapsingToolbarLayout.title = movie.title
            val formatted = movie.releaseDateFormatted
            releaseDateLabel!!.text = formatted
            val rating = movie.voteAverage.toString() + getString(R.string.out_of_ten)
            ratingLabel!!.text = rating
            synopsisLabel!!.text = movie.overview
        }
    }

    private fun updateStarIcon(favoriteMovie: FavoriteMovie?) {
        if (favoriteMovie == null) {
            unfillStar()
        } else {
            fillStar()
        }
    }

    private fun updateBookmarkIcon(watchLaterMovie: WatchLaterMovie?) {
        if (watchLaterMovie == null) {
            unfillBookmark()
        } else {
            fillBookmark()
        }
    }

    private fun updateTrailers(videos: List<Video>?) {
        trailersLoading!!.visibility = View.GONE
        if (videos == null) {
            trailersErrorLabel!!.visibility = View.VISIBLE
        } else {
            if (videos.size == 0) {
                noTrailersLabel!!.visibility = View.VISIBLE
            } else {
                val trailers = ArrayList<Video>()
                if (trailers.size == 0) {
                    noTrailersLabel!!.visibility = View.VISIBLE
                } else {
                    trailersLinearAdapter!!.data = trailers
                    this.trailers!!.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateCredit(credit: Credit?) {
        castLoading!!.visibility = View.GONE
        if (credit == null) {
            castErrorLabel!!.visibility = View.VISIBLE
        } else {
            if (credit.cast.size == 0) {
                noCastLabel!!.visibility = View.VISIBLE
            } else {
                castLinearAdapter!!.data = credit.cast
                this.cast!!.visibility = View.VISIBLE
            }
        }
    }

    private fun updateReviews(reviews: List<Review>?) {
        reviewsLoading!!.visibility = View.GONE
        if (reviews == null) {
            reviewsErrorLabel!!.visibility = View.VISIBLE
        } else {
            if (reviews.size == 0) {
                noReviewsLabel!!.visibility = View.VISIBLE
            } else {
                this.reviews!!.visibility = View.VISIBLE
                mReviewsLinearAdapter!!.data = reviews
            }
        }
    }

    //endregion

    //region setup

    private fun setup() {
        setupToolbar()
        setupTrailersRecyclerView()
        setupReviewsRecyclerView()
        setupCastRecyclerView()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupTrailersRecyclerView() {
        trailers?.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        trailers?.layoutManager = layoutManager
        trailersLinearAdapter = TrailersLinearAdapter(trailerClickCallback)
        trailers?.adapter = trailersLinearAdapter
        trailers?.isNestedScrollingEnabled = false
    }

    private fun setupCastRecyclerView() {
        cast?.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        cast?.layoutManager = layoutManager
        castLinearAdapter = CastLinearAdapter(castClickCallback)
        cast?.adapter = castLinearAdapter
        cast?.isNestedScrollingEnabled = false
    }

    private fun setupReviewsRecyclerView() {
        reviews?.setHasFixedSize(true)
        val reviewsLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        reviews?.layoutManager = reviewsLayoutManager
        reviewsLayoutManager.isSmoothScrollbarEnabled = true
        mReviewsLinearAdapter = ReviewsLinearAdapter()
        reviews?.adapter = mReviewsLinearAdapter
        reviews?.isNestedScrollingEnabled = false
    }

    //endregion

    //region Icons

    private fun fillStar() {
        star!!.setImageResource(R.drawable.ic_star_filled)
    }

    private fun unfillStar() {
        star!!.setImageResource(R.drawable.ic_star)
    }

    private fun fillBookmark() {
        bookmark!!.setImageResource(R.drawable.ic_bookmark_filled)
    }

    private fun unfillBookmark() {
        bookmark!!.setImageResource(R.drawable.ic_bookmark)
    }

    companion object {

        val INTENT_EXTRA_MOVIE_ID = "MOVIE_ID"
    }

    //endregion
}