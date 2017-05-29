package com.asmat.rolando.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.models.TrailerAdapterOnClickHandler;
import com.asmat.rolando.popularmovies.models.Video;
import com.squareup.picasso.Picasso;

/**
 * Created by rolandoasmat on 5/20/17.
 */

public class TrailersLinearAdapter
        extends RecyclerView.Adapter<TrailersLinearAdapter.TrailersLinearAdapterViewHolder> {

    /**
     * Data set of Video objects
     */
    private Video[] mTrailers;

    /**
     * OnClick listener
     */
    private final TrailerAdapterOnClickHandler mClickHandler;

    // ----------------------------- API -----------------------------
    /**
     * Default constructor
     */
    public TrailersLinearAdapter(TrailerAdapterOnClickHandler handler) { this.mClickHandler = handler; }

    /**
     * Set list of trailers
     */
    public void setTrailers(Video[] trailers) {
        mTrailers = trailers;
        notifyDataSetChanged();
    }

    public Video[] getTrailers() {
        return mTrailers;
    }

    // ----------------------------- Overrides -----------------------------
    @Override
    public TrailersLinearAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForLinearItem = R.layout.trailer_linear_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForLinearItem, parent, false);
        return new TrailersLinearAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersLinearAdapterViewHolder holder, int position) {
        Video video = mTrailers[position];
        String caption = video.getName();
        holder.mCaption.setText(caption);
        String thumbnail = video.youtubeThumbnail();
        ImageView imageView = holder.mTrailerThumbnail;
        Picasso.with(imageView.getContext())
                .load(thumbnail)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        if(mTrailers == null){
            return 0;
        } else {
            return mTrailers.length;
        }
    }

    // ----------------------------- ViewHolder -----------------------------
    class TrailersLinearAdapterViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mTrailerThumbnail;
        public final TextView mCaption;

        public TrailersLinearAdapterViewHolder(View view) {
            super(view);
            mTrailerThumbnail = (ImageView) view.findViewById(R.id.iv_trailer_thumbnail);
            mCaption = (TextView) view.findViewById(R.id.tv_trailer_caption);
            mTrailerThumbnail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Video trailer = mTrailers[position];
            mClickHandler.onClick(trailer);
        }
    }

}
