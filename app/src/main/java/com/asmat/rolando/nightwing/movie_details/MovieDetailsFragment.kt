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
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.networking.models.CreditsResponse
import com.asmat.rolando.nightwing.networking.models.ReviewsResponse
import com.asmat.rolando.nightwing.networking.models.VideosResponse
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import com.asmat.rolando.nightwing.ui.common.BaseLinearAdapter
import com.asmat.rolando.nightwing.ui.row_view.RowViewItemUiModel
import com.asmat.rolando.nightwing.ui.row_view.RowView
import com.asmat.rolando.nightwing.utilities.URLUtils
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.details_screen_user_actions.*
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.primary_details.*
import javax.inject.Inject

class MovieDetailsFragment: Fragment(), BaseLinearAdapter.Callback<RowViewItemUiModel> {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    val viewModel: MovieDetailsViewModel by viewModels{ viewModelFactory }

    // Recycler View Adapters
    private lateinit var trailersLinearAdapter: TrailersLinearAdapter
    private lateinit var castLinearAdapter: CastLinearAdapter
    private lateinit var reviewsLinearAdapter: ReviewsLinearAdapter

    val similarMoviesRowViewModel: SimilarMoviesRowViewModel by lazy {
        viewModelFactory.getSimilarMoviesRowViewModel(movieID)
    }

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
        heartContainer?.setOnClickListener {
            viewModel.onSaveTapped()
        }
        shareContainer?.setOnClickListener {
            viewModel.onShareTapped()
        }
    }
    //endregion

    //region setup

    private fun setupObservers() {
        // Movie info
        viewModel.movieDetailsUIModel.observe(viewLifecycleOwner) {
            updateBackdrop(it.backdropURL)
            updateTitle(it.title)
            updateReleaseDate(it.releaseDateText)
            updateRating(it.voteAverage)
            updateRuntime(it.runtime)
            updatePoster(it.posterURL)
            updateSummary(it.overview)
            taglineLabel.text = it.tagline
        }

        viewModel.director.observe(viewLifecycleOwner) {
            directorLabel?.text = it
            moreFromDirectorMoviesRow?.configure(title = resources.getString(R.string.more_from_director, it))
        }

        // Movie icons

        viewModel.isSavedMovie.observe(viewLifecycleOwner, Observer { isSavedMovie ->
            updateHeart(isSavedMovie == true)
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

        similarMoviesRow.observe(similarMoviesRowViewModel, viewLifecycleOwner)

//        viewModel.recommendedMovies.observe(viewLifecycleOwner) {
//            recommendedMoviesRow.setData(it)
//        }

//        viewModel.directorMovies.observe(viewLifecycleOwner) {
//            moreFromDirectorMoviesRow.setData(it)
//        }

        viewModel.reviews.observe(viewLifecycleOwner, Observer { reviews ->
            updateReviews(reviews)
        })
    }

    private fun setupUI() {
        setupToolbar()
        setupTrailersRecyclerView()
        setupReviewsRecyclerView()
        setupCastRecyclerView()
        setupSimilarMoviesRow()
        setupRecommendedMoviesRow()
        setupDirectorMoviesRow()
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

    private fun setupSimilarMoviesRow() {
        similarMoviesRow.configure("Similar Movies", false, object : RowView.Callback {
            override fun onCardClicked(id: Int) = navigateToMovieDetails(id)
            override fun onRetry() = similarMoviesRowViewModel.load()
        })
        similarMoviesRowViewModel.load()
    }

    private fun setupRecommendedMoviesRow() {
        recommendedMoviesRow.configure("Recommended movies", true, object: RowView.Callback {
            override fun onSeeAllClicked() {
                val action = MovieDetailsFragmentDirections.actionMovieDetailsScreenToRecommendedMoviesGrid(movieID)
                findNavController().navigate(action)
            }
            override fun onCardClicked(id: Int) = navigateToMovieDetails(id)
        })
    }

    private fun setupDirectorMoviesRow() {
        moreFromDirectorMoviesRow.configure(null, false, object: RowView.Callback {
            override fun onCardClicked(id: Int) = navigateToMovieDetails(id)
        })
    }

    private fun navigateToMovieDetails(id: Int) {
        val action = MovieDetailsFragmentDirections.actionMovieDetailsScreenToMovieDetailsScreen(id)
        findNavController().navigate(action)
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
    private fun updateHeart(enable: Boolean) {
        heartIcon?.isSelected = enable
    }

    override fun cardClicked(item: RowViewItemUiModel) {
        item.id?.let { movieID ->
            val action = MovieDetailsFragmentDirections.actionMovieDetailsScreenToMovieDetailsScreen(movieID)
            findNavController().navigate(action)
        }
    }
    //endregion

    companion object {
        const val MOVIE_ID_ARG = "movieIdArg"
        fun newInstance(extras: Bundle?): MovieDetailsFragment {
            val fragment = MovieDetailsFragment()
            fragment.arguments = extras
            return fragment
        }
    }
}