package com.luke.movies.data.repositories.movies

import androidx.paging.PageKeyedDataSource
import com.luke.movies.base.FIRST_PAGE
import com.luke.movies.base.output.SimpleOutput
import com.luke.movies.data.api.ApiServices
import com.luke.movies.data.models.movieDetails.MovieDetails
import com.luke.movies.data.models.movies.Movie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class MovieDataSource @Inject constructor (
    private val apiService: ApiServices,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Movie>() {

    private var page = FIRST_PAGE

    var output = SimpleOutput<MovieDetails, String>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        loading(true)
        compositeDisposable.add(
            apiService.getMovies(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.movies, null, page + 1)
                        loading(true)
                    }, {
                        error(it.message)
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        loading(true)
        compositeDisposable.add(
            apiService.getMovies(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.totalPages >= params.key) {
                            callback.onResult(it.movies, params.key + 1)
                            loading(false)
                        } else {
                            output.endOfList.postValue(true)
                        }
                    }, {
                        error(it.message)
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
    }


    /**
     *
     */
    private fun loading(load: Boolean) {
        output.loading.postValue(load)
    }

    /**
     *
     */
    private fun error(error: String?) {
        error?.let {
            Timber.e("Error: $it")
            output.error.postValue(it)
        }
        loading(false)
    }
}