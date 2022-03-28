package com.luke.movies.ui.movieDetails

import android.os.Bundle
import android.view.MenuItem
import com.luke.movies.R
import com.luke.movies.base.BaseActivity
import com.luke.movies.ui.movieDetails.MovieDetailFragment.Companion.REQUEST_MOVIE_ID
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsActivity : com.luke.movies.base.BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_movie_details)

        openMovieDetails()
    }

    private fun openMovieDetails() {
        val movieDetailFragment = MovieDetailFragment()
        val bundle = Bundle()
        bundle.putInt(REQUEST_MOVIE_ID, intent.getIntExtra(REQUEST_MOVIE_ID, 0))
        movieDetailFragment.arguments = bundle
        addFragment(R.id.content_movie_details, movieDetailFragment, true)
    }

    fun updateTitle(title: String) {
        changeTitle(title)
    }

    /**
     *
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}