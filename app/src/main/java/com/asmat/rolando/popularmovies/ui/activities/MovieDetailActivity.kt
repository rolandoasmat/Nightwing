package com.asmat.rolando.popularmovies.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.support.v4.app.ShareCompat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View

import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.database.DatabaseManager
import com.asmat.rolando.popularmovies.ui.adapters.linear.CastLinearAdapter
import com.asmat.rolando.popularmovies.ui.adapters.linear.ReviewsLinearAdapter
import com.asmat.rolando.popularmovies.ui.adapters.linear.TrailersLinearAdapter
import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.database.entities.WatchLaterMovie
import com.asmat.rolando.popularmovies.extensions.gone
import com.asmat.rolando.popularmovies.extensions.visible
import com.asmat.rolando.popularmovies.model.MoviesRepository
import com.asmat.rolando.popularmovies.networking.the.movie.db.TheMovieDBClient
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.CreditsResponse
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MovieDetailsResponse
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.ReviewsResponse
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.VideosResponse
import com.asmat.rolando.popularmovies.utilities.DateUtils
import com.asmat.rolando.popularmovies.viewmodels.MovieDetailsViewModel
import com.squareup.picasso.Picasso

import com.asmat.rolando.popularmovies.utilities.URLUtils
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.movie_details_user_actions.*
import kotlinx.android.synthetic.main.primary_details.*

class MovieDetailActivity : AppCompatActivity() {

    companion object {
        const val INTENT_EXTRA_MOVIE_ID = "MOVIE_ID"
    }

    // Recycler View Adapters
    private lateinit var trailersLinearAdapter: TrailersLinearAdapter
    private lateinit var castLinearAdapter: CastLinearAdapter
    private lateinit var reviewsLinearAdapter: ReviewsLinearAdapter

    // View Model
    private lateinit var viewModel: MovieDetailsViewModel

    //region Callbacks
    private val trailerClickCallback = { trailer: VideosResponse.Video ->
        val url = URLUtils.getYoutubeURL(trailer.key)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.resolveActivity(packageManager)?.let {
            startActivity(intent)
        }
    }

