package com.asmat.rolando.popularmovies.ui.adapters;

import android.view.View;
import android.widget.TextView;
import com.asmat.rolando.popularmovies.R;

public class ReviewsLinearAdapter extends BaseLinearAdapter<Review, ReviewsLinearAdapter.ViewHolder>{

    public ReviewsLinearAdapter() {
        super();
    }

    @Override
    int getLayoutForLinearItem() {
        return R.layout.review_linear_item;
    }

    @Override
    ViewHolder createViewHolder(View view) {
        return new ReviewsLinearAdapter.ViewHolder(view);
    }

    class ViewHolder extends BaseLinearAdapter.ViewHolder {
        private final TextView content;
        private final TextView reviewer;

        ViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.tv_review_content);
            reviewer = itemView.findViewById(R.id.tv_reviewer);
        }

        @Override
        void bind(Object item) {
            if (item instanceof Review) {
                Review review = (Review) item;
                content.setText(review.getContent());
                reviewer.setText(review.getAuthor());
            }
        }
    }
}