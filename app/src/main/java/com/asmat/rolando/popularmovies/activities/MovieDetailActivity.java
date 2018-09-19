package com.asmat.rolando.popularmovies.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ShareCompat;
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
import com.asmat.rolando.popularmovies.database.DatabaseManager;
import com.asmat.rolando.popularmovies.database.FavoriteMovie;
import com.asmat.rolando.popularmovies.database.Movie;
import com.asmat.rolando.popularmovies.database.WatchLaterMovie;
import com.asmat.rolando.popularmovies.models.AdapterOnClickHandler;
import com.asmat.rolando.popularmovies.models.Review;
import com.asmat.rolando.popularmovies.models.Video;
import com.asmat.rolando.popularmovies.viewmodels.MovieDetailsViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements AdapterOnClickHandler<Video> {
    // Movie details
    @BindView(R.id.toolbarImage)
    ImageView mMovieBackdrop;
    @BindView(R.id.iv_poster_thumbnail)
    ImageView mMoviePoster;
    @BindView(R.id.tv_release_date)
    TextView mReleaseDate;
    @BindView(R.id.tv_movie_rating)
    TextView mMovieRating;
    @BindView(R.id.tv_synopsis_content)
    TextView mMovieSynopsis;
    // Trailers
    @BindView(R.id.rv_trailers)
    RecyclerView mTrailers;
    @BindView(R.id.pb_trailers_loading_bar)
    ProgressBar mTrailersLoading;
    @BindView(R.id.tv_no_trailers)
    TextView mNoTrailersLabel;
    @BindView(R.id.tv_error_trailers)
    TextView mTrailersErrorLabel;
    private LinearLayoutManager mTrailersLayoutManager;
    private TrailersLinearAdapter mTrailersLinearAdapter;
    // Reviews
    @BindView(R.id.rv_reviews)
    RecyclerView mReviews;
    @BindView(R.id.pb_reviews_loading_bar)
    ProgressBar mReviewsLoading;
    @BindView(R.id.tv_no_reviews)
    TextView mNoReviewsLabel;
    @BindView(R.id.tv_error_reviews)
    TextView mReviewsErrorLabel;
    private LinearLayoutManager mReviewsLayoutManager;
    private ReviewsLinearAdapter mReviewsLinearAdapter;
    // Star
    @BindView(R.id.star)
    ImageView star;
    // Bookmark
    @BindView(R.id.bookmark)
    ImageView bookmark;

    public final static String INTENT_EXTRA_MOVIE_ID = "MOVIE_ID";

    // ViewModel
    private MovieDetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(INTENT_EXTRA_MOVIE_ID)) {
            int movieID = intent.getIntExtra(INTENT_EXTRA_MOVIE_ID, 0);
            viewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);
            viewModel.init(movieID);
        }
        viewModel.getMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                MovieDetailActivity.this.updateMovieDetails(movie);
            }
        });
        viewModel.getFavoriteMovie().observe(this, new Observer<FavoriteMovie>() {
            @Override
            public void onChanged(@Nullable FavoriteMovie favoriteMovie) {
                MovieDetailActivity.this.updateStarIcon(favoriteMovie);
            }
        });
        viewModel.getWatchLaterMovie().observe(this, new Observer<WatchLaterMovie>() {
            @Override
            public void onChanged(@Nullable WatchLaterMovie watchLaterMovie) {
                MovieDetailActivity.this.updateBookmarkIcon(watchLaterMovie);
            }
        });
        viewModel.getVideos().observe(this, new Observer<Video[]>() {
            @Override
            public void onChanged(@Nullable Video[] videos) {
                MovieDetailActivity.this.updateTrailers(videos);
            }
        });
        viewModel.getReviews().observe(this, new Observer<Review[]>() {
            @Override
            public void onChanged(@Nullable Review[] reviews) {
                MovieDetailActivity.this.updateReviews(reviews);
            }
        });

        setupToolbar();
        setupTrailersRecyclerView();
        setupReviewsRecyclerView();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * User Actions
     */
    public void onStar(View view) {
        Movie movie = MovieDetailActivity.this.viewModel.getMovie().getValue();
        if (movie == null) return;
        FavoriteMovie favoriteMovie = new FavoriteMovie(movie.getId());
        if (this.viewModel.getFavoriteMovie().getValue() == null) {
            // Add the Favorite movie
            DatabaseManager.INSTANCE.addMovie(movie);
            DatabaseManager.INSTANCE.addFavoriteMovie(favoriteMovie);
        } else {
            DatabaseManager.INSTANCE.deleteFavoriteMovie(favoriteMovie);
            // Check if it's a watch later movie, delete movie ref if it's not
            if (this.viewModel.getWatchLaterMovie().getValue() == null) {
                DatabaseManager.INSTANCE.deleteMovie(movie);
            }
        }
    }

    public void onBookmark(View view) {
        Movie movie = MovieDetailActivity.this.viewModel.getMovie().getValue();
        if (movie == null) return;
        WatchLaterMovie watchLaterMovie = new WatchLaterMovie(movie.getId());
        if (this.viewModel.getWatchLaterMovie().getValue() == null) {
            DatabaseManager.INSTANCE.addMovie(movie);
            DatabaseManager.INSTANCE.addWatchLaterMovie(watchLaterMovie);
        } else {
            DatabaseManager.INSTANCE.deleteWatchLaterMovie(watchLaterMovie);
            if (this.viewModel.getFavoriteMovie().getValue() == null) {
                DatabaseManager.INSTANCE.deleteMovie(movie);
            }
        }
    }

    public void onShare(View view) {
        String mimeType = "text/plain";
        String title = getResources().getString(R.string.share_movie);
        Movie movie = viewModel.getMovie().getValue();
        String movieTitle = movie.getTitle();
        String textToShare = getResources().getString(R.string.check_out_movie)+
                " \""+movieTitle+"\"";
        Video[] videos = mTrailersLinearAdapter.getData();
        if(videos != null && videos.length > 0) {
            textToShare += "\n" + videos[0].getYouTubeURL();
        }

        Intent intent = ShareCompat.IntentBuilder.from(this)
                .setChooserTitle(title)
                .setType(mimeType)
                .setText(textToShare).getIntent();
        startActivity(Intent.createChooser(intent, title));
    }

    /**
     * Private methods
     */

    private void fillStar() {
        star.setImageResource(R.drawable.ic_star_filled);
    }

    private void unfillStar() {
        star.setImageResource(R.drawable.ic_star);
    }

    private void fillBookmark() {
        bookmark.setImageResource(R.drawable.ic_bookmark_filled);
    }

    private void unfillBookmark() {
        bookmark.setImageResource(R.drawable.ic_bookmark);
    }

    private void setupTrailersRecyclerView() {
        mTrailers.setHasFixedSize(true);
        mTrailersLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTrailers.setLayoutManager(mTrailersLayoutManager);
        mTrailersLinearAdapter = new TrailersLinearAdapter(this);
        mTrailers.setAdapter(mTrailersLinearAdapter);
        mTrailers.setNestedScrollingEnabled(false);
    }

    private void setupReviewsRecyclerView() {
        mReviews.setHasFixedSize(true);
        mReviewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviews.setLayoutManager(mReviewsLayoutManager);
        mReviewsLayoutManager.setSmoothScrollbarEnabled(true);
        mReviewsLinearAdapter = new ReviewsLinearAdapter();
        mReviews.setAdapter(mReviewsLinearAdapter);
        mReviews.setNestedScrollingEnabled(false);
    }

    private void updateMovieDetails(Movie movie) {
        if (movie != null) {
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
    }

    private void updateStarIcon(FavoriteMovie favoriteMovie) {
        if (favoriteMovie == null) {
            unfillStar();
        } else {
            fillStar();
        }
    }

    private void updateBookmarkIcon(WatchLaterMovie watchLaterMovie) {
        if (watchLaterMovie == null) {
            unfillBookmark();
        } else {
            fillBookmark();
        }
    }

    private void updateTrailers(Video[] videos) {
        mTrailersLoading.setVisibility(View.GONE);
        if (videos == null) {
            mTrailersErrorLabel.setVisibility(View.VISIBLE);
        } else {
            if(videos.length == 0) {
                mNoTrailersLabel.setVisibility(View.VISIBLE);
            } else {
                ArrayList<Video> trailers = new ArrayList<>();
                for(Video video: videos) {
                    if(video.getType().equals("Trailer")){
                        trailers.add(video);
                    }
                }
                if(trailers.size() == 0) {
                    mNoTrailersLabel.setVisibility(View.VISIBLE);
                } else {
                    Video[] array = trailers.toArray(new Video[0]);
                    mTrailersLinearAdapter.setData(array);
                    mTrailers.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void updateReviews(Review[] reviews) {
        mReviewsLoading.setVisibility(View.GONE);
        if(reviews == null) {
            mReviewsErrorLabel.setVisibility(View.VISIBLE);
        } else {
            if(reviews.length == 0) {
                mNoReviewsLabel.setVisibility(View.VISIBLE);
            } else {
                mReviews.setVisibility(View.VISIBLE);
                mReviewsLinearAdapter.setData(reviews);
            }
        }
    }
}