package com.luke.movies.ui.movies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.luke.movies.R
import com.luke.movies.databinding.FragmentMovieDetailsBinding
import com.luke.movies.databinding.FragmentMoviesBinding
import com.luke.movies.helpers.extensions.visible
import com.luke.movies.helpers.network.NetworkUtil
import com.luke.movies.ui.movieDetails.MovieDetailFragment.Companion.REQUEST_MOVIE_ID
import com.luke.movies.ui.movieDetails.MovieDetailsActivity
import com.luke.movies.viewModels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 */
@AndroidEntryPoint
class MoviesFragment : Fragment() {

    @Inject lateinit var viewModel: MoviesViewModel
    private var movieAdapter = MoviesPagedAdapter()
    private var _binding: FragmentMovieDetailsBinding?=null
    private val binding: FragmentMovieDetailsBinding get()=_binding!!
    private var _binding2: FragmentMoviesBinding?=null
    private val binding2: FragmentMoviesBinding get()= _binding2!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMovieDetailsBinding.inflate(layoutInflater)
        _binding2 = FragmentMoviesBinding.inflate(layoutInflater)

        observeData()

        return binding2.root
    }

    /**
     *
     */
    private fun observeData() {
        setUpRecyclerView()

        viewModel.moviePagedList.observe(viewLifecycleOwner, {
            movieAdapter.submitList(it)
        })

        viewModel.output.loading.observe(viewLifecycleOwner, {
            binding.progressCircularDetails.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.output.error.observe(viewLifecycleOwner, {
            binding.error.text = it
            it?.let { binding.error.visible() }
        })

        if (!NetworkUtil.isInternetAvailable)
            Toast.makeText(requireContext(), getString(R.string.error_internet), Toast.LENGTH_SHORT)
                .show()
    }

    private fun setUpRecyclerView() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        // ??
        binding2.moviesRecyclerView.layoutManager = gridLayoutManager
        binding2.moviesRecyclerView.setHasFixedSize(true)
        binding2.moviesRecyclerView.adapter = movieAdapter
        movieAdapter.onMovieClicked = {
            it?.let { movie ->
                openMovieDetails(movie.id)
            }
        }
    }

    private fun openMovieDetails(movieId: Int) {
        activity?.let {
            it.startActivity(
                Intent(it, MovieDetailsActivity::class.java)
                    .putExtra(REQUEST_MOVIE_ID, movieId)
            )
        }
    }
}