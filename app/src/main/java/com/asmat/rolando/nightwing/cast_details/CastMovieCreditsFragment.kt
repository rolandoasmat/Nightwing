package com.asmat.rolando.nightwing.cast_details

import androidx.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.asmat.rolando.nightwing.BuildConfig
import com.asmat.rolando.nightwing.NightwingApplication
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.utilities.ViewUtils
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_cast_movie_credits.*
import javax.inject.Inject

private const val ARG_PERSON_ID = "ARG_PERSON_ID"

/**
 * Uses the Person ID to retrieve a person's movie credits.
 */
class CastMovieCreditsFragment: Fragment(), MovieCreditsAdapter.ItemCallback {

    companion object {
        @JvmStatic
        fun newInstance(personID: Int) =
                CastMovieCreditsFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_PERSON_ID, personID)
                    }
                }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: PersonMovieCreditsViewModel by viewModels { viewModelFactory }

    private var listener: Listener? = null

    private val adapter by lazy { MovieCreditsAdapter(this) }

    private val personID: Int? by lazy { arguments?.getInt(ARG_PERSON_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as NightwingApplication).component().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cast_movie_credits, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        (parentFragment as? Listener)?.let {
            listener = it
        } ?: run {
            if (BuildConfig.DEBUG) {
                throw RuntimeException(context.toString() + " must implement Listener")
            } else {
                // Fail silently on prod
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun setup() {
        val numOfColumns = ViewUtils.calculateNumberOfColumns(requireActivity())
        movieCreditsRecyclerView?.layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, numOfColumns)
        movieCreditsRecyclerView?.adapter = adapter
        personID?. let { id -> viewModel.init(id) }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.uiModel.observe(viewLifecycleOwner, Observer { uiModel ->
            uiModel?.let { render(it) }
        })
    }

    private fun render(uiModel: PersonMovieCreditsUiModel) {
        adapter.updateData(uiModel.movies)
        listener?.setBackdropURL(uiModel.backdropURL)
    }

    interface Listener {
        fun setBackdropURL(url: String?)
    }

    override fun onMovieBannerClicked(movieID: Int) {
        (parentFragment as? MovieCreditsAdapter.ItemCallback)?.onMovieBannerClicked(movieID)
    }

}
