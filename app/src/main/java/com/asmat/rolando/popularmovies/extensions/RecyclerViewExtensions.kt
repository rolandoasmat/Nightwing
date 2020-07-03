package com.asmat.rolando.popularmovies.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setNearBottomScrollListener(callback: ()-> Unit) {
    val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0) { // User is scrolling down
                (layoutManager as? LinearLayoutManager)?.let { layoutManager ->
                    val positionOfLastItem = layoutManager.itemCount - 1
                    val currentPositionOfLastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    if (currentPositionOfLastVisibleItem >= positionOfLastItem - 5) {
                        callback()
                    }
                }
            }
        }
    }
    addOnScrollListener(listener)
}