package com.asmat.rolando.popularmovies.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.Snackbar;
import android.util.Log;

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
    private MoviesGridAdapter mMoviesGridAdapter;
    private GridLayoutManager mMoviesGridLayoutManager;
    private Request mRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // RecyclerView reference
        mMoviesGrid = (RecyclerView) findViewById(R.id.movie_grid_rv);
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

    private void showErrorSnackBar(){
        Snackbar.make(mMoviesGrid, "No Internet Connection", Snackbar.LENGTH_LONG).show();
    }

    // ----------------------------- AsyncTask -----------------------------
    public class FetchMoviesTask extends AsyncTask<Request, Void, Movie[]> {

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
                } else {
                    Log.v(TAG, "Fetch complete! ADDING to data set");
                    mMoviesGridAdapter.addMovies(movieData);
                }
            } else {
                // Something went wrong fetching the data
                showErrorSnackBar();
            }
        }
    }
    // ----------------------------------------------------------

}


