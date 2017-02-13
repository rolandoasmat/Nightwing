package com.asmat.rolando.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.models.Movie;
import com.asmat.rolando.popularmovies.models.MovieAdapterOnClickHandler;
import com.squareup.picasso.Picasso;


/**
 * Created by rolandoasmat on 2/9/17.
 */

public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.MoviesGridAdapterViewHolder> {

    /**
     * Data set of Movie objects
     */
    private Movie[] mMovies;

    /**
     * OnClick listener
     */
    private final MovieAdapterOnClickHandler mClickHandler;

    // ----------------------------- API -----------------------------
    /**
     * Default constructor
     */
    public MoviesGridAdapter(MovieAdapterOnClickHandler handler) {
        this.mClickHandler = handler;
    }
    /**
     * Set list of movies
     */
    public void setMovies(Movie[] movies) {
        mMovies  = movies;
        notifyDataSetChanged();
    }

    /**
     * Add any newly fetched movies from API
     */
    public void addMovies(Movie[] movies) {
        int firstArrayLength = mMovies.length;
        int secondArrayLength = movies.length;
        int newSize = firstArrayLength + secondArrayLength;
        Movie[] combined = new Movie[newSize];
        for(int i = 0; i < firstArrayLength; i++){
            combined[i] = mMovies[i];
        }
        for(int i = 0; i < secondArrayLength; i++){
            combined[firstArrayLength+i] = movies[i];
        }
        mMovies = combined;
        notifyItemRangeInserted(firstArrayLength,secondArrayLength);
    }
    // ----------------------------------------------------------

    // ----------------------------- Overrides -----------------------------
    /**
     * Called whenever a ViewHolder is needed. Happens when the RecyclerView is layed out
     * Enough are created to fill the screen.
     *
     * @param parent ViewGroup where ViewHolders will be contained in.
     * @param viewType Used if different types of ViewHolders, not the case here.
     *
     * @return ViewHolder
     */
    @Override
    public MoviesGridAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForGridItem = R.layout.movie_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForGridItem, parent, false);
        return new MoviesGridAdapterViewHolder(view);
    }

    /**
     * Called by RecyclerView to specify how to display data on the passed in position.
     *
     * @param holder ViewHolder to update with correct data, using given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MoviesGridAdapterViewHolder holder, int position) {
        Movie movie = mMovies[position];
        String posterURL = movie.getPosterURL();
        ImageView imageView = holder.mMoviePoster;
        Picasso.with(imageView.getContext()).load(posterURL).into(imageView);
    }

    @Override
    public int getItemCount() {
        if(mMovies == null){
            return 0;
        } else {
            return mMovies.length;
        }
    }

    // ----------------------------------------------------------


    // ----------------------------- ViewHolder -----------------------------
    /**
     * Cache of the children views for a forecast list item.
     */
    class MoviesGridAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mMoviePoster;

        /**
         * Creates cache of view holder
         *
         * @param itemView Layout of view holder
         */
        public MoviesGridAdapterViewHolder(View itemView) {
            super(itemView);
            mMoviePoster = (ImageView) itemView.findViewById(R.id.iv_movie_grid_item);
            itemView.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Movie movie = mMovies[position];
            mClickHandler.onClick(movie);
        }
    }
    // ----------------------------------------------------------
}