    private val castClickCallback = { cast: CreditsResponse.Cast ->
        val intent = Intent(this@MovieDetailActivity, CastDetailActivity::class.java)
        intent.putExtra(CastDetailActivity.EXTRA_CAST, cast)
        intent.resolveActivity(packageManager)?.let {
            startActivity(intent)
        }
    }
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        if (intent != null && intent.hasExtra(INTENT_EXTRA_MOVIE_ID)) {
            val databaseManager = DatabaseManager(this)
            val tmdbClient = TheMovieDBClient()
            val moviesRepository = MoviesRepository(databaseManager, tmdbClient)
            val movieID = intent.getIntExtra(INTENT_EXTRA_MOVIE_ID, 0)
            viewModel = ViewModelProviders.of(this, ViewModelFactory(moviesRepository, movieID)).get(MovieDetailsViewModel::class.java) // TODO use movieID to create view model
        }
        setupObservers()
        setupUI()
    }

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
        super.onBackPressed()
        return true
    }

    //region User Actions

    fun onStar(view: View) {
        viewModel.onStarTapped()
    }

    fun onBookmark(view: View) {
        viewModel.onBookmarkTapped()
    }

    fun onShare(view: View) {
        val mimeType = "text/plain"
        val title = resources.getString(R.string.share_movie)
        val movieDetails = viewModel.movieDetails.value
        var textToShare = resources.getString(R.string.check_out_movie) +
                " \"" + movieDetails?.title + "\"" // TODO replace with string resource with variables
        val videos = trailersLinearAdapter.data
        if (videos.isNotEmpty()) {
            textToShare += "\n" + URLUtils.getYoutubeURL(videos.first().key)
        }

        val intent = ShareCompat.IntentBuilder.from(this)
                .setChooserTitle(title)
                .setType(mimeType)
                .setText(textToShare).intent
        startActivity(Intent.createChooser(intent, title))
    }

    //endregion

    //region Update UI

    private fun updateMovieDetails(movie: MovieDetailsResponse?) {
        if (movie == null) {
            return
        }
        val backdropURL = URLUtils.getImageURL780(movie.backdrop_path!!) // TODO create UI model classes
        val posterURL = URLUtils.getImageURL342(movie.poster_path!!)
        Picasso.with(this).load(backdropURL).into(backdrop)
        Picasso.with(this).load(posterURL).into(thumbnail)
        collapsingToolbar.title = movie.title
        val formatted = DateUtils.formatDate(movie.release_date)
        releaseDateLabel.text = formatted
        val rating = movie.popularity.toString() + getString(R.string.out_of_ten) // TODO use string resource with variables
        movieRatingLabel.text = rating
        summary.text = movie.overview
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

    private fun updateTrailers(videos: List<VideosResponse.Video>?) { // TODO observe and update error state
        trailersLoadingBar.gone()
        when {
            videos == null -> trailersErrorStateLabel.visible()
            videos.isEmpty() -> trailersEmptyStateLabel.visible()
            else -> {
                trailersLinearAdapter.data = videos
                trailersRecyclerView.visible()
            }
        }
    }

    private fun updateCast(cast: List<CreditsResponse.Cast>?) {
        castLoadingBar.gone()
        when {
            cast == null -> castErrorStateLabel.visible()
            cast.isEmpty() -> castEmptyStateLabel.visible()
            else -> {
                castLinearAdapter.data = cast
                castRecyclerView.visible()
            }
        }
    }

    private fun updateReviews(reviews: List<ReviewsResponse.Review>?) {
        reviewsLoadingBar.gone()
        when {
            reviews == null -> reviewsErrorStateLabel.visible()
            reviews.isEmpty() -> reviewsEmptyStateLabel.visible()
            else -> {
                reviewsLinearAdapter.data = reviews
                reviewsRecyclerView.visible()
            }
        }
    }

    //endregion

    //region setup

    private fun setupObservers() {
        viewModel
                .movieDetails
                .observe(this, Observer { movieDetailsResponse ->
                    updateMovieDetails(movieDetailsResponse)
                })

        viewModel
                .favoriteMovie
                .observe(this, Observer { favoriteMovie ->
                    updateStarIcon(favoriteMovie)
                })

        viewModel
                .watchLaterMovie
                .observe(this, Observer { watchLaterMovie ->
                    updateBookmarkIcon(watchLaterMovie)
                })

        viewModel
                .videos
                .observe(this, Observer { videos ->
                    updateTrailers(videos)
                })

        viewModel
                .cast
                .observe(this, Observer { cast ->
                    updateCast(cast)
                })

        viewModel
                .reviews
                .observe(this, Observer { reviews ->
                    updateReviews(reviews)
                })
    }

    private fun setupUI() {
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
        trailersRecyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        trailersRecyclerView.layoutManager = layoutManager
        trailersLinearAdapter = TrailersLinearAdapter(trailerClickCallback)
        trailersRecyclerView.adapter = trailersLinearAdapter
        trailersRecyclerView.isNestedScrollingEnabled = false
    }

    private fun setupCastRecyclerView() {
        castRecyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        castRecyclerView.layoutManager = layoutManager
        castLinearAdapter = CastLinearAdapter(castClickCallback)
        castRecyclerView.adapter = castLinearAdapter
        castRecyclerView.isNestedScrollingEnabled = false
    }

    private fun setupReviewsRecyclerView() {
        reviewsRecyclerView.setHasFixedSize(true)
        val reviewsLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        reviewsRecyclerView.layoutManager = reviewsLayoutManager
        reviewsLayoutManager.isSmoothScrollbarEnabled = true
        reviewsLinearAdapter = ReviewsLinearAdapter()
        reviewsRecyclerView.adapter = reviewsLinearAdapter
        reviewsRecyclerView.isNestedScrollingEnabled = false
    }

    //endregion

    //region Icons

    private fun fillStar() {
        star.setImageResource(R.drawable.ic_star_filled)
    }

    private fun unfillStar() {
        star.setImageResource(R.drawable.ic_star)
    }

    private fun fillBookmark() {
        bookmark.setImageResource(R.drawable.ic_bookmark_filled)
    }

    private fun unfillBookmark() {
        bookmark.setImageResource(R.drawable.ic_bookmark)
    }

    //endregion
}