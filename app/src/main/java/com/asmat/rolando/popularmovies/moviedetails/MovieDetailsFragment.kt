package com.asmat.rolando.popularmovies.moviedetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.asmat.rolando.popularmovies.MovieNightApplication
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.extensions.gone
import com.asmat.rolando.popularmovies.extensions.visible
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.CreditsResponse
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.ReviewsResponse
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.VideosResponse
import com.asmat.rolando.popularmovies.ui.castdetails.CastDetailsActivity
import com.asmat.rolando.popularmovies.utilities.URLUtils
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.movie_details_user_actions.*
import kotlinx.android.synthetic.main.primary_details.*
import javax.inject.Inject

class MovieDetailsFragment: Fragment() {

    companion object {
        fun newInstance(extras: Bundle?): MovieDetailsFragment {
            val fragment = MovieDetailsFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    val viewModel: MovieDetailsViewModel by viewModels{ viewModelFactory }


    // Recycler View Adapters
    private lateinit var trailersLinearAdapter: TrailersLinearAdapter
    private lateinit var castLinearAdapter: CastLinearAdapter
    private lateinit var reviewsLinearAdapter: ReviewsLinearAdapter

    private val movieID: Int
        get() {
            return requireArguments().getInt(MovieDetailsActivity.EXTRA_MOVIE_ID)
        }


    //region Callbacks
    private val trailerClickCallback = { trailer: VideosResponse.Video ->
        val url = URLUtils.getYoutubeURL(trailer.key)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.resolveActivity(requireActivity().packageManager)?.let {
            startActivity(intent)
        }
    }

    private val castClickCallback = { cast: CreditsResponse.Cast ->
        val intent = Intent(requireContext(), CastDetailsActivity::class.java)
        intent.putExtra(CastDetailsActivity.EXTRA_PERSON_ID, cast.id)
        intent.resolveActivity(requireActivity().packageManager)?.let {
            startActivity(intent)
        }
    }
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as? MovieNightApplication)?.component()?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init(movieID)
        setupObservers()
        setupUI()
        sendEvents()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            android.R.id.home -> {
//                super.onBackPressed()
//                return true
//            }
//        }
        return super.onOptionsItemSelected(item)
    }

//    override fun onSupportNavigateUp(): Boolean {
//        super.onBackPressed()
//        return true
//    }

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
        viewModel.backdropURL.observe(viewLifecycleOwner, Observer { url ->
            updateBackdrop(url)
        })

        viewModel.movieTitle.observe(viewLifecycleOwner, Observer { title ->
            updateTitle(title)
        })

        viewModel.releaseDate.observe(viewLifecycleOwner, Observer { date ->
            updateReleaseDate(date)
        })

        viewModel.rating.observe(viewLifecycleOwner, Observer { rating ->
            updateRating(rating)
        })

        viewModel.runtime.observe(viewLifecycleOwner, Observer { runtime ->
            updateRuntime(runtime)
        })

        viewModel.posterURL.observe(viewLifecycleOwner, Observer { url ->
            updatePoster(url)
        })

        viewModel.summary.observe(viewLifecycleOwner, Observer { summary ->
            updateSummary(summary)
        })

        viewModel.tagline.observe(viewLifecycleOwner, Observer { text ->
            taglineLabel.text = text
        })

        // Movie icons

        viewModel.isFavoriteMovie.observe(viewLifecycleOwner, Observer { isFavoriteMovie ->
            updateStar(isFavoriteMovie == true)
        })

        viewModel.isWatchLaterMovie.observe(viewLifecycleOwner, Observer { isWatchLaterMovie ->
            updateBookmark(isWatchLaterMovie == true)
        })

        viewModel.shareMovie.observe(viewLifecycleOwner, Observer { movieData ->
            shareMovie(movieData)
        })

        // Movie lists

        viewModel.videos.observe(viewLifecycleOwner, Observer { videos ->
            updateTrailers(videos)
        })

        viewModel.cast.observe(viewLifecycleOwner, Observer { cast ->
            updateCast(cast)
        })

        viewModel.reviews.observe(viewLifecycleOwner, Observer { reviews ->
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
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupTrailersRecyclerView() {
        trailersRecyclerView.setHasFixedSize(true)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext(), androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        trailersRecyclerView.layoutManager = layoutManager
        trailersLinearAdapter = TrailersLinearAdapter(trailerClickCallback)
        trailersRecyclerView.adapter = trailersLinearAdapter
        trailersRecyclerView.isNestedScrollingEnabled = false
    }

    private fun setupCastRecyclerView() {
        castRecyclerView.setHasFixedSize(true)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext(), androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        castRecyclerView.layoutManager = layoutManager
        castLinearAdapter = CastLinearAdapter(castClickCallback)
        castRecyclerView.adapter = castLinearAdapter
        castRecyclerView.isNestedScrollingEnabled = false
    }

    private fun setupReviewsRecyclerView() {
        reviewsRecyclerView.setHasFixedSize(true)
        val reviewsLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext(), androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
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
        val intent = ShareCompat.IntentBuilder.from(requireActivity())
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