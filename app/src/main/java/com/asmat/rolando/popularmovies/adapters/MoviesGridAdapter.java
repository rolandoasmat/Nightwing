package com.asmat.rolando.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.database.Movie;
import com.asmat.rolando.popularmovies.models.MovieAdapterOnClickHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rolandoasmat on 2/9/17.
 */

public class MoviesGridAdapter
        extends RecyclerView.Adapter<MoviesGridAdapter.MoviesGridAdapterViewHolder> {

    private ArrayList<Movie> mMovies;
    private final MovieAdapterOnClickHandler mClickHandler;

    /**
     * ----------------------------- API -----------------------------
     */
    public MoviesGridAdapter(MovieAdapterOnClickHandler handler) {
        this.mClickHandler = handler;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.mMovies  = movies;
        notifyDataSetChanged();
    }

    public void addMovies(ArrayList<Movie> movies) {
        int indexOfFirstNewItem = this.mMovies.size();
        mMovies.addAll(movies);
        notifyItemRangeInserted(indexOfFirstNewItem,indexOfFirstNewItem+movies.size());
    }

    /**
     * ----------------------------- Overrides -----------------------------
     */
    @Override
    public MoviesGridAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForGridItem = R.layout.movie_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForGridItem, parent, false);
        return new MoviesGridAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesGridAdapterViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        String posterURL = movie.getPosterURL();
        ImageView imageView = holder.mMoviePoster;
        Picasso.with(imageView.getContext())
                .load(posterURL)
                .resize(342, 485)
                .into(imageView);
        // TODO could we use the position to know whether we're close to the end and we need to load more data?
    }

    @Override
    public int getItemCount() {
        if(mMovies == null){
            return 0;
        } else {
            return mMovies.size();
        }
    }

    /**
     * ----------------------------- View Holder -----------------------------
     */
    class MoviesGridAdapterViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mMoviePoster;

        public MoviesGridAdapterViewHolder(View itemView) {
            super(itemView);
            mMoviePoster = (ImageView) itemView.findViewById(R.id.iv_movie_grid_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Movie movie = mMovies.get(position);
            mClickHandler.onClick(movie);
        }
    }

}

