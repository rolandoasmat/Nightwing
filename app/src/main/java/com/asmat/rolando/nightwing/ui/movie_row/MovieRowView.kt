package com.asmat.rolando.nightwing.ui.movie_row

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.movie_details.MovieCardUIModel
import com.asmat.rolando.nightwing.movie_details.MoviesLinearAdapter
import com.asmat.rolando.nightwing.ui.common.BaseLinearAdapter
import kotlinx.android.synthetic.main.movie_row_view.view.*

class MovieRowView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0): ConstraintLayout(context, attrs, defStyleAttr), BaseLinearAdapter.Callback<MovieCardUIModel> {
    private var callback: Callback? = null

    init {
        inflate(context, R.layout.movie_row_view, this)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        movieRowRecyclerView.layoutManager = layoutManager
    }

    fun setTitle(title: String) {
        movieTitleLabel.text = title
    }

    fun setAdapter(adapter: MoviesLinearAdapter) {
        movieRowRecyclerView.adapter = adapter
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    override fun cardClicked(item: MovieCardUIModel) {
        callback?.onCardClicked(item)
    }

    interface Callback {
        fun onCardClicked(item: MovieCardUIModel)
    }

}