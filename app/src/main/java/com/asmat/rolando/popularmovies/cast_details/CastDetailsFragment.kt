package com.asmat.rolando.popularmovies.cast_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.asmat.rolando.popularmovies.MovieNightApplication
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.extensions.gone
import com.asmat.rolando.popularmovies.extensions.visible
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_cast_details.*
import javax.inject.Inject

class CastDetailsFragment: Fragment(), PersonMovieCreditsFragment.Listener {

    companion object {
        const val CAST_ID_ARG = "castIDArg"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    val viewModel: CastDetailsViewModel by viewModels{ viewModelFactory }

    private val tabName = listOf("Info", "Movie credits")

    private val personID: Int
        get() = requireArguments().getInt(CAST_ID_ARG)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as? MovieNightApplication)?.component()?.inject(this)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cast_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init(personID)
        setup()
    }


    //region setup

    private fun setup() {
        setupToolbar()
        observeViewModel()
    }

    private fun setupToolbar() {
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun observeViewModel() {
        viewModel.name.observe(viewLifecycleOwner, Observer {
            collapsingToolbar.title = it
        })
        viewModel.uiModel.observe(viewLifecycleOwner, Observer { uiModel ->
            uiModel?.let { setupViewPager(uiModel) }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            updateLoading(loading == true)
        })
    }

    //endregion

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            android.R.id.home -> {
//                super.onBackPressed()
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return true
//    }

    private fun setupViewPager(uiModel: CastDetailsUiModel) {
        val adapter = CastDetailsPagerAdapter(uiModel, this)
        castDetailsViewPager?.adapter = adapter
        TabLayoutMediator(tabLayout, castDetailsViewPager) { tab, position ->
            tab.text = tabName[position]
        }.attach()
    }

    //region Callbacks
    override fun setBackdropURL(url: String?) {
        url?.let {
            Picasso.get().load(it).into(toolbarImage)
        }
    }
    //endregion

    private fun updateLoading(loading: Boolean) {
        if (loading) {
            loadingView?.visible()
        } else {
            loadingView?.gone()
        }
    }
}
