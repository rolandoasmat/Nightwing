package com.asmat.rolando.popularmovies.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_movie_backdrop) ImageView mMovieBackdrop;

    @BindView(R.id.iv_poster_thumbnail) ImageView mMoviePoster;
    @BindView(R.id.tv_movie_title) TextView mMovieTitle;
    @BindView(R.id.tv_release_date) TextView mReleaseDate;
    @BindView(R.id.tv_movie_rating) TextView mMovieRating;

    @BindView(R.id.tv_synopsis_content) TextView mMovieSynopsis;

    final private String INTENT_EXTRA_TAG = "MOVIE_DATA";
    final private String DATE_FORMAT = "MMMM dd, yyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity != null) {
            if(intentThatStartedThisActivity.hasExtra(INTENT_EXTRA_TAG)) {
                Movie movie = intentThatStartedThisActivity.getParcelableExtra(INTENT_EXTRA_TAG);
                populateViews(movie);
            }
        }
        updateActionBarTitle(R.string.movie_detail_activity_title);
    }

    private void populateViews(Movie movie){
        Picasso.with(this).load(movie.getbackdropURL()).into(mMovieBackdrop);
        Picasso.with(this).load(movie.getPosterURL()).into(mMoviePoster);
        mMovieTitle.setText(movie.getTitle());
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String dateString = sdf.format(movie.getReleaseDate());
        mReleaseDate.setText(dateString);
        String rating = movie.getUserRating()+getString(R.string.out_of_ten);
        mMovieRating.setText(rating);
        mMovieSynopsis.setText(movie.getPlotSynopsis());

    }

    private void updateActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    private void updateActionBarTitle(int stringID){
        updateActionBarTitle(getString(stringID));
    }
}
