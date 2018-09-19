package com.asmat.rolando.popularmovies.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.models.AdapterOnClickHandler;
import com.asmat.rolando.popularmovies.models.Video;
import com.squareup.picasso.Picasso;

public class TrailersLinearAdapter extends BaseLinearAdapter<Video,TrailersLinearAdapter.ViewHolder> {

    public TrailersLinearAdapter(AdapterOnClickHandler<Video> clickHandler) {
        super(clickHandler);
    }

    @Override
    int getLayoutForLinearItem() {
        return R.layout.trailer_linear_item;
    }

    @Override
    ViewHolder createViewHolder(View view) {
        return new TrailersLinearAdapter.ViewHolder(view);
    }

    @Override
    void bindViewHolder(Video item, ViewHolder holder) {
        String caption = item.getName();
        holder.caption.setText(caption);
        String thumbnail = item.getYouTubeThumbnailURL();
        ImageView imageView = holder.thumbnail;
        Picasso.with(imageView.getContext())
                .load(thumbnail)
                .into(imageView);
    }

    class ViewHolder extends BaseLinearAdapter.ViewHolder {
        final ImageView thumbnail;
        final TextView caption;

        ViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.iv_trailer_thumbnail);
            caption = view.findViewById(R.id.tv_trailer_caption);
            thumbnail.setOnClickListener(this);
        }
    }
}