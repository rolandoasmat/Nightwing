package com.asmat.rolando.popularmovies.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asmat.rolando.popularmovies.adapters.MoviesGridAdapter;
import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.managers.MovieApiManager;
import com.asmat.rolando.popularmovies.models.Movie;
import com.asmat.rolando.popularmovies.models.Request;
import com.asmat.rolando.popularmovies.models.RequestTypeEnum;
import com.asmat.rolando.popularmovies.utilities.NetworkUtils;


public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mMoviesGrid;
    private TextView mErrorMessageTextView;
    private ProgressBar mLoadingBar;


    private MoviesGridAdapter mMoviesGridAdapter;
    private GridLayoutManager mMoviesGridLayoutManager;
    private Request mRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // UI references
        mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message);
        mLoadingBar = (ProgressBar) findViewById(R.id.pb_loading_bar);
        mMoviesGrid = (RecyclerView) findViewById(R.id.rv_movie_grid);
        mMoviesGrid.setHasFixedSize(false);
        // LayoutManager
        mMoviesGridLayoutManager = new GridLayoutManager(this, 2);
        mMoviesGrid.setLayoutManager(mMoviesGridLayoutManager);
        // Set adapter
        mMoviesGridAdapter = new MoviesGridAdapter();
        mMoviesGrid.setAdapter(mMoviesGridAdapter);
        // Load inital data
        loadData();
        // Setup scroll listener
        setScrollListener();
    }

    private void loadData(){
        // Create request
        mRequest = new Request(RequestTypeEnum.POPULAR, 1);
        // Execute
        new FetchMoviesTask().execute(this.mRequest);
    }

    private void setScrollListener() {
        mMoviesGrid.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0) { // User is scrolling down
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

    private void showErrorState() {
        mMoviesGrid.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mLoadingBar.setVisibility(View.INVISIBLE);
        Snackbar.make(mMoviesGrid, "No Internet Connection", Snackbar.LENGTH_LONG).show();
    }

    private void showLoadingState() {
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mLoadingBar.setVisibility(View.VISIBLE);
        Snackbar.make(mMoviesGrid, "Loading", Snackbar.LENGTH_SHORT).show();
    }

    private void showGrid() {
        mMoviesGrid.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mLoadingBar.setVisibility(View.INVISIBLE);
    }

    // ----------------------------- AsyncTask -----------------------------
    public class FetchMoviesTask extends AsyncTask<Request, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingState();
        }

        @Override
        protected Movie[] doInBackground(Request... params) {
            if(params == null || params.length == 0){
                return null;
            }
            Request request = params[0];
            RequestTypeEnum type = request.getRequestType();
            int page = request.getPage();
            try {
                switch (type){
                    case POPULAR:
                        Log.v(TAG, "Fetching page "+page+" of POPULAR movies...");
                        return MovieApiManager.fetchPopularMovies(page);
                    case TOP_RATED:
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
                showErrorState();
            }
        }
    }
    // ----------------------------------------------------------

}


