package com.asmat.rolando.nightwing.popular_people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.asmat.rolando.nightwing.NightwingApplication
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_popular_people.*
import javax.inject.Inject

class PopularPeopleGridFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val viewModel: PopularPeopleViewModel by viewModels { viewModelFactory }

    private var adapter: PopularPeopleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as NightwingApplication).component().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_popular_people, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.data.observe(viewLifecycleOwner) {
            adapter?.setData(it)
        }
    }

    private fun setupRecyclerView() {
        adapter = PopularPeopleAdapter()
        val layoutManager = GridLayoutManager(requireContext(), 2)
        popularPeopleRecyclerView?.adapter = adapter
        popularPeopleRecyclerView?.layoutManager = layoutManager
    }
}