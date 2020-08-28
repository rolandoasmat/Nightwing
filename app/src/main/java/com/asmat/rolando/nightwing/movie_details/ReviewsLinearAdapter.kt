package com.asmat.rolando.nightwing.movie_details

import android.view.View
import android.widget.TextView
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.networking.models.ReviewsResponse
import com.asmat.rolando.nightwing.ui.common.BaseLinearAdapter
import kotlinx.android.synthetic.main.review_linear_item.view.*

private typealias Review = ReviewsResponse.Review
private typealias ReviewViewHolder = ReviewsLinearAdapter.ViewHolder

class ReviewsLinearAdapter : BaseLinearAdapter<Review, ReviewViewHolder>() {

    override val layoutForLinearItem: Int
        get() = R.layout.review_linear_item

    override fun createViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : BaseLinearAdapter<Review, ReviewViewHolder>.ViewHolder(view) {

        private val content: TextView = view.tv_review_content
        private val reviewer: TextView = view.tv_reviewer

        override fun bind(item: Review) {
            content.text = item.content
            reviewer.text = item.author
        }
    }
}