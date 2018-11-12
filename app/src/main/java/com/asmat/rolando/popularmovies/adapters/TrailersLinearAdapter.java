package com.asmat.rolando.popularmovies.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.models.AdapterOnClickHandler;
import com.asmat.rolando.popularmovies.networking.models.Video;
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

    class ViewHolder extends BaseLinearAdapter.ViewHolder {
        final ImageView thumbnail;
        final TextView caption;

        ViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.iv_trailer_thumbnail);
            caption = view.findViewById(R.id.tv_trailer_caption);
            thumbnail.setOnClickListener(this);
        }

        @Override
        void bind(Object item) {
            if (item instanceof Video) {
                Video video = (Video) item;
                caption.setText(video.getName());
                String url = video.getYouTubeThumbnailURL();
                Picasso.with(thumbnail.getContext())
                        .load(url)
                        .into(thumbnail);
            }
        }
    }
}