package com.asmat.rolando.nightwing.ui.movie_row

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.movie_details.MoviesLinearAdapter
import kotlinx.android.synthetic.main.movie_row_view.view.*

class MovieRowView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0): ConstraintLayout(context, attrs, defStyleAttr) {
    private var callback: Callback? = null

    init {
        inflate(context, R.layout.movie_row_view, this)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        movieRowRecyclerView.layoutManager = layoutManager
        seeAllLabel?.setOnClickListener {
            callback?.onSeeAllClicked()
        }
    }

    fun configure(title: String, adapter: MoviesLinearAdapter, callback: Callback) {
        movieTitleLabel.text = title
        movieRowRecyclerView.adapter = adapter
        this.callback = callback
    }

    interface Callback {
        fun onSeeAllClicked()
    }
}