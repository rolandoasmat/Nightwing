package com.asmat.rolando.popularmovies.ui.moviedetails

import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import androidx.core.app.ShareCompat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import com.asmat.rolando.popularmovies.MovieNightApplication
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.extensions.gone
import com.asmat.rolando.popularmovies.extensions.visible
import com.asmat.rolando.popularmovies.model.mappers.DataModelMapper
import com.asmat.rolando.popularmovies.model.mappers.UiModelMapper
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.*
import com.asmat.rolando.popularmovies.repositories.PeopleRepository
import com.asmat.rolando.popularmovies.ui.castdetails.CastDetailsActivity
import com.squareup.picasso.Picasso
import com.asmat.rolando.popularmovies.utilities.URLUtils
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.movie_details_user_actions.*
import kotlinx.android.synthetic.main.primary_details.*
import javax.inject.Inject

class MovieDetailsActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_MOVIE_ID = "extra_movie_id"

        fun createIntent(context: Context, movieID: Int): Intent {
            val destinationClass = MovieDetailsActivity::class.java
            val intentToStartDetailActivity = Intent(context, destinationClass)
            intentToStartDetailActivity.putExtra(EXTRA_MOVIE_ID, movieID)
            return intentToStartDetailActivity
        }
    }

    @Inject
    lateinit var moviesRepository: MoviesRepository

    @Inject
    lateinit var peopleRepository: PeopleRepository

    @Inject
    lateinit var dataModelMapper: DataModelMapper

    @Inject
    lateinit var uiModelMapper: UiModelMapper

    private val movieID: Int
        get() {
            return intent.getIntExtra(EXTRA_MOVIE_ID, 0)
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
        val intent = Intent(this@MovieDetailsActivity, CastDetailsActivity::class.java)
        intent.putExtra(CastDetailsActivity.EXTRA_PERSON_ID, cast.id)
        intent.resolveActivity(packageManager)?.let {
            startActivity(intent)
        }
    }
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as MovieNightApplication).component().inject(this)
        setContentView(R.layout.activity_movie_detail)
        viewModel = ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository, dataModelMapper, uiModelMapper)).get(MovieDetailsViewModel::class.java)
        viewModel.init(movieID)
        setupObservers()
        setupUI()
        sendEvents()
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
    private fun sendEvents() {
        starContainer?.setOnClickListener {
            viewModel.onStarTapped()
        }
        shareContainer?.setOnClickListener {
            viewModel.onShareTapped()
        }
        toWatchContainer?.setOnClickListener {
            viewModel.onBookmarkTapped()
        }
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

        viewModel.runtime.observe(this, Observer { runtime ->
            updateRuntime(runtime)
        })

        viewModel
                .posterURL
                .observe(this, Observer { url ->
                    updatePoster(url)
                })

        viewModel.summary.observe(this, Observer { summary ->
            updateSummary(summary)
        })

        viewModel.tagline.observe(this, Observer { text ->
            taglineLabel.text = text
        })

        // Movie icons

        viewModel
                .isFavoriteMovie
                .observe(this, Observer { isFavoriteMovie ->
                    updateStar(isFavoriteMovie == true)
                })

        viewModel
                .isWatchLaterMovie
                .observe(this, Observer { isWatchLaterMovie ->
                    updateBookmark(isWatchLaterMovie == true)
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
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        trailersRecyclerView.layoutManager = layoutManager
        trailersLinearAdapter = TrailersLinearAdapter(trailerClickCallback)
        trailersRecyclerView.adapter = trailersLinearAdapter
        trailersRecyclerView.isNestedScrollingEnabled = false
    }

    private fun setupCastRecyclerView() {
        castRecyclerView.setHasFixedSize(true)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        castRecyclerView.layoutManager = layoutManager
        castLinearAdapter = CastLinearAdapter(castClickCallback)
        castRecyclerView.adapter = castLinearAdapter
        castRecyclerView.isNestedScrollingEnabled = false
    }

    private fun setupReviewsRecyclerView() {
        reviewsRecyclerView.setHasFixedSize(true)
        val reviewsLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
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
            Picasso
                    .get()
                    .load(it)
                    .placeholder(R.drawable.image_loading)
                    .into(backdrop)
        } ?: run {
            backdrop?.gone()
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
        movieRatingLabel.text = rating
    }

    private fun updateRuntime(runtime: String?) {
        runtimeLabel.text = runtime
    }

    private fun updatePoster(url: String?) {
        url?.let {
            Picasso.get().load(it).into(thumbnail)
        } ?: thumbnail?.gone()
    }

    private fun updateSummary(summaryStr: String?) {
        summaryStr?.let {
            summary.text = it
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

    private fun shareMovie(movieData: Pair<String, String>?) {
        if (movieData == null) {
            return
        }
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
    private fun updateStar(enable: Boolean) {
        starIcon?.isSelected = enable
    }

    private fun updateBookmark(enable: Boolean) {
        bookmarkIcon?.isSelected = enable
    }
    //endregion
}