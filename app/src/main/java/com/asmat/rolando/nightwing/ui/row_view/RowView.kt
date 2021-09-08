package com.asmat.rolando.nightwing.ui.row_view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.extensions.gone
import com.asmat.rolando.nightwing.extensions.visible
import com.asmat.rolando.nightwing.ui.common.BaseLinearAdapter
import kotlinx.android.synthetic.main.row_view.view.*

class RowView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0): ConstraintLayout(context, attrs, defStyleAttr), BaseLinearAdapter.Callback<RowViewItemUiModel> {

    private var callback: Callback? = null
    private var rowAdapter = RowViewLinearAdapter(this)

    init {
        inflate(context, R.layout.row_view, this)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        movieRowRecyclerView.layoutManager = layoutManager
        seeAllLabel?.setOnClickListener {
            callback?.onSeeAllClicked()
        }
        retryButton?.setOnClickListener {
            callback?.onRetry()
        }
        movieRowRecyclerView.adapter = rowAdapter
    }

    fun configure(
        title: String? = null,
        seeAllButtonEnabled: Boolean = true,
        callback: Callback? = null)
    {
        movieTitleLabel.text = title
        seeAllLabel.isGone = !seeAllButtonEnabled
        this.callback = callback
    }

    fun setData(data: List<RowViewItemUiModel>) {
        if (data.isEmpty()) {
            this.gone()
        } else {
            this.visible()
            rowAdapter.data = data
        }
    }

    fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            loadingBar?.visible()
        } else {
            loadingBar?.gone()
        }
    }

    fun setRetry(showRetry: Boolean) {
        if (showRetry) {
            retryButton?.visible()
        } else {
            retryButton?.gone()
        }
    }

    interface Callback {
        fun onSeeAllClicked() { }
        fun onCardClicked(id: Int)
        fun onRetry() { }
    }

    override fun cardClicked(item: RowViewItemUiModel) {
        item.id?.let { id ->
            this.callback?.onCardClicked(id)
        }
    }
}