package com.asmat.rolando.popularmovies.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.Snackbar;

import com.asmat.rolando.popularmovies.adapters.MoviesGridAdapter;
import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.managers.MovieApiManager;
import com.asmat.rolando.popularmovies.models.Movie;
import com.asmat.rolando.popularmovies.models.Request;
import com.asmat.rolando.popularmovies.models.RequestTypeEnum;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mMoviesGrid;
    private MoviesGridAdapter mMoviesGridAdapter;
    private GridLayoutManager mMoviesGridLayoutManager;

    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get reference to layout elements
        mMoviesGrid = (RecyclerView) findViewById(R.id.movie_grid_rv);
        // Grid will not be of fixed size
        mMoviesGrid.setHasFixedSize(false);
        // Set layout manager: grid layout manager with 2 columns
        mMoviesGridLayoutManager = new GridLayoutManager(this, 2);
        mMoviesGrid.setLayoutManager(mMoviesGridLayoutManager);
        // Set adapter
        mMoviesGridAdapter = new MoviesGridAdapter();
        mMoviesGrid.setAdapter(mMoviesGridAdapter);
        // Load date
        loadData();
        // Setup scroll listener
        setScrollListener();
    }

    private void loadData(){
        // Create request
        this.request = new Request(RequestTypeEnum.POPULAR, 1);
        // Execute
        new FetchMoviesTask().execute(this.request);
    }

    private void setScrollListener() {
        mMoviesGrid.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0) {
                    // User is scrolling down
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
        this.request.nextPage();
        // Execute
        new FetchMoviesTask().execute(this.request);
    }

    private void showErrorSnackBar(){

        Snackbar snackbar = Snackbar
                .make(mMoviesGrid, "No Internet Connection", Snackbar.LENGTH_LONG);
        snackbar.show();
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
                        return MovieApiManager.fetchPopularMovies(page);
                    case TOP_RATED:
                        return MovieApiManager.fetchTopRatedMovies(page);
                }
                return new Movie[0];
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movieData) {
            if (movieData != null) {
                if(request.getPage() == 1) {
                    mMoviesGridAdapter.setMovies(movieData);
                } else {
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


