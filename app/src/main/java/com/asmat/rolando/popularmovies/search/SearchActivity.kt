package com.asmat.rolando.popularmovies.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.asmat.rolando.popularmovies.MovieNightApplication
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.extensions.setNearBottomScrollListener
import com.asmat.rolando.popularmovies.model.mappers.UiModelMapper
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.repositories.PeopleRepository
import com.asmat.rolando.popularmovies.ui.castdetails.CastDetailsActivity
import com.asmat.rolando.popularmovies.ui.moviedetails.MovieDetailsActivity
import kotlinx.android.synthetic.main.activity_search_results.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity(), SearchAdapter.Callbacks {

    @Inject lateinit var moviesRepository: MoviesRepository
    @Inject lateinit var peopleRepository: PeopleRepository
    @Inject lateinit var mapper: UiModelMapper
    lateinit var viewModel: SearchViewModel
    private var searchview: SearchView? =null
    private var adapter: SearchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as MovieNightApplication).component().inject(this)
        setContentView(R.layout.activity_search_results)
        viewModel = SearchViewModel(moviesRepository, peopleRepository, mapper)
        setupToolbar()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = SearchAdapter(this)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = GridLayoutManager(this, 2)
        recyclerView?.setNearBottomScrollListener {
            viewModel.loadMore()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    // Enable Back arrow
    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bar = supportActionBar
        bar?.setDisplayHomeAsUpEnabled(true)
    }

    // User entered search term and pressed the search button
    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            viewModel.setSearchTerm(query)
        }
    }

    private fun observeViewModel() {
        viewModel.searchHint.observe(this, Observer { hint ->
            updateSearchHint(hint)
        })
        viewModel.results.observe(this, Observer { results ->
            updateResults(results)
        })
    }

    private fun updateSearchHint(hint: String) {
        searchview?.queryHint = hint
    }

    private fun updateResults(items: List<SearchViewModel.SearchResultUiModel>) {
        adapter?.setData(items)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the options menu from XML
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search_results, menu)
        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchview = menu.findItem(R.id.search).actionView as SearchView
        // Assumes current activity is the searchable activity
        searchview?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchview?.isIconified = false
        searchview?.setIconifiedByDefault(true)
        searchview?.isSubmitButtonEnabled = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when(it.itemId) {
                R.id.search_movies -> {
                    viewModel.setSearchMode(SearchViewModel.SearchMode.MOVIES)
                }
                R.id.search_people -> {
                    viewModel.setSearchMode(SearchViewModel.SearchMode.PEOPLE)
                }
                else -> { }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun openMovieDetails(id: Int) {
        val intent = MovieDetailsActivity.createIntent(this, id)
        startActivity(intent)
    }

    override fun openActorDetails(id: Int) {
        val intent = CastDetailsActivity.createIntent(this, id)
        startActivity(intent)
    }
}