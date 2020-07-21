package com.asmat.rolando.popularmovies.ui.moviegrid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.asmat.rolando.popularmovies.MovieNightApplication
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.deep_links.DeepLinksUtils
import com.asmat.rolando.popularmovies.extensions.gone
import com.asmat.rolando.popularmovies.extensions.visible
import com.asmat.rolando.popularmovies.model.mappers.DataModelMapper
import com.asmat.rolando.popularmovies.model.mappers.UiModelMapper
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.repositories.PeopleRepository
import com.asmat.rolando.popularmovies.utilities.ViewUtils
import kotlinx.android.synthetic.main.fragment_movie_grid.*
import kotlinx.android.synthetic.main.retry_layout.*
import javax.inject.Inject

abstract class BaseMovieGridFragment: androidx.fragment.app.Fragment() {

    @Inject
    lateinit var moviesRepository: MoviesRepository

    @Inject
    lateinit var peopleRepository: PeopleRepository

    @Inject
    lateinit var dataModelMapper: DataModelMapper

    @Inject
    lateinit var uiModelMapper: UiModelMapper

    @Inject
    lateinit var deepLinksUtils: DeepLinksUtils

    private var moviesGridAdapter: BaseMoviesGridAdapter? = null

    abstract val viewModel: BaseMovieGridViewModel

    private var callbacks: MovieGridCallbacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as MovieNightApplication).component().inject(this)
        viewModel.load()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = context ?: return
        val numOfColumns = ViewUtils.calculateNumberOfColumns(context)
        val layoutManager = GridLayoutManager(context, numOfColumns)
        moviesRecyclerView?.layoutManager = layoutManager
        moviesGridAdapter = BaseMoviesGridAdapter(object : BaseMoviesGridAdapter.Callback {
            override fun itemPressed(index: Int) {
                viewModel.itemPressed(index)
            }
        })
        moviesRecyclerView?.adapter = moviesGridAdapter
        moviesRecyclerView?.isNestedScrollingEnabled = false

        retryButton?.setOnClickListener {
            viewModel.load()
        }

        refreshUI()
        viewModel.load()
        observeViewModel()
        setCallback()
    }

    private fun setCallback() {
        (parentFragment as? MovieGridCallbacks)?.let {
            this.callbacks = it
        }
    }

    private fun refreshUI() {
        renderMoviesUIModels(viewModel.moviesUIModels.value)
        renderError(viewModel.error.value)
        handleNavigationEvent(viewModel.navigationEvent.value)
    }

    private fun observeViewModel() {
        viewModel.moviesUIModels.observe(viewLifecycleOwner, Observer { movies ->
            renderMoviesUIModels(movies)
        })
        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            renderError(error)
        })
        viewModel.navigationEvent.observe(viewLifecycleOwner, Observer { navigationEvent ->
            handleNavigationEvent(navigationEvent)
        })
    }

    private fun renderMoviesUIModels(movies: List<MovieGridItemUiModel>?) {
        movies?.let {
            if (it.isEmpty()) {
                onlyShow(Layout.EMPTY)
            } else {
                moviesGridAdapter?.setMovies(it)
                onlyShow(Layout.GRID)
            }
        }
    }

    private fun handleNavigationEvent(navigationEvent: BaseMovieGridViewModel.NavigationEvent?) {
        navigationEvent?.let { event ->
            when (event) {
                is BaseMovieGridViewModel.NavigationEvent.ShowMovieDetailScreen -> {
                    viewModel.navigationEvent.value = null
                    callbacks?.showMovieDetailScreen(event.movieID)
                }
            }
        }
    }

    /**
     * Sets a generic error message and updates layouts' visibility
     */
    private fun renderError(error: Throwable?) {
        error?.let {
            val message = getString(R.string.generic_error)
            updateRetryLayout(message)
        }
    }

    /**
     * Sets the message of the retry layout and updates layouts' visibility
     */
    private fun updateRetryLayout(message: String?) {
        message?.let {
            retryMessageTextView?.text = it
            onlyShow(Layout.RETRY)
        }
    }

    /**
     * Makes one 1 of [Layout] layouts visible.
     *
     * @param layout Make sure only this layout is visible
     */
    private fun onlyShow(layout: Layout) {
        moviesRecyclerView?.gone()
        retryLayout?.gone()
        emptyLayout?.gone()
        when (layout) {
            Layout.GRID -> moviesRecyclerView?.visible()
            Layout.RETRY -> retryLayout?.visible()
            Layout.EMPTY -> emptyLayout?.visible()
        }
    }

    enum class Layout {
        GRID,
        RETRY,
        EMPTY
    }

    interface MovieGridCallbacks {
        fun showMovieDetailScreen(movieID: Int)
    }
}