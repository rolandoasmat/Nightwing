package com.asmat.rolando.popularmovies.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.adapters.ReviewsLinearAdapter;
import com.asmat.rolando.popularmovies.adapters.TrailersLinearAdapter;
import com.asmat.rolando.popularmovies.data.PopularMoviesContract;
import com.asmat.rolando.popularmovies.managers.MovieApiManager;
import com.asmat.rolando.popularmovies.models.Movie;
import com.asmat.rolando.popularmovies.models.Review;
import com.asmat.rolando.popularmovies.models.TrailerAdapterOnClickHandler;
import com.asmat.rolando.popularmovies.models.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapterOnClickHandler {

    @BindView(R.id.iv_movie_backdrop) ImageView mMovieBackdrop;
    @BindView(R.id.iv_poster_thumbnail) ImageView mMoviePoster;
    @BindView(R.id.tv_movie_title) TextView mMovieTitle;
    @BindView(R.id.tv_release_date) TextView mReleaseDate;
    @BindView(R.id.tv_movie_rating) TextView mMovieRating;
    @BindView(R.id.tv_synopsis_content) TextView mMovieSynopsis;
    @BindView(R.id.rv_trailers) RecyclerView mTrailers;
    private LinearLayoutManager mTrailersLayoutManager;
    private TrailersLinearAdapter mTrailersLinearAdapter;
    @BindView(R.id.rv_reviews) RecyclerView mReviews;
    private LinearLayoutManager mReviewsLinearLayoutManager;
    private ReviewsLinearAdapter mReviewsLinearAdapter;
    @BindView(R.id.star) ImageView star;
    public final static String INTENT_EXTRA_TAG = "MOVIE_DATA";
    private static final int VIDEOS_LOADER = 3948;
    private static final int REVIEWS_LOADER = 2938;
    private LoaderManager.LoaderCallbacks<Video[]> videosCallbacks;
    private LoaderManager.LoaderCallbacks<Review[]> reviewsCallbacks;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity != null) {
            if(intentThatStartedThisActivity.hasExtra(INTENT_EXTRA_TAG)) {
                movie = intentThatStartedThisActivity.getParcelableExtra(INTENT_EXTRA_TAG);
                populateViews(movie);
            }
        }
        updateActionBarTitle(R.string.movie_detail_activity_title);
        setVideosLoaderCallback();
        setReviewsLoaderCallback();

        setupTrailersRecyclerView();
        setupReviewsRecyclerView();

        getSupportLoaderManager().initLoader(VIDEOS_LOADER, null, videosCallbacks);
        getSupportLoaderManager().initLoader(REVIEWS_LOADER, null, reviewsCallbacks);

        setStarStatus();
    }

    @Override
    public void onClick(Video trailer) {
        String url = trailer.youtubeUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setStarStatus() {
        if(isMovieFavorited()) {
            fillStar();
        } else {
            unfillStar();
        }
    }


    // TODO user a new query instead
    private boolean isMovieFavorited() {
        int id = movie.getId();
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(PopularMoviesContract.FavoritesEntry.CONTENT_URI,null,null,null,null);
        while(cursor.moveToNext()) {
            if(Movie.getMovieFromCursorEntry(cursor).getId() == id) {
                return true;
            }
        }
        cursor.close();
        return false;
    }

    private void fillStar() {
        star.setImageResource(R.drawable.ic_star_yellow_24dp);
    }

    private void unfillStar() {
        star.setImageResource(R.drawable.ic_star_gray_24dp);
    }

    public void onShare(View view) {
        String mimeType = "text/plain";
        String title = "Share movie...";
        String movieTitle = movie.getTitle();
        String movieTrailerUrl = mTrailersLinearAdapter.getTrailers()[0].youtubeUrl();
        String textToShare = "Check out the trailer for "+movieTitle+"!\n"+movieTrailerUrl;

        Intent intent = ShareCompat.IntentBuilder.from(this)
                .setChooserTitle(title)
                .setType(mimeType)
                .setText(textToShare).getIntent();
        startActivity(intent);
    }

    public void onStar(View view) {
        int movieID = movie.getId();
        if(isMovieFavorited()) {
            getContentResolver().delete(PopularMoviesContract.FavoritesEntry.CONTENT_URI,
                    PopularMoviesContract.FavoritesEntry.COLUMN_MOVIE_ID+" = "+ movieID,
                    null);
        } else {
            String movieTitle = movie.getTitle();
            String posterUrl = movie.getPosterURL();
            String backdropurl = movie.getbackdropURL();
            String movieSynopsis = movie.getPlotSynopsis();
            double movieRating = movie.getUserRating();
            String releaseDate = movie.getReleaseDate();

            ContentValues contentValues = new ContentValues();
            contentValues.put(PopularMoviesContract.FavoritesEntry.COLUMN_MOVIE_ID, movieID);
            contentValues.put(PopularMoviesContract.FavoritesEntry.COLUMN_TITLE, movieTitle);
            contentValues.put(PopularMoviesContract.FavoritesEntry.COLUMN_POSTER_URL, posterUrl);
            contentValues.put(PopularMoviesContract.FavoritesEntry.COLUMN_BACKDROP_URL, backdropurl );
            contentValues.put(PopularMoviesContract.FavoritesEntry.COLUMN_SYNOPSIS, movieSynopsis);
            contentValues.put(PopularMoviesContract.FavoritesEntry.COLUMN_RATING, movieRating);
            contentValues.put(PopularMoviesContract.FavoritesEntry.COLUMN_RELEASE_DATE, releaseDate);

            getContentResolver().insert(PopularMoviesContract.FavoritesEntry.CONTENT_URI, contentValues);
        }
        setStarStatus();
    }

    private void setupTrailersRecyclerView() {
        mTrailers.setHasFixedSize(true);
        mTrailersLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTrailers.setLayoutManager(mTrailersLayoutManager);
        mTrailersLinearAdapter = new TrailersLinearAdapter(this);
        mTrailers.setAdapter(mTrailersLinearAdapter);
    }

    private void setupReviewsRecyclerView() {
        mReviews.setHasFixedSize(true);
        mReviewsLinearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviews.setLayoutManager(mReviewsLinearLayoutManager);
        mReviewsLinearAdapter = new ReviewsLinearAdapter();
        mReviews.setAdapter(mReviewsLinearAdapter);
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
                            return MovieApiManager.fetchMovieVideos(movie.getId(), 1);
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
                            return MovieApiManager.fetchMovieReviews(movie.getId(), 1);
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
                    // TODO no reviews state
                    mReviewsLinearAdapter.setReviews(data);
                }
            }

            @Override
            public void onLoaderReset(Loader<Review[]> loader) { }
        };
    }

    private void populateViews(Movie movie){
        Picasso.with(this).load(movie.getBackdropUrlComplete()).into(mMovieBackdrop);
        Picasso.with(this).load(movie.getPosterUrlComplete()).into(mMoviePoster);
        mMovieTitle.setText(movie.getTitle());
        String formatted = movie.getReleaseDateFormatted();
        mReleaseDate.setText(formatted);
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
