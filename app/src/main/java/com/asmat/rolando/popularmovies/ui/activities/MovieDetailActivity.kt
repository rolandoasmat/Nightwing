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
import com.asmat.rolando.popularmovies.MovieNightApplication

import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.ui.adapters.linear.CastLinearAdapter
import com.asmat.rolando.popularmovies.ui.adapters.linear.ReviewsLinearAdapter
import com.asmat.rolando.popularmovies.ui.adapters.linear.TrailersLinearAdapter
import com.asmat.rolando.popularmovies.extensions.gone
import com.asmat.rolando.popularmovies.extensions.visible
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.*
import com.asmat.rolando.popularmovies.viewmodels.MovieDetailsViewModel
import com.squareup.picasso.Picasso

import com.asmat.rolando.popularmovies.utilities.URLUtils
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.movie_details_user_actions.*
import kotlinx.android.synthetic.main.primary_details.*
import javax.inject.Inject

class MovieDetailActivity : AppCompatActivity() {

    companion object {
        const val INTENT_EXTRA_MOVIE_DATA = "MOVIE_DATA"
    }

    @Inject
    lateinit var moviesRepository: MoviesRepository

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
//            startActivity(intent)
        }
    }
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as MovieNightApplication).component().inject(this)
        setContentView(R.layout.activity_movie_detail)

        if (intent != null && intent.hasExtra(INTENT_EXTRA_MOVIE_DATA)) {
            val movieData = intent.getParcelableExtra<Movie>(INTENT_EXTRA_MOVIE_DATA)
            viewModel = ViewModelProviders.of(this, ViewModelFactory(moviesRepository, movieData)).get(MovieDetailsViewModel::class.java)
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
        viewModel.onShareTapped()
    }

    //endregion

    //region setup

    private fun setupObservers() {
        // Movie info
        viewModel
                .backdropURL
                .observe(this, Observer { url ->
                    updateBackdrop(url)
                })

        viewModel
                .movieTitle
                .observe(this, Observer { title ->
                    updateTitle(title)
                })

        viewModel
                .releaseDate
                .observe(this, Observer { date ->
                    updateReleaseDate(date)
                })

        viewModel
                .rating
                .observe(this, Observer { rating ->
                    updateRating(rating)
                })

        viewModel
                .posterURL
                .observe(this, Observer { url ->
                    updatePoster(url)
                })

        viewModel
                .summary
                .observe(this, Observer { summary ->
                    updateSummary(summary)
                })

        // Movie icons

        viewModel
                .isFavoriteMovie
                .observe(this, Observer { isFavoriteMovie ->
                    updateStarIcon(isFavoriteMovie)
                })

        viewModel
                .isWatchLaterMovie
                .observe(this, Observer { isWatchLaterMovie ->
                    updateBookmarkIcon(isWatchLaterMovie)
                })

        viewModel
                .shareMovie
                .observe(this, Observer { movieData ->
                    shareMovie(movieData)
                })

        // Movie lists

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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

    //region Update UI

    private fun updateBackdrop(url: String?) {
        url?.let {
            Picasso.with(this).load(it).into(backdrop)
        }
    }

    private fun updateTitle(title: String?) {
        title?.let {
            collapsingToolbar.title = it
        }
    }

    private fun updateReleaseDate(date: String?) {
        date?.let {
            releaseDateLabel.text = it
        }
    }

    private fun updateRating(rating: String?) {
        rating?.let {
            val ratingFormatted = getString(R.string.out_of_ten, it)
            movieRatingLabel.text = ratingFormatted
        }
    }

    private fun updatePoster(url: String?) {
        url?.let {
            Picasso.with(this).load(it).into(thumbnail)
        }
    }

    private fun updateSummary(summaryStr: String?) {
        summaryStr?.let {
            summary.text = it
        }
    }

    private fun updateStarIcon(isFavoriteMovie: Boolean?) {
        isFavoriteMovie?.let {
            if (it) {
                fillStar()
            } else {
                unfillStar()
            }
        } ?: unfillStar()
    }

    private fun updateBookmarkIcon(isWatchLaterMovie: Boolean?) {
        isWatchLaterMovie?.let {
            if (it) {
                fillBookmark()
            } else {
                unfillBookmark()
            }
        } ?: unfillBookmark()
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

    private fun shareMovie(movieData: Pair<String, String>?) {
        if (movieData == null) { return }
        val movieTitle = movieData.first
        val movieURL = movieData.second
        val mimeType = "text/plain"
        val title = resources.getString(R.string.share_movie)
        val textToShare = resources.getString(R.string.check_out_movie, movieTitle, movieURL)
        val intent = ShareCompat.IntentBuilder.from(this)
                .setChooserTitle(title)
                .setType(mimeType)
                .setText(textToShare).intent
        startActivity(Intent.createChooser(intent, title))
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