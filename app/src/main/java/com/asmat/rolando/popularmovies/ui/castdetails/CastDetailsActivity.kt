package com.asmat.rolando.popularmovies.ui.castdetails

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.MovieNightApplication
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.di.ViewModelFactory
import com.asmat.rolando.popularmovies.extensions.gone
import com.asmat.rolando.popularmovies.extensions.visible
import com.asmat.rolando.popularmovies.ui.castdetails.personmoviecredits.PersonMovieCreditsFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_cast_detail.*
import javax.inject.Inject

class CastDetailsActivity : AppCompatActivity(), PersonMovieCreditsFragment.Listener {

    companion object {
        const val PERSON_ID_KEY = "PERSON_ID_KEY"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val personID: Int
        get() = intent?.getIntExtra(PERSON_ID_KEY, -1)
                ?: throw IllegalStateException("No person ID found.")

    lateinit var viewModel: CastDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as MovieNightApplication).component().inject(this)
        setContentView(R.layout.activity_cast_detail)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CastDetailsViewModel::class.java)
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
        viewModel.loading.observe(this, Observer { loading ->
            updateLoading(loading == true)
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

    //region Callbacks
    override fun onMoviePressed(position: Int) {
        Log.v("TESTTAG", "Movie credit pressed: $position")
    }

    override fun backdropURL(url: String?) {
        url?.let {
            Picasso.get().load(it).into(toolbarImage)
        }
    }
    //endregion

    private fun updateLoading(loading: Boolean) {
        if (loading) {
            loadingView?.visible()
        } else {
            loadingView?.gone()
        }
    }
}
