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
import com.asmat.rolando.popularmovies.adapters.CastLinearAdapter;
import com.asmat.rolando.popularmovies.adapters.ReviewsLinearAdapter;
import com.asmat.rolando.popularmovies.adapters.TrailersLinearAdapter;
import com.asmat.rolando.popularmovies.database.DatabaseManager;
import com.asmat.rolando.popularmovies.database.FavoriteMovie;
import com.asmat.rolando.popularmovies.database.Movie;
import com.asmat.rolando.popularmovies.database.WatchLaterMovie;
import com.asmat.rolando.popularmovies.models.AdapterOnClickHandler;
import com.asmat.rolando.popularmovies.models.Credit;
import com.asmat.rolando.popularmovies.models.Review;
import com.asmat.rolando.popularmovies.models.Video;
import com.asmat.rolando.popularmovies.viewmodels.MovieDetailsViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements AdapterOnClickHandler<Video> {

    @BindView(R.id.toolbarImage)
    ImageView backdrop;
    @BindView(R.id.iv_poster_thumbnail)
    ImageView thumbnail;
    @BindView(R.id.tv_release_date)
    TextView releaseDateLabel;
    @BindView(R.id.tv_movie_rating)
    TextView ratingLabel;
    @BindView(R.id.tv_synopsis_content)
    TextView synopsisLabel;

    // TODO raa create custom view to wrap label, rv, loader, error label, empty label
    // TODO raa use data bindings
    // Trailers
    @BindView(R.id.rv_trailers)
    RecyclerView trailers;
    @BindView(R.id.pb_trailers_loading_bar)
    ProgressBar trailersLoading;
    @BindView(R.id.tv_no_trailers)
    TextView noTrailersLabel;
    @BindView(R.id.tv_error_trailers)
    TextView trailersErrorLabel;
    private TrailersLinearAdapter trailersLinearAdapter;

    // Cast
    @BindView(R.id.cast_recycler_view)
    RecyclerView cast;
    @BindView(R.id.cast_loading)
    ProgressBar castLoading;
    @BindView(R.id.cast_empty)
    TextView noCastLabel;
    @BindView(R.id.cast_error)
    TextView castErrorLabel;
    private CastLinearAdapter castLinearAdapter;

    // Reviews
    @BindView(R.id.rv_reviews)
    RecyclerView reviews;
    @BindView(R.id.pb_reviews_loading_bar)
    ProgressBar reviewsLoading;
    @BindView(R.id.tv_no_reviews)
    TextView noReviewsLabel;
    @BindView(R.id.tv_error_reviews)
    TextView reviewsErrorLabel;
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
        viewModel.getCredit().observe(this, new Observer<Credit>() {
            @Override
            public void onChanged(@Nullable Credit credit) {
                MovieDetailActivity.this.updateCredit(credit);
            }
        });

        viewModel.getReviews().observe(this, new Observer<Review[]>() {
            @Override
            public void onChanged(@Nullable Review[] reviews) {
                MovieDetailActivity.this.updateReviews(reviews);
            }
        });

        setup();
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

    //region User Actions

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
        Video[] videos = trailersLinearAdapter.getData();
        if(videos != null && videos.length > 0) {
            textToShare += "\n" + videos[0].getYouTubeURL();
        }

        Intent intent = ShareCompat.IntentBuilder.from(this)
                .setChooserTitle(title)
                .setType(mimeType)
                .setText(textToShare).getIntent();
        startActivity(Intent.createChooser(intent, title));
    }

    //endregion

    /**
     * Private methods
     */

    //region Update UI

    private void updateMovieDetails(Movie movie) {
        if (movie != null) {
            Picasso.with(this).load(movie.getBackdropURL()).into(backdrop);
            Picasso.with(this).load(movie.getPosterURL()).into(thumbnail);
            CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
            collapsingToolbarLayout.setTitle(movie.getTitle());
            String formatted = movie.getReleaseDateFormatted();
            releaseDateLabel.setText(formatted);
            String rating = movie.getVoteAverage()+getString(R.string.out_of_ten);
            ratingLabel.setText(rating);
            synopsisLabel.setText(movie.getOverview());
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
        trailersLoading.setVisibility(View.GONE);
        if (videos == null) {
            trailersErrorLabel.setVisibility(View.VISIBLE);
        } else {
            if(videos.length == 0) {
                noTrailersLabel.setVisibility(View.VISIBLE);
            } else {
                ArrayList<Video> trailers = new ArrayList<>();
                for(Video video: videos) {
                    if(video.getType().equals("Trailer")){
                        trailers.add(video);
                    }
                }
                if(trailers.size() == 0) {
                    noTrailersLabel.setVisibility(View.VISIBLE);
                } else {
                    Video[] array = trailers.toArray(new Video[0]);
                    trailersLinearAdapter.setData(array);
                    this.trailers.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void updateCredit(Credit credit) {
        castLoading.setVisibility(View.GONE);
        if (credit == null) {
            castErrorLabel.setVisibility(View.VISIBLE);
        } else {
            if(credit.getCast().length == 0) {
                noCastLabel.setVisibility(View.VISIBLE);
            } else {
                castLinearAdapter.setData(credit.getCast());
                this.cast.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateReviews(Review[] reviews) {
        reviewsLoading.setVisibility(View.GONE);
        if(reviews == null) {
            reviewsErrorLabel.setVisibility(View.VISIBLE);
        } else {
            if(reviews.length == 0) {
                noReviewsLabel.setVisibility(View.VISIBLE);
            } else {
                this.reviews.setVisibility(View.VISIBLE);
                mReviewsLinearAdapter.setData(reviews);
            }
        }
    }

    //endregion

    //region setup

    private void setup() {
        setupToolbar();
        setupTrailersRecyclerView();
        setupReviewsRecyclerView();
        setupCastRecyclerView();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupTrailersRecyclerView() {
        trailers.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        trailers.setLayoutManager(layoutManager);
        trailersLinearAdapter = new TrailersLinearAdapter(this);
        trailers.setAdapter(trailersLinearAdapter);
        trailers.setNestedScrollingEnabled(false);
    }

    private void setupCastRecyclerView() {
        cast.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        cast.setLayoutManager(layoutManager);
        castLinearAdapter = new CastLinearAdapter();
        cast.setAdapter(castLinearAdapter);
        cast.setNestedScrollingEnabled(false);
    }

    private void setupReviewsRecyclerView() {
        reviews.setHasFixedSize(true);
        LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reviews.setLayoutManager(reviewsLayoutManager);
        reviewsLayoutManager.setSmoothScrollbarEnabled(true);
        mReviewsLinearAdapter = new ReviewsLinearAdapter();
        reviews.setAdapter(mReviewsLinearAdapter);
        reviews.setNestedScrollingEnabled(false);
    }

    //endregion

    //region Icons

    private void fillStar() {
        star.setImageResource(R.drawable.ic_star_filled);
    } // TODO raa use selectors instead

    private void unfillStar() {
        star.setImageResource(R.drawable.ic_star);
    }

    private void fillBookmark() {
        bookmark.setImageResource(R.drawable.ic_bookmark_filled);
    }

    private void unfillBookmark() {
        bookmark.setImageResource(R.drawable.ic_bookmark);
    }

    //endregion
}