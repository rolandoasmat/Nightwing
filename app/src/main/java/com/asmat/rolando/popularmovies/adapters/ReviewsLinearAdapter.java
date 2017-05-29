package com.asmat.rolando.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.models.Review;

/**
 * Created by rolandoasmat on 5/20/17.
 */

public class ReviewsLinearAdapter extends RecyclerView.Adapter<ReviewsViewHolder> {

    private Review[] mReviews;

    public void setReviews(Review[] reviews) {
        this.mReviews = reviews;
    }


    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForLinearItem = R.layout.review_linear_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForLinearItem, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        Review review = mReviews[position];
        holder.mReviewer.setText(review.getAuthor());
        holder.mReviewContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if(mReviews == null) {
            return 0;
        } else {
            return mReviews.length;
        }
    }
}

class ReviewsViewHolder extends RecyclerView.ViewHolder {
    public final TextView mReviewContent;
    public final TextView mReviewer;

    public ReviewsViewHolder(View itemView) {
        super(itemView);
        mReviewContent = (TextView) itemView.findViewById(R.id.tv_review_content);
        mReviewer = (TextView) itemView.findViewById(R.id.tv_reviewer);
    }

}
