package com.luke.movies.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.luke.movies.base.MOVIES_PER_PAGE
import com.luke.movies.base.output.SimpleOutput
import com.luke.movies.base.output.SingleLiveEvent
import com.luke.movies.data.api.ApiServices
import com.luke.movies.data.models.movieDetails.MovieDetails
import com.luke.movies.data.models.movies.Movie
import com.luke.movies.data.repositories.movies.MoviesRepository
import io.mockk.*
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MoviesViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var moviesRepository: MoviesRepository

    private val observer: Observer<PagedList<Movie>> = mockk(relaxed = true)
    private val outputObserver: Observer<MovieDetails> = mockk(relaxed = true)

    private lateinit var viewModel: MoviesViewModel

    @Before
    fun setup()  {
        moviesRepository = mockk {
            every { output } returns expectedSimpleOutput

            every { fetchLiveMoviePagedList(any()) } returns
                    LivePagedListBuilder(
                        TestDataSourceFactory(),
                        PagedList.Config.Builder()
                            .setEnablePlaceholders(false)
                            .setPageSize(MOVIES_PER_PAGE)
                            .build()
                    ).build()
        }
        viewModel = MoviesViewModel(moviesRepository)
        viewModel.moviePagedList.observeForever(observer)
        viewModel.output.complete.observeForever(outputObserver)
    }

    @After
    fun tearDown() {
        viewModel.moviePagedList.removeObserver(observer)
    }

    @Test
    fun `call to paging source should be successful`() {
        verify { observer.onChanged(any()) }
    }

    @Test
    fun `complete output state should be successful`() {
        verify { outputObserver.onChanged(movieDetails) }
    }
}

class TestDataSourceFactory : DataSource.Factory<Int, Movie>() {
    override fun create(): DataSource<Int, Movie> {
        return MovieDataSource()
    }
}

class MovieDataSource : PageKeyedDataSource<Int, Movie>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {}

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {}

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {}
}

private val movieDetails = MovieDetails(
    true,
    "",
    false,
    10,
    emptyList(),
    "",
    1,
    "",
    "",
    "",
    "",
    8.5,
    "",
    "",
    500000,
    2,
    emptyList(),
    "",
    "",
    "",
    false,
    2.4,
    7
)

private val expectedSimpleOutput = SimpleOutput<MovieDetails, String>().apply {
     complete.apply {
         value = movieDetails
     }
}

