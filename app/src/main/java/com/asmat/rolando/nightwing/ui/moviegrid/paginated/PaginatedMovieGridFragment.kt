package com.asmat.rolando.nightwing.ui.moviegrid.paginated

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.asmat.rolando.nightwing.ui.moviegrid.BaseMovieGridFragment
import kotlinx.android.synthetic.main.fragment_movie_grid.*

abstract class PaginatedMovieGridFragment: BaseMovieGridFragment() {

    abstract override val viewModel: PaginatedMovieGridViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (moviesRecyclerView?.layoutManager as? GridLayoutManager)?.let { layoutManager ->
            moviesRecyclerView?.addOnScrollListener(createScrollListener(layoutManager))
        }
    }

    private fun createScrollListener(layoutManager: GridLayoutManager): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { // User is scrolling down
                    val positionOfLastItem = layoutManager.itemCount - 1
                    val currentPositionOfLastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    if (currentPositionOfLastVisibleItem >= positionOfLastItem - 5) {
                        viewModel.loadMore()
                    }
                }
            }
        }
    }
}