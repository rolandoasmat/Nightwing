package com.asmat.rolando.popularmovies.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asmat.rolando.popularmovies.adapters.MoviesGridAdapter;
import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.managers.MovieApiManager;
import com.asmat.rolando.popularmovies.models.Movie;
import com.asmat.rolando.popularmovies.models.MovieAdapterOnClickHandler;
import com.asmat.rolando.popularmovies.models.Request;
import com.asmat.rolando.popularmovies.models.RequestType;
import com.asmat.rolando.popularmovies.utilities.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity
        extends AppCompatActivity
        implements MovieAdapterOnClickHandler{

    private static String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.rv_movie_grid) RecyclerView mMoviesGrid;
    @BindView(R.id.tv_error_message) TextView mErrorMessageTextView;
    @BindView(R.id.pb_loading_bar) ProgressBar mLoadingBar;

    private MoviesGridAdapter mMoviesGridAdapter;
    private GridLayoutManager mMoviesGridLayoutManager;
    private Request mRequest;
    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        updateActionBarTitle(R.string.most_popular);
        mMoviesGrid.setHasFixedSize(true);
        // LayoutManager
        mMoviesGridLayoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));
        mMoviesGrid.setLayoutManager(mMoviesGridLayoutManager);
        // Set adapter
        mMoviesGridAdapter = new MoviesGridAdapter(this);
        mMoviesGrid.setAdapter(mMoviesGridAdapter);
        // Load initial data
        loadData();
        // Setup scroll listener
        setScrollListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sort_by_top_rated:
                Log.v(TAG, "Sort by top rated");
                sortByTopRated();
                return true;
            case R.id.sort_by_most_popular:
                Log.v(TAG, "Sort by most popular");
                sortByMostPopular();
                return true;
            case R.id.show_favorites:
                Log.v(TAG, "Show favorites");
                // showFavorites(); TODO create this method to show faves using ContentProvider
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(MovieDetailActivity.INTENT_EXTRA_TAG, movie);
        startActivity(intentToStartDetailActivity);
    }

    private void fetchFavoriteMovies() {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(PopularMoviesContract.CONTENT_URI,null,null,null,null);
    }

    private void sortByTopRated() {
        if(mRequest.getRequestType() != RequestType.TOP_RATED) {
            // Only sort if not already sorted by top rated
            mRequest.resetPage();
            mRequest.setRequestType(RequestType.TOP_RATED);
            executeRequest();
            updateActionBarTitle(R.string.top_rated);
        }
    }

    private void sortByMostPopular() {
        if(mRequest.getRequestType() != RequestType.POPULAR) {
            // Only sort if not already sorted by most popular
            mRequest.resetPage();
            mRequest.setRequestType(RequestType.POPULAR);
            executeRequest();
            updateActionBarTitle(R.string.most_popular);
        }
    }

    private void updateActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    private void updateActionBarTitle(int stringID){
        updateActionBarTitle(getString(stringID));
    }

    private void executeRequest(){
        new FetchMoviesTask().execute(this.mRequest);
    }

    private void loadData(){
        // Create request
        mRequest = new Request(RequestType.POPULAR, 1);
        // Execute
        executeRequest();
    }

    private void setScrollListener() {
        mMoviesGrid.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0 && !isLoading) { // User is scrolling down
                    int positionOfLastItem = mMoviesGridLayoutManager.getItemCount()-1;
                    int currentPositionOfLastVisibleItem = mMoviesGridLayoutManager.findLastCompletelyVisibleItemPosition();
                    if(positionOfLastItem == currentPositionOfLastVisibleItem){
                        lastItemReached();
                    }
                }
            }
        }
        );
    }

    private void lastItemReached() {
        Log.v(TAG, "Reached last item of list.");
        mRequest.nextPage();
        // Fetch more movies
        new FetchMoviesTask().execute(mRequest);
    }

    private void showErrorState(String message) {
        mMoviesGrid.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mLoadingBar.setVisibility(View.INVISIBLE);
        Snackbar.make(mMoviesGrid, message, Snackbar.LENGTH_LONG).show();
    }

    private void showLoadingState() {
        isLoading = true;
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mLoadingBar.setVisibility(View.VISIBLE);
        Snackbar.make(mMoviesGrid, "Loading", Snackbar.LENGTH_SHORT).show();
    }

    private void showGrid() {
        isLoading = false;
        mMoviesGrid.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mLoadingBar.setVisibility(View.INVISIBLE);
    }

    private int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    // ----------------------------- AsyncTask -----------------------------
    private class FetchMoviesTask extends AsyncTask<Request, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingState();
            if(!NetworkUtils.isOnline(getBaseContext())) {
                // User has no internet connection
                this.cancel(true);
                Log.e(TAG, "Showing error state");
                // Something went wrong fetching the data
                showErrorState(getString(R.string.no_internet));
            }
        }

        @Override
        protected Movie[] doInBackground(Request... params) {
            if(params == null || params.length == 0){
                return null;
            }
            Request request = params[0];
            int type = request.getRequestType();
            int page = request.getPage();
            try {
                switch (type){
                    case RequestType.POPULAR:
                        Log.v(TAG, "Fetching page "+page+" of POPULAR movies...");
                        return MovieApiManager.fetchPopularMovies(page);
                    case RequestType.TOP_RATED:
                        Log.v(TAG, "Fetching page "+page+" of TOP_RATED movies...");
                        return MovieApiManager.fetchTopRatedMovies(page);
                }
                return null;
            } catch (Exception e) {
                Log.e(TAG, "Something went wrong fetching data!");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movieData) {
            if (movieData != null && movieData.length > 0) {
                if(mRequest.getPage() == 1) {
                    Log.v(TAG, "Fetch complete! SETTING data set");
                    mMoviesGridAdapter.setMovies(movieData);
                    showGrid();
                } else {
                    Log.v(TAG, "Fetch complete! ADDING to data set");
                    mMoviesGridAdapter.addMovies(movieData);
                    showGrid();
                }
            } else {
                Log.e(TAG, "Showing error state");
                // Something went wrong fetching the data
                showErrorState(getString(R.string.generic_error));
            }
        }
    }
    // ----------------------------------------------------------

}


