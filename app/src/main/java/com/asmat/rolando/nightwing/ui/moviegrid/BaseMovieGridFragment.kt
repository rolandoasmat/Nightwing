package com.asmat.rolando.nightwing.ui.moviegrid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.asmat.rolando.nightwing.NightwingApplication
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.deep_links.DeepLinksUtils
import com.asmat.rolando.nightwing.extensions.gone
import com.asmat.rolando.nightwing.extensions.visible
import com.asmat.rolando.nightwing.model.mappers.DataModelMapper
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import com.asmat.rolando.nightwing.repositories.PeopleRepository
import com.asmat.rolando.nightwing.utilities.ViewUtils
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_movie_grid.*
import kotlinx.android.synthetic.main.retry_layout.*
import javax.inject.Inject

abstract class BaseMovieGridFragment: androidx.fragment.app.Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var moviesGridAdapter: BaseMoviesGridAdapter? = null

    abstract val viewModel: BaseMovieGridViewModel

    abstract fun goToMovieDetailsScreen(movieID: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as NightwingApplication).component().inject(this)
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
            override fun itemPressed(movieID: Int) {
                goToMovieDetailsScreen(movieID)
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
    }

    private fun refreshUI() {
        renderMoviesUIModels(viewModel.uiModels.value)
        renderError(viewModel.error.value)
    }

    private fun observeViewModel() {
        viewModel.uiModels.observe(viewLifecycleOwner, Observer { movies ->
            renderMoviesUIModels(movies)
        })
        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            renderError(error)
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