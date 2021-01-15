package com.asmat.rolando.nightwing.saved_tv_shows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.asmat.rolando.nightwing.NightwingApplication
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.saved.SavedFragmentDirections
import com.asmat.rolando.nightwing.saved_movies.SavedMoviesViewModel
import com.asmat.rolando.nightwing.ui.grid.GridAdapter
import com.asmat.rolando.nightwing.utilities.ViewUtils
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_saved_movies.*
import kotlinx.android.synthetic.main.fragment_saved_tv_shows.*
import javax.inject.Inject

class SavedTvShowsFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    val viewModel: SavedTvShowsViewModel by viewModels { viewModelFactory }

    private var adapter: GridAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as? NightwingApplication)?.component()?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_saved_tv_shows, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.tvShows.observe(viewLifecycleOwner) {
            adapter?.setData(it)
        }
    }

    private fun setup() {
        adapter = GridAdapter(object : GridAdapter.Callback{
            override fun cardClicked(id: Int) {
                val action = SavedFragmentDirections.actionGlobalActionToTvShowDetailsScreen(id)
                findNavController().navigate(action)
            }
        })
        savedTvShowsRecyclerView?.adapter = adapter
        val numOfColumns = ViewUtils.calculateNumberOfColumns(requireContext())
        savedTvShowsRecyclerView?.layoutManager = GridLayoutManager(requireContext(), numOfColumns)
    }

}