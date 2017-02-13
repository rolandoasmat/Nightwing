package com.asmat.rolando.popularmovies.activities;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView mMovieBackdrop;

    private ImageView mMoviePoster;
    private TextView mMovieTitle;
    private TextView mReleaseDate;
    private TextView mMovieRating;

    private TextView mMovieSynopsis;

    final private String INTENT_EXTRA_TAG = "MOVIE_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mMovieBackdrop = (ImageView) findViewById(R.id.iv_movie_backdrop);
        mMoviePoster = (ImageView) findViewById(R.id.iv_poster_thumbnail);
        mMovieTitle = (TextView) findViewById(R.id.tv_movie_title);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mMovieRating = (TextView) findViewById(R.id.tv_movie_rating);
        mMovieSynopsis = (TextView) findViewById(R.id.tv_synopsis_content);

        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity != null) {
            if(intentThatStartedThisActivity.hasExtra(INTENT_EXTRA_TAG)) {
                Movie movie = (Movie) intentThatStartedThisActivity.getParcelableExtra(INTENT_EXTRA_TAG);
                populateViews(movie);
            }
        }
    }

    private void populateViews(Movie movie){
        Picasso.with(this).load(movie.getbackdropURL()).into(mMovieBackdrop);
        Picasso.with(this).load(movie.getPosterURL()).into(mMoviePoster);
        mMovieTitle.setText(movie.getTitle());
        mReleaseDate.setText(movie.getReleaseDate().toString());
        mMovieRating.setText(movie.getUserRating()+"/10.0");
        mMovieSynopsis.setText(movie.getPlotSynopsis());

    }
}
