package com.asmat.rolando.popularmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.adapters.TrailersLinearAdapter;
import com.asmat.rolando.popularmovies.managers.MovieApiManager;
import com.asmat.rolando.popularmovies.models.Movie;
import com.asmat.rolando.popularmovies.models.Review;
import com.asmat.rolando.popularmovies.models.TrailerAdapterOnclickHandler;
import com.asmat.rolando.popularmovies.models.Video;
import com.asmat.rolando.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List>, TrailerAdapterOnclickHandler {

    @BindView(R.id.iv_movie_backdrop) ImageView mMovieBackdrop;
    @BindView(R.id.iv_poster_thumbnail) ImageView mMoviePoster;
    @BindView(R.id.tv_movie_title) TextView mMovieTitle;
    @BindView(R.id.tv_release_date) TextView mReleaseDate;
    @BindView(R.id.tv_movie_rating) TextView mMovieRating;
    @BindView(R.id.tv_synopsis_content) TextView mMovieSynopsis;

    @BindView(R.id.rv_trailers) RecyclerView mTrailers;
    private LinearLayoutManager mTrailersLayoutManager;
    private TrailersLinearAdapter mTrailersLinearAdapter;

    final static String INTENT_EXTRA_TAG = "MOVIE_DATA";
    final private String DATE_FORMAT = "MMMM dd, yyyy";

    private static final int VIDEOS_LOADER = 3948;
    private static final int REVIEWS_LOADER = 2938;

    private LoaderManager.LoaderCallbacks<Video[]> videosCallbacks;
    private LoaderManager.LoaderCallbacks<Review[]> reviewsCallbacks;

    private String movieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity != null) {
            if(intentThatStartedThisActivity.hasExtra(INTENT_EXTRA_TAG)) {
                Movie movie = intentThatStartedThisActivity.getParcelableExtra(INTENT_EXTRA_TAG);
                movieID = movie.getId();
                populateViews(movie);
            }
        }
        updateActionBarTitle(R.string.movie_detail_activity_title);
        setVideosLoaderCallback();
        setReviewsLoaderCallback();

        mTrailers.setHasFixedSize(true);
        mTrailersLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mTrailers.setLayoutManager(mTrailersLayoutManager);
        mTrailersLinearAdapter = new TrailersLinearAdapter(this);
        mTrailers.setAdapter(mTrailersLinearAdapter);

        getSupportLoaderManager().initLoader(VIDEOS_LOADER, null, videosCallbacks);
        getSupportLoaderManager().initLoader(REVIEWS_LOADER, null, reviewsCallbacks);
    }

    @Override
    public void onClick(Video trailer) {
        Context context = this;
        Class destinationClass = MovieDetailActivity.class;
        //TODO create intent to launch video
        System.out.print(trailer.youtubeUrl());
    }

    private void setVideosLoaderCallback() {
        videosCallbacks = new LoaderManager.LoaderCallbacks<Video[]>() {
            @Override
            public Loader<Video[]> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<Video[]>(getBaseContext()) {

                    @Override
                    protected void onStartLoading() {
                        // TODO show loader
                        forceLoad();
                    }

                    @Override
                    public Video[] loadInBackground() {
                        try {
                            return MovieApiManager.fetchMovieVideos(movieID, 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<Video[]> loader, Video[] data) {
                // TODO hide loader
                if(data == null) {
                    // TODO show error message
                } else {
                    System.out.print("Videos fetched.");
                    ArrayList<Video> trailers = new ArrayList<>();
                    for(Video video: data) {
                        if(video.getType().equals("Trailer")){
                            trailers.add(video);
                        }
                    }
                    System.out.print("Filter Trailers.");
                    Video[] array = trailers.toArray(new Video[0]);
                    mTrailersLinearAdapter.setTrailers(array);
                }
            }

            @Override
            public void onLoaderReset(Loader<Video[]> loader) { }
        };
    }

    private void setReviewsLoaderCallback() {
        reviewsCallbacks = new LoaderManager.LoaderCallbacks<Review[]>() {
            @Override
            public Loader<Review[]> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<Review[]>(getBaseContext()) {

                    @Override
                    protected void onStartLoading() {
                        // TODO show loader
                        forceLoad();
                    }

                    @Override
                    public Review[] loadInBackground() {
                        try {
                            return MovieApiManager.fetchMovieReviews(movieID, 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<Review[]> loader, Review[] data) {
                // TODO hide loader
                if(data == null) {
                    // TODO show error message
                } else {
                    System.out.print("GOT REVIEWS");
                }
            }

            @Override
            public void onLoaderReset(Loader<Review[]> loader) { }
        };
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
        // Get Videos and Reviews
        fetchVideos(movie.getId());
        fetchReviews(movie.getId());
    }

    private void fetchVideos(String movieID) {
        try {
            Video[] vides = MovieApiManager.fetchMovieVideos(movieID,1);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void fetchReviews(String movieID) {
        try {
            Review[] reviews = MovieApiManager.fetchMovieReviews(movieID,1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    private void updateActionBarTitle(int stringID){
        updateActionBarTitle(getString(stringID));
    }

    /**
     * ---------------------------- Loader ----------------------------
     */

    @Override
    public Loader<List> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List> loader, List data) {

    }

    @Override
    public void onLoaderReset(Loader<List> loader) {

    }
}
