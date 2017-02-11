package com.asmat.rolando.popularmovies.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.asmat.rolando.popularmovies.adapters.MoviesGridAdapter;
import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.managers.MovieApiManager;
import com.asmat.rolando.popularmovies.models.Movie;
import com.asmat.rolando.popularmovies.models.Request;
import com.asmat.rolando.popularmovies.models.RequestTypeEnum;

import static android.R.attr.type;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mMoviesGrid;
    private MoviesGridAdapter mMoviesGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get reference to layout elements
        mMoviesGrid = (RecyclerView) findViewById(R.id.movie_grid_rv);
        // Grid will not be of fixed size
        mMoviesGrid.setHasFixedSize(false);
        // Set layout manager: grid layout manager with 2 columns
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMoviesGrid.setLayoutManager(layoutManager);
        // Set adapter
        mMoviesGridAdapter = new MoviesGridAdapter();
        mMoviesGrid.setAdapter(mMoviesGridAdapter);
        // Load date
        loadData();
    }

    public void loadData(){
        // Create request
        Request request = new Request(RequestTypeEnum.POPULAR, 1);
        // Execute
        new FetchMoviesTask().execute(request);
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
                mMoviesGridAdapter.setMovies(movieData);
            }
        }
    }
    // ----------------------------------------------------------

}


