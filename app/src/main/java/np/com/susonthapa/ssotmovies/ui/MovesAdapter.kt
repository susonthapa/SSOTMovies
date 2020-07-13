package np.com.susonthapa.ssotmovies.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import np.com.susonthapa.ssotmovies.databinding.MoviesItemLayoutBinding
import np.com.susonthapa.ssotmovies.persistance.Movies

/**
 * Created by suson on 7/13/20
 */

class MovesAdapter (private val items: List<Movies>, private val removeListener: (Movies) -> Unit) : RecyclerView.Adapter<MoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding = MoviesItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(items[position], removeListener)
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

