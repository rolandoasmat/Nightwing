package com.asmat.rolando.popularmovies.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.asmat.rolando.popularmovies.MovieNightApplication

import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.repositories.PeopleRepository
import com.asmat.rolando.popularmovies.ui.cast_detail.CastDetailViewModel
import com.asmat.rolando.popularmovies.ui.transformations.RoundedTransformation
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_cast_detail.*
import kotlinx.android.synthetic.main.cast_primary_details.*
import java.lang.IllegalStateException
import javax.inject.Inject

class CastDetailActivity : AppCompatActivity() {

    companion object {
        const val PERSON_ID_KEY = "PERSON_ID_KEY"
    }

    @Inject
    lateinit var moviesRepository: MoviesRepository
    @Inject
    lateinit var peopleRepository: PeopleRepository

    private val personID: Int
            get() = intent?.getIntExtra(PERSON_ID_KEY, -1) ?: throw IllegalStateException("No person ID found.")

    lateinit var viewModel: CastDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as MovieNightApplication).component().inject(this)
        setContentView(R.layout.activity_cast_detail)
        viewModel = ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository)).get(CastDetailViewModel::class.java)
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
        viewModel.hometown.observe(this, Observer {
            homeTown.text = it
        })
        viewModel.birthday.observe(this, Observer {
            birthdate.text = it
        })
        viewModel.deathday.observe(this, Observer {
            deathdate.text = it
        })
        viewModel.photoURL.observe(this, Observer {
            Picasso.with(this)
                    .load(it)
                    .resize(342, 513)
                    .centerCrop()
                    .transform(RoundedTransformation(50, 0))
                    .error(R.drawable.person)
                    .into(poster)
        })
        viewModel.biography.observe(this, Observer {
            biography.text = it
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
}
