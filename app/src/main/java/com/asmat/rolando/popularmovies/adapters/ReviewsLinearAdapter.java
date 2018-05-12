package com.asmat.rolando.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.models.Review;

public class ReviewsLinearAdapter extends RecyclerView.Adapter<ReviewsLinearAdapter.ViewHolder> {

    private Review[] mReviews;

    public void setReviews(Review[] reviews) {
        this.mReviews = reviews;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForLinearItem = R.layout.review_linear_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForLinearItem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = mReviews[position];
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        if(mReviews == null) {
            return 0;
        } else {
            return mReviews.length;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mReviewContent;
        private final TextView mReviewer;

        ViewHolder(View itemView) {
            super(itemView);
            mReviewContent = itemView.findViewById(R.id.tv_review_content);
            mReviewer = itemView.findViewById(R.id.tv_reviewer);
        }

        void bind(Review review) {
            mReviewContent.setText(review.getContent());
            mReviewer.setText(review.getAuthor());
        }
    }
}