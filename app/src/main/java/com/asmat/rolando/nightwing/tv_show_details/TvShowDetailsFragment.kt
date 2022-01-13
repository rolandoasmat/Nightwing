package com.asmat.rolando.nightwing.tv_show_details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.asmat.rolando.nightwing.NightwingApplication
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.share.ShareData
import com.asmat.rolando.nightwing.ui.row_view.RowView
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.details_screen_user_actions.*
import kotlinx.android.synthetic.main.fragment_tv_show_details.*
import kotlinx.android.synthetic.main.fragment_tv_show_details.toolbar
import javax.inject.Inject

class TvShowDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    val viewModel: TvShowDetailsViewModel by viewModels { viewModelFactory }

    val args: TvShowDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tv_show_details, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as NightwingApplication).component().inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeViewModel()
        viewModel.load(args.tvShowIdArg)
        setupToolbar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        heartContainer?.setOnClickListener {
            viewModel.heartIconTapped()
        }
        shareContainer?.setOnClickListener {
            viewModel.shareIconTapped()
        }
    }

    private fun observeViewModel() {
        viewModel.uiModel.observe(viewLifecycleOwner) {
            createdByLabel?.text = it.createdBy
            firstAirDateLabel?.text = it.firstAirDateText
            lastAirDateLabel?.text = it.lastAirDate
            networksLabel?.text = it.networks
            overviewLabel?.text = it.overview
            numberOfEpisodesLabel?.text = it.numberOfEpisodes
            numberOfSeasonsLabel?.text = it.numberOfSeasons
            taglineLabel?.text = it.tagline
            statusLabel?.text = it.status
            it.backdropUrl?.let { url ->
                Picasso.get()
                    .load(url)
                    .into(tvShowBackdrop)
            }
            tvShowCollapsingToolbar?.title = it.name
        }
        viewModel.seasons.observe(viewLifecycleOwner) { items ->
            seasonsRowView?.configure(
                title = "Seasons",
                seeAllButtonEnabled = false,
                callback = object : RowView.Callback {
                    override fun onCardClicked(id: Int, view: View) {
                        val action = TvShowDetailsFragmentDirections
                            .actionTvShowDetailsToTvSeasonDetails(args.tvShowIdArg, id)
                        findNavController().navigate(action)
                    }
                },
                data = items
            )
        }
        viewModel.isSaved.observe(viewLifecycleOwner) {
            heartIcon?.isSelected = it == true
        }
        viewModel.share.observe(viewLifecycleOwner) {
            shareTvShow(it)
        }
    }

    private fun setupToolbar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        tvShowCollapsingToolbar?.setupWithNavController(
            toolbar,
            findNavController(),
            appBarConfiguration
        )
    }

    private fun shareTvShow(data: ShareData?) {
        data?.let {
            val mimeType = "text/plain"
            val intent = ShareCompat.IntentBuilder.from(requireActivity())
                .setChooserTitle(it.title)
                .setType(mimeType)
                .setText(it.message).intent
            startActivity(Intent.createChooser(intent, it.title))
        }
    }
}