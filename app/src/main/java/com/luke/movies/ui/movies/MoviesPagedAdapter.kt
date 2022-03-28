package com.luke.movies.ui.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.luke.movies.BuildConfig
import com.luke.movies.R
import com.luke.movies.data.models.movies.Movie
import com.luke.movies.databinding.ItemMovieBinding
import com.luke.movies.helpers.extensions.loadImage
import kotlinx.android.synthetic.main.item_movie.view.*

class MoviesPagedAdapter : PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    var onMovieClicked: ((Movie?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MovieItemViewHolder(ItemMovieBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MovieItemViewHolder).bind(getItem(position))
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            (oldItem.id == newItem.id)

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            (oldItem == newItem)
    }


    inner class MovieItemViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie?) {
            binding.title
            binding.title.text = movie?.title
            binding.releaseDate.text = movie?.releaseDate
            val voteAverage: Double? = movie?.voteAverage
            binding.circularProgressBar.progress = voteAverage?.times(10)?.toFloat()!!
            binding.percentage.text = String.format("%d%s", voteAverage.times(10).toInt(), "%")
            binding.image.loadImage("${BuildConfig.POSTER_BASE_URL}${movie.posterPath}")
            binding.root.setOnClickListener {
                onMovieClicked?.invoke(movie)
            }
        }
    }
}