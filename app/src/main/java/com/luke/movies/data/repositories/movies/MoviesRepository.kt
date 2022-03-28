package com.luke.movies.data.repositories.movies

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.luke.movies.base.MOVIES_PER_PAGE
import com.luke.movies.base.output.SimpleOutput
import com.luke.movies.data.api.ApiServices
import com.luke.movies.data.models.movieDetails.MovieDetails
import com.luke.movies.data.models.movies.Movie
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MoviesRepository @Inject constructor (private val apiService: ApiServices) {

    var output = SimpleOutput<MovieDetails, String>()
    private lateinit var moviePagedList: LiveData<PagedList<Movie>>
    private lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<Movie>> {
        moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)
        output = moviesDataSourceFactory.output

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(MOVIES_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()

        return moviePagedList
    }
}