package com.asmat.rolando.nightwing.ui.row_view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.ui.common.BaseLinearAdapter
import kotlinx.android.synthetic.main.row_view.view.*

class RowView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0): ConstraintLayout(context, attrs, defStyleAttr), BaseLinearAdapter.Callback<RowViewItemUiModel> {

    private var callback: Callback? = null
    private var rowAdapter = MoviesLinearAdapter(this)

    init {
        inflate(context, R.layout.row_view, this)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        movieRowRecyclerView.layoutManager = layoutManager
        seeAllLabel?.setOnClickListener {
            callback?.onSeeAllClicked()
        }
        movieRowRecyclerView.adapter = rowAdapter
    }

    fun configure(title: String? = null, seeAllButtonEnabled: Boolean = true, callback: Callback) {
        movieTitleLabel.text = title
        seeAllLabel.isGone = !seeAllButtonEnabled
        this.callback = callback
    }

    fun setTitle(title: String) {
        movieTitleLabel.text = title
    }

    fun setData(data: List<RowViewItemUiModel>) {
        rowAdapter.data = data
    }

    interface Callback {
        fun onSeeAllClicked() { }
        fun onCardClicked(id: Int)
    }

    override fun cardClicked(item: RowViewItemUiModel) {
        this.callback?.onCardClicked(item.id)
    }
}