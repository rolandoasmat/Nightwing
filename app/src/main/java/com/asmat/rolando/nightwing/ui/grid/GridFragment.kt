package com.asmat.rolando.nightwing.ui.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.extensions.setNearBottomScrollListener
import com.asmat.rolando.nightwing.utilities.ViewUtils
import kotlinx.android.synthetic.main.fragment_grid.*

abstract class GridFragment: Fragment(), GridAdapter.Callback {

    private var adapter: GridAdapter? = null

    abstract val viewModel: GridViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeViewModel()
        viewModel.load()
    }

    override fun cardClicked(id: Int) {
        onItemClicked(id)
    }

    abstract fun onItemClicked(id: Int)

    private fun setupRecyclerView() {
        adapter = GridAdapter(this)
        gridRecyclerView?.adapter = adapter
        val numOfColumns = ViewUtils.calculateNumberOfColumns(requireContext())
        gridRecyclerView?.layoutManager = GridLayoutManager(requireContext(), numOfColumns)
        gridRecyclerView?.setNearBottomScrollListener {
            viewModel.loadMore()
        }
    }

    private fun observeViewModel() {
        viewModel.items.observe(viewLifecycleOwner) {
            adapter?.setData(it)
        }
    }
}