package com.asmat.rolando.popularmovies.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.asmat.rolando.popularmovies.adapters.MoviesGridAdapter;
import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.data.PopularMoviesContract;
import com.asmat.rolando.popularmovies.managers.MovieApiManager;
import com.asmat.rolando.popularmovies.models.Movie;
import com.asmat.rolando.popularmovies.models.MovieAdapterOnClickHandler;
import com.asmat.rolando.popularmovies.models.Request;
import com.asmat.rolando.popularmovies.models.RequestType;
import com.asmat.rolando.popularmovies.utilities.NetworkUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor>{

    @BindView(R.id.tv_error_message) TextView mErrorMessageTextView;
    @BindView(R.id.pb_loading_bar) ProgressBar mLoadingBar;
    @BindView(R.id.rv_movie_grid) RecyclerView mMoviesGrid;
    private MoviesGridAdapter mMoviesGridAdapter;
    private GridLayoutManager mMoviesGridLayoutManager;
    private static String TAG = MainActivity.class.getSimpleName();

    // Screen state
    private Request mRequest; // Request to execute
    private Movie[] mPopularMovies;
    private Movie[] mTopRatedMovies;
    private boolean isLoading;

    final String POPULAR_MOVIES_PARCEL_KEY = "popular_movies";
    final String TOP_RATED_MOVIES_PARCEL_KEY = "top_rated_movies";
    final int LOADER_ID = 9324;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupRecyclerView();

        if(savedInstanceState != null) {
            mRequest = savedInstanceState.getParcelable(Request.PARCEL_KEY);
            mPopularMovies = (Movie[]) savedInstanceState.getParcelableArray(POPULAR_MOVIES_PARCEL_KEY);
            mTopRatedMovies = (Movie[]) savedInstanceState.getParcelableArray(TOP_RATED_MOVIES_PARCEL_KEY);
            switch(mRequest.getRequestType()) {
                case RequestType.POPULAR:
                    mMoviesGridAdapter.setMovies(mPopularMovies);
                    updateActionBarTitle(R.string.most_popular);
                    break;
                case RequestType.TOP_RATED:
                    mMoviesGridAdapter.setMovies(mTopRatedMovies);
                    updateActionBarTitle(R.string.top_rated);
                    break;
                case RequestType.FAVORITES:
                    showFavorites();
                    break;
            }
        } else {
            mRequest = new Request(RequestType.POPULAR, 1, 0, 0);
            mPopularMovies = new Movie[0];
            mTopRatedMovies = new Movie[0];
            updateActionBarTitle(R.string.most_popular);
            setScrollListener();
            executeRequest();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mRequest.getRequestType() == RequestType.FAVORITES) {
            showFavorites();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("request", mRequest);
        outState.putParcelableArray("popular_movies", mPopularMovies);
        outState.putParcelableArray("top_rated_movies", mTopRatedMovies);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
                showFavorites();
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

    private void setupRecyclerView() {
        mMoviesGrid.setHasFixedSize(true);
        mMoviesGridLayoutManager = new GridLayoutManager(this, calculateNoOfColumns());
        mMoviesGrid.setLayoutManager(mMoviesGridLayoutManager);
        mMoviesGridAdapter = new MoviesGridAdapter(this);
        mMoviesGrid.setAdapter(mMoviesGridAdapter);
    }

    private void sortByTopRated() {
        setScrollListener();
        if(mRequest.getRequestType() != RequestType.TOP_RATED) {
            // Only sort if not already sorted by top rated
            updateActionBarTitle(R.string.top_rated);
            mRequest.setRequestType(RequestType.TOP_RATED);
            if(mTopRatedMovies.length == 0) {
                mRequest.setTopRatedMoviesPage(0);
                executeRequest();
            } else {
                mMoviesGridAdapter.setMovies(mTopRatedMovies);
            }
        }
    }

    private void sortByMostPopular() {
        setScrollListener();
        if(mRequest.getRequestType() != RequestType.POPULAR) {
            // Only sort if not already sorted by top rated
            updateActionBarTitle(R.string.most_popular);
            mRequest.setRequestType(RequestType.POPULAR);
            if(mPopularMovies.length == 0) {
                mRequest.setPopularMoviesPage(0);
                executeRequest();
            } else {
                mMoviesGridAdapter.setMovies(mPopularMovies);
            }
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

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if(dy > 0 && !isLoading) { // User is scrolling down
                int positionOfLastItem = mMoviesGridLayoutManager.getItemCount()-1;
                int currentPositionOfLastVisibleItem = mMoviesGridLayoutManager.findLastVisibleItemPosition();
                if(positionOfLastItem == currentPositionOfLastVisibleItem){
                    lastItemReached();
                }
            }
        }
    };

    private void setScrollListener() {
        mMoviesGrid.clearOnScrollListeners();
        mMoviesGrid.addOnScrollListener(mOnScrollListener);
    }

    private void removeScrollListener() {
        mMoviesGrid.clearOnScrollListeners();
    }

    private void lastItemReached() {
        Log.v(TAG, "Reached last item of list.");
        new FetchMoviesTask().execute(mRequest);
    }

    private void showErrorState(String message) {
        mMoviesGrid.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setText(message);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mLoadingBar.setVisibility(View.INVISIBLE);
        Snackbar.make(findViewById(R.id.fl_main_activity), message, Snackbar.LENGTH_LONG).show();
    }

    private void showLoadingState() {
        isLoading = true;
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mLoadingBar.setVisibility(View.VISIBLE);
    }

    private void showGrid() {
        isLoading = false;
        mMoviesGrid.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mLoadingBar.setVisibility(View.INVISIBLE);
    }

    private int calculateNoOfColumns() {
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return 3;
        } else {
            return 2;
        }
    }

    private void showFavorites() {
        removeScrollListener();
        mRequest.setRequestType(RequestType.FAVORITES);
        updateActionBarTitle(R.string.favorites);
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = PopularMoviesContract.FavoritesEntry.CONTENT_URI;
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ArrayList<Movie> favoriteMovies = new ArrayList<>();
        while(data.moveToNext()) {
            favoriteMovies.add(Movie.getMovieFromCursorEntry(data));
        }
        data.close();
        Movie[] movies = favoriteMovies.toArray(new Movie[0]);
        mMoviesGridAdapter.setMovies(movies);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

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
                int requestType = mRequest.getRequestType();
                int page = mRequest.getPage();
                Toast.makeText(getBaseContext(), "Movies fetched.", Toast.LENGTH_SHORT).show();
                if(page == 1) {
                    // This is the first fetch, set
                    Log.v(TAG, "Fetch complete! SETTING data set");
                    mMoviesGridAdapter.setMovies(movieData);
                    if(requestType == RequestType.POPULAR) {
                        mPopularMovies = movieData;
                        mRequest.setPopularMoviesPage(1);
                    } else if (mRequest.getRequestType() == RequestType.TOP_RATED){
                        mTopRatedMovies = movieData;
                        mRequest.setTopRatedMoviesPage(1);
                    }
                } else {
                    // These are new fetches, append
                    Log.v(TAG, "Fetch complete! ADDING to data set");
                    Movie[] combined = mMoviesGridAdapter.addMovies(movieData);
                    if(requestType == RequestType.POPULAR) {
                        mPopularMovies = combined;
                        mRequest.nextPopularMoviesPage();
                    } else if (mRequest.getRequestType() == RequestType.TOP_RATED){
                        mTopRatedMovies = combined;
                        mRequest.nextTopRatedMoviesPage();
                    }
                }
                mRequest.nextPage();
                showGrid();
            } else {
                Log.e(TAG, "Showing error state");
                // Something went wrong fetching the data
                showErrorState(getString(R.string.generic_error));
            }
        }
    }
    // ----------------------------------------------------------

}


