package com.asmat.rolando.nightwing.cast_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.asmat.rolando.nightwing.NightwingApplication
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.extensions.gone
import com.asmat.rolando.nightwing.extensions.visible
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_cast_details.*
import kotlinx.android.synthetic.main.fragment_cast_details.collapsingToolbar
import kotlinx.android.synthetic.main.fragment_cast_details.toolbar
import javax.inject.Inject

class CastDetailsFragment: Fragment(), CastMovieCreditsFragment.Listener, MovieCreditsAdapter.ItemCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    val viewModel: CastDetailsViewModel by viewModels{ viewModelFactory }

    private val tabName = listOf("Info", "Movie credits")

    val args by navArgs<CastDetailsFragmentArgs>()
    private val personID: Int
        get() { return args.castIdArg }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as? NightwingApplication)?.component()?.inject(this)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cast_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init(personID)
        setup()
    }

    override fun onMovieBannerClicked(movieID: Int) {
        val action = CastDetailsFragmentDirections.actionCastDetailsScreenToMovieDetailsScreen(movieID)
        findNavController().navigate(action)
    }

    //region setup

    private fun setup() {
        setupToolbar()
        observeViewModel()
    }

    private fun setupToolbar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        collapsingToolbar?.setupWithNavController(toolbar, findNavController(), appBarConfiguration)
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
