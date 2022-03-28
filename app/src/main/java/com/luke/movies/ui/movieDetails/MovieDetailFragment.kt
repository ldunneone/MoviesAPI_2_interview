package com.luke.movies.ui.movieDetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.luke.movies.BuildConfig
import com.luke.movies.data.models.movieDetails.MovieDetails
import com.luke.movies.databinding.FragmentMovieDetailsBinding
import com.luke.movies.helpers.extensions.*
import com.luke.movies.viewModels.MovieDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 */
@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    @Inject lateinit var viewModel: MovieDetailsViewModel
    private var movieId: Int = 0
    private var _binding: FragmentMovieDetailsBinding ?=null
    private val binding: FragmentMovieDetailsBinding get()=_binding!!

    companion object {
        const val REQUEST_MOVIE_ID = "MovieId"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMovieDetailsBinding.inflate(layoutInflater)

        arguments?.let { it.getInt(REQUEST_MOVIE_ID).let { id -> movieId = id } }
        observeData()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     *
     */
    private fun observeData() {
        viewModel.output.loading.observe(viewLifecycleOwner, {
            binding.progressCircularDetails.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.output.complete.observe(viewLifecycleOwner, {
            binding.progressCircularDetails.gone()
            displayData(it)
        })

        viewModel.output.error.observe(viewLifecycleOwner, {
            binding.error.visible()
            binding.error.text = it
        })
        viewModel.getMovieDetails(movieId)
    }

    private fun displayData(movieDetails: MovieDetails) {
        binding.title.text = movieDetails.title
        binding.releaseDate.text = movieDetails.releaseDate
        binding.description.text = movieDetails.overview
        binding.runtime.text = String.format("%s min", movieDetails.runtime)
        binding.image.loadImage("${BuildConfig.POSTER_BASE_URL}${movieDetails.posterPath}")
        activity?.let { (it as MovieDetailsActivity).updateTitle(movieDetails.title) }
        binding.genre.text = movieDetails.genres.genreToCommaSeparatedString()
        binding.language.text = movieDetails.languages.languageToCommaSeparatedString()

        binding.bookNow.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("${BuildConfig.MOVIE_WEB_URL}${movieId}")
            startActivity(openURL)
        }
    }

}