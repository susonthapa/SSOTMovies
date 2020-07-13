package np.com.susonthapa.ssotmovies.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import np.com.susonthapa.ssotmovies.databinding.MoviesItemLayoutBinding
import np.com.susonthapa.ssotmovies.persistance.Movies

/**
 * Created by suson on 7/13/20
 */

class MoviesAdapter (private val removeListener: (Movies) -> Unit) : ListAdapter<Movies, MoviesViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding = MoviesItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(getItem(position), removeListener)
    }

}

class MoviesViewHolder (private val binding: MoviesItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movies, removeListener: (Movies) -> Unit) {
        binding.apply {
            movieImage.setImageURI(movie.image)
            movieTitle.text = movie.title
            movieYear.text = movie.year
            movieType.text = movie.type

            movieRemove.setOnClickListener {
                removeListener(movie)
            }
        }
    }

}

class MovieDiffCallback : DiffUtil.ItemCallback<Movies>() {
    override fun areItemsTheSame(oldItem: Movies, newItem: Movies): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movies, newItem: Movies): Boolean {
        return oldItem == newItem
    }

}

