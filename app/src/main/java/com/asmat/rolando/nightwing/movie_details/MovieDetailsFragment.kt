package com.asmat.rolando.nightwing.movie_details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.asmat.rolando.nightwing.NightwingApplication
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.extensions.gone
import com.asmat.rolando.nightwing.extensions.visible
import com.asmat.rolando.nightwing.networking.models.CreditsResponse
import com.asmat.rolando.nightwing.networking.models.ReviewsResponse
import com.asmat.rolando.nightwing.networking.models.VideosResponse
import com.asmat.rolando.nightwing.ui.common.BaseLinearAdapter
import com.asmat.rolando.nightwing.ui.movie_row.MovieRowView
import com.asmat.rolando.nightwing.utilities.URLUtils
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.movie_details_user_actions.*
import kotlinx.android.synthetic.main.primary_details.*
import javax.inject.Inject

class MovieDetailsFragment: Fragment(), BaseLinearAdapter.Callback<MovieCardUIModel> {

    companion object {
        const val MOVIE_ID_ARG = "movieIdArg"
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
    private lateinit var similarMoviesLinearAdapter: MoviesLinearAdapter
    private lateinit var recommendedMoviesLinearAdapter: MoviesLinearAdapter
    private lateinit var directorMoviesLinearAdapter: MoviesLinearAdapter
    private lateinit var reviewsLinearAdapter: ReviewsLinearAdapter

    private val movieID: Int
        get() {
            return requireArguments().getInt(MOVIE_ID_ARG)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as? NightwingApplication)?.component()?.inject(this)
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
        viewModel.movieDetailsUIModel.observe(viewLifecycleOwner) {
            updateBackdrop(it.backdropURL)
            updateTitle(it.title)
            updateReleaseDate(it.releaseDate)
            updateRating(it.voteAverage)
            updateRuntime(it.runtime)
            updatePoster(it.posterURL)
            updateSummary(it.overview)
            taglineLabel.text = it.tagline
        }

        viewModel.director.observe(viewLifecycleOwner) {
            directorLabel?.text = it
            moreFromDirectorLabel?.text = resources.getString(R.string.more_from_director, it)
        }

        // Movie icons

        viewModel.isFavoriteMovie.observe(viewLifecycleOwner, Observer { isFavoriteMovie ->
            updateStar(isFavoriteMovie == true)
        })

        viewModel.isWatchLaterMovie.observe(viewLifecycleOwner, Observer { isWatchLaterMovie ->
            updateBookmark(isWatchLaterMovie == true)
        })

        viewModel.shareMovie.observe(viewLifecycleOwner, Observer { textToShare ->
            textToShare?.let { text ->
                shareMovie(text)
            }
        })

        // Movie lists

        viewModel.videos.observe(viewLifecycleOwner, Observer { videos ->
            updateTrailers(videos)
        })

        viewModel.cast.observe(viewLifecycleOwner, Observer { cast ->
            updateCast(cast)
        })

        viewModel.similarMovies.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                similarMoviesHeaderLabel?.gone()
            }
            similarMoviesLinearAdapter.data = it
        }

        viewModel.recommendedMovies.observe(viewLifecycleOwner) {
            recommendedMoviesLinearAdapter.data = it
        }

        viewModel.directorMovies.observe(viewLifecycleOwner) {
            directorMoviesLinearAdapter.data = it
        }

        viewModel.reviews.observe(viewLifecycleOwner, Observer { reviews ->
            updateReviews(reviews)
        })
    }

    private fun setupUI() {
        setupToolbar()
        setupTrailersRecyclerView()
        setupReviewsRecyclerView()
        setupCastRecyclerView()
        setupSimilarMoviesRecyclerView()
        setupRecommendedMoviesRow()
        setupDirectorMoviesRecyclerView()
    }

    private fun setupToolbar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        collapsingToolbar?.setupWithNavController(toolbar, findNavController(), appBarConfiguration)
    }

    private fun setupTrailersRecyclerView() {
        trailersRecyclerView.setHasFixedSize(true)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext(), androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        trailersRecyclerView.layoutManager = layoutManager
        trailersLinearAdapter = TrailersLinearAdapter(object: BaseLinearAdapter.Callback<VideosResponse.Video> {
            override fun cardClicked(item: VideosResponse.Video) {
                val url = URLUtils.getYoutubeURL(item.key)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                intent.resolveActivity(requireActivity().packageManager)?.let {
                    startActivity(intent)
                }
            }
        })
        trailersRecyclerView.adapter = trailersLinearAdapter
        trailersRecyclerView.isNestedScrollingEnabled = false
    }

    private fun setupCastRecyclerView() {
        castRecyclerView.setHasFixedSize(true)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext(), androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        castRecyclerView.layoutManager = layoutManager
        castLinearAdapter = CastLinearAdapter(object: BaseLinearAdapter.Callback<Cast>{
            override fun cardClicked(item: Cast) {
                val action = MovieDetailsFragmentDirections.actionMovieDetailsScreenToCastDetailsScreen(item.id)
                findNavController().navigate(action)
            }
        })
        castRecyclerView.adapter = castLinearAdapter
        castRecyclerView.isNestedScrollingEnabled = false
    }

    private fun setupSimilarMoviesRecyclerView() {
        similarMoviesRecyclerView.setHasFixedSize(true)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext(), androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        similarMoviesRecyclerView.layoutManager = layoutManager
        similarMoviesLinearAdapter = MoviesLinearAdapter(this)
        similarMoviesRecyclerView.adapter = similarMoviesLinearAdapter
        similarMoviesRecyclerView.isNestedScrollingEnabled = false
    }

    private fun setupRecommendedMoviesRow() {
        recommendedMoviesLinearAdapter = MoviesLinearAdapter(this)
        recommendedMoviesRow.configure("Recommended movies", recommendedMoviesLinearAdapter, object: MovieRowView.Callback {
            override fun onSeeAllClicked() {
                val action = MovieDetailsFragmentDirections.actionMovieDetailsScreenToRecommendedMoviesGrid(movieID)
                findNavController().navigate(action)
            }
        })
    }

    private fun setupDirectorMoviesRecyclerView() {
        directorMoviesRecyclerView.setHasFixedSize(true)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext(), androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        directorMoviesRecyclerView.layoutManager = layoutManager
        directorMoviesLinearAdapter = MoviesLinearAdapter(this)
        directorMoviesRecyclerView.adapter = directorMoviesLinearAdapter
        directorMoviesRecyclerView.isNestedScrollingEnabled = false
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

    private fun shareMovie(textToShare: String) {
        val mimeType = "text/plain"
        val title = resources.getString(R.string.share_movie)
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

    override fun cardClicked(item: MovieCardUIModel) {
        val movieID = item.id
        val action = MovieDetailsFragmentDirections.actionMovieDetailsScreenToMovieDetailsScreen(movieID)
        findNavController().navigate(action)
    }
    //endregion

}