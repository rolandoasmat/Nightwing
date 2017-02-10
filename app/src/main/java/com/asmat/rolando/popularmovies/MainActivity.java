package com.asmat.rolando.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

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
    }

}
