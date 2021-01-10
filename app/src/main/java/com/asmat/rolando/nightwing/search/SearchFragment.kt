package com.asmat.rolando.nightwing.search

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.asmat.rolando.nightwing.NightwingApplication
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.extensions.setNearBottomScrollListener
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchFragment: Fragment(), SearchAdapter.Callbacks {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    val viewModel: SearchViewModel by viewModels { viewModelFactory }

    private var adapter: SearchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as? NightwingApplication)?.component()?.inject(this)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        searchView?.setIconifiedByDefault(false)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setSearchTerm(query ?: "")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchTerm(newText ?: "")
                return false
            }
        })
        setupRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.configure_search -> {
                showConfigureSearchDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showConfigureSearchDialog() {
        val currentlySelectedIndex: Int? = viewModel.searchMode.value?.let { mode ->
            when (mode) {
                SearchViewModel.SearchMode.MOVIES -> 0
                SearchViewModel.SearchMode.PEOPLE -> 1
            }
        }
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }

        val items: Array<CharSequence> = arrayOf("Movies", "People")
        builder?.setSingleChoiceItems(items, currentlySelectedIndex ?: 0) { p0, p1 ->
            when (p1) {
                0 -> viewModel.setSearchMode(SearchViewModel.SearchMode.MOVIES)
                1 -> viewModel.setSearchMode(SearchViewModel.SearchMode.PEOPLE)
            }
        }?.setTitle("Search settings")

        val dialog: AlertDialog? = builder?.create()
        dialog?.show()
    }

    private fun setupRecyclerView() {
        adapter = SearchAdapter(this)
        searchResultsRecyclerView?.adapter = adapter
        searchResultsRecyclerView?.layoutManager = GridLayoutManager(requireContext(), 2)
        searchResultsRecyclerView?.setNearBottomScrollListener {
            viewModel.loadMore()
        }
    }

    private fun observeViewModel() {
        viewModel.results.observe(viewLifecycleOwner, Observer {
            updateResults(it)
        })
        viewModel.searchHint.observe(viewLifecycleOwner, Observer {
            searchView?.queryHint = it
        })
    }

    private fun updateResults(items: List<SearchViewModel.SearchResultUiModel>) {
        adapter?.setData(items)
    }

    override fun openMovieDetails(id: Int) {
        val action = SearchFragmentDirections.actionGlobalActionToMovieDetailsScreen(id)
        findNavController().navigate(action)
    }

    override fun openActorDetails(id: Int) {
        val action = SearchFragmentDirections.actionGlobalActionToCastDetailsScreen(id)
        findNavController().navigate(action)
    }
}