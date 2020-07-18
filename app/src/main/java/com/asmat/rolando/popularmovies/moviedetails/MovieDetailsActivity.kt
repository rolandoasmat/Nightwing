package com.asmat.rolando.popularmovies.moviedetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.asmat.rolando.popularmovies.MovieNightApplication
import com.asmat.rolando.popularmovies.R

class MovieDetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MOVIE_ID = "extra_movie_id"

        fun createIntent(context: Context, movieID: Int): Intent {
            val destinationClass = MovieDetailsActivity::class.java
            val intentToStartDetailActivity = Intent(context, destinationClass)
            intentToStartDetailActivity.putExtra(EXTRA_MOVIE_ID, movieID)
            return intentToStartDetailActivity
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as MovieNightApplication).component().inject(this)
        setContentView(R.layout.activity_movie_detail)
        if (savedInstanceState == null) {
            val fragment = MovieDetailsFragment.newInstance(intent.extras)
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.movieDetailsContent, fragment)
                    .commit()
        }
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



}