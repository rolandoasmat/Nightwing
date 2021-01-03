package com.asmat.rolando.nightwing.ui.row_view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.asmat.rolando.nightwing.R
import kotlinx.android.synthetic.main.row_view.view.*

class RowView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0): ConstraintLayout(context, attrs, defStyleAttr) {
    private var callback: Callback? = null

    init {
        inflate(context, R.layout.row_view, this)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        movieRowRecyclerView.layoutManager = layoutManager
        seeAllLabel?.setOnClickListener {
            callback?.onSeeAllClicked()
        }
    }

    fun <T: RecyclerView.ViewHolder> configure(title: String, adapter: RecyclerView.Adapter<T>, callback: Callback) {
        movieTitleLabel.text = title
        movieRowRecyclerView.adapter = adapter
        this.callback = callback
    }

    interface Callback {
        fun onSeeAllClicked()
    }
}