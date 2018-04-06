package com.asmat.rolando.popularmovies.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

    @BindView(R.id.toolbarImage) ImageView mMovieBackdrop;
    @BindView(R.id.iv_poster_thumbnail) ImageView mMoviePoster;
    @BindView(R.id.tv_release_date) TextView mReleaseDate;
    @BindView(R.id.tv_movie_rating) TextView mMovieRating;
    @BindView(R.id.tv_synopsis_content) TextView mMovieSynopsis;
    @BindView(R.id.rv_trailers) RecyclerView mTrailers;
    @BindView(R.id.pb_trailers_loading_bar) ProgressBar mTrailersLoading;
    @BindView(R.id.tv_no_trailers) TextView mNoTrailersLabel;
    @BindView(R.id.tv_error_trailers) TextView mTrailersErrorLabel;
    private LinearLayoutManager mTrailersLayoutManager;
    private TrailersLinearAdapter mTrailersLinearAdapter;
    @BindView(R.id.rv_reviews) RecyclerView mReviews;
    @BindView(R.id.pb_reviews_loading_bar) ProgressBar mReviewsLoading;
    @BindView(R.id.tv_no_reviews) TextView mNoReviewsLabel;
    @BindView(R.id.tv_error_reviews) TextView mReviewsErrorLabel;
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

        setVideosLoaderCallback();
        setReviewsLoaderCallback();

        setupTrailersRecyclerView();
        setupReviewsRecyclerView();

        getSupportLoaderManager().initLoader(VIDEOS_LOADER, null, videosCallbacks);
        getSupportLoaderManager().initLoader(REVIEWS_LOADER, null, reviewsCallbacks);

        setStarStatus();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                onBackPressed();
//            }
//        });
    }

    @Override
    public void onClick(Video trailer) {
        String url = trailer.getYouTubeURL();
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
        String title = getResources().getString(R.string.share_movie);
        String movieTitle = movie.getTitle();
        String textToShare = getResources().getString(R.string.check_out_movie)+
                " \""+movieTitle+"\"";
        Video[] videos = mTrailersLinearAdapter.getTrailers();
        if(videos != null && videos.length > 0) {
            textToShare += "\n" + videos[0].getYouTubeURL();
        }

        Intent intent = ShareCompat.IntentBuilder.from(this)
                .setChooserTitle(title)
                .setType(mimeType)
                .setText(textToShare).getIntent();
        startActivity(Intent.createChooser(intent, title));
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
            String backdropurl = movie.getBackdropPath();
            String movieSynopsis = movie.getOverview();
            double movieRating = movie.getVoteAverage();
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
        mReviewsLinearLayoutManager.setSmoothScrollbarEnabled(true);

        mReviewsLinearAdapter = new ReviewsLinearAdapter();

        mReviews.setAdapter(mReviewsLinearAdapter);
        mReviews.setNestedScrollingEnabled(false);
    }

    private void setVideosLoaderCallback() {
        videosCallbacks = new LoaderManager.LoaderCallbacks<Video[]>() {
            @Override
            public Loader<Video[]> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<Video[]>(getBaseContext()) {
                    @Override
                    protected void onStartLoading() {
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
                mTrailersLoading.setVisibility(View.GONE);
                if(data == null) {
                    mTrailersErrorLabel.setVisibility(View.VISIBLE);
                } else {
                    if(data.length == 0) {
                        mNoTrailersLabel.setVisibility(View.VISIBLE);
                    } else {
                        ArrayList<Video> trailers = new ArrayList<>();
                        for(Video video: data) {
                            if(video.getType().equals("Trailer")){
                                trailers.add(video);
                            }
                        }
                        if(trailers.size() == 0) {
                            mNoTrailersLabel.setVisibility(View.VISIBLE);
                        } else {
                            Video[] array = trailers.toArray(new Video[0]);
                            mTrailersLinearAdapter.setTrailers(array);
                            mTrailers.setVisibility(View.VISIBLE);
                        }
                    }
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
                mReviewsLoading.setVisibility(View.GONE);
                if(data == null) {
                    mReviewsErrorLabel.setVisibility(View.VISIBLE);
                } else {
                    if(data.length == 0) {
                        mNoReviewsLabel.setVisibility(View.VISIBLE);
                    } else {
                        mReviews.setVisibility(View.VISIBLE);
                        mReviewsLinearAdapter.setReviews(data);
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<Review[]> loader) { }
        };
    }

    private void populateViews(Movie movie){
        Picasso.with(this).load(movie.getBackdropURL()).into(mMovieBackdrop);
        Picasso.with(this).load(movie.getPosterURL()).into(mMoviePoster);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitle(movie.getTitle());
        String formatted = movie.getReleaseDateFormatted();
        mReleaseDate.setText(formatted);
        String rating = movie.getVoteAverage()+getString(R.string.out_of_ten);
        mMovieRating.setText(rating);
        mMovieSynopsis.setText(movie.getOverview());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}