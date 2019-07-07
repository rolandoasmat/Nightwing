package com.asmat.rolando.popularmovies.ui.castdetails

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import com.asmat.rolando.popularmovies.MovieNightApplication

import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.repositories.PeopleRepository
 import com.asmat.rolando.popularmovies.ui.castdetails.personmoviecredits.PersonMovieCreditsFragment
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.activity_cast_detail.*
import java.lang.IllegalStateException
import javax.inject.Inject

class CastDetailsActivity : AppCompatActivity(), PersonMovieCreditsFragment.Listener {

    companion object {
        const val PERSON_ID_KEY = "PERSON_ID_KEY"
    }

    @Inject
    lateinit var moviesRepository: MoviesRepository
    @Inject
    lateinit var peopleRepository: PeopleRepository

    private val personID: Int
            get() = intent?.getIntExtra(PERSON_ID_KEY, -1) ?: throw IllegalStateException("No person ID found.")

    lateinit var viewModel: CastDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as MovieNightApplication).component().inject(this)
        setContentView(R.layout.activity_cast_detail)
        viewModel = ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository)).get(CastDetailsViewModel::class.java)
        viewModel.init(personID)
        setup()
    }


    //region setup

    private fun setup() {
        setupToolbar()
        observeViewModel()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun observeViewModel() {
        viewModel.name.observe(this, Observer {
            collapsingToolbar.title = it
        })
        viewModel.uiModel.observe(this, Observer { uiModel ->
            uiModel?.let { setupViewPager(uiModel) }
        })
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

    private fun setupViewPager(uiModel: CastDetailsUiModel) {
        val adapter = CastDetailsPagerAdapter(uiModel, supportFragmentManager, this)
        castDetailsViewPager?.adapter = adapter
        tabLayout?.setupWithViewPager(castDetailsViewPager)
    }

    override fun onMoviePressed(position: Int) {
        Log.v("TESTTAG", "Movie credit pressed: $position")
    }
}
