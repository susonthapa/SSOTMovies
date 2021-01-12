package np.com.susonthapa.ssotmovies.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import np.com.susonthapa.ssotmovies.persistance.Movies
import np.com.susonthapa.ssotmovies.repository.Lce
import np.com.susonthapa.ssotmovies.repository.MoviesRepository
import javax.inject.Inject

/**
 * Created by suson on 7/13/20
 */

class MoviesViewModel @Inject constructor(
    private val moviesRepo: MoviesRepository,
) : ViewModel() {

    private val _movies = MutableLiveData<List<Movies>>()
    val movies: LiveData<List<Movies>> = _movies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var getMoviesJob: Job? = null

    @FlowPreview
    fun getAllMovies() {
        getMoviesJob?.cancel()
        getMoviesJob = viewModelScope.launch {
            moviesRepo.getMovies()
                .collect {
                    when (it) {
                        is Lce.Loading -> {
                            _isLoading.value = true
                        }

                        is Lce.Content -> {
                            _isLoading.value = false
                            _movies.value = it.packet
                        }

                        is Lce.Error -> {
                            _isLoading.value = false
                        }
                    }
                }
        }
    }

    fun removeMovie(movie: Movies) {
        viewModelScope.launch {
            moviesRepo.removeMovie(movie)
        }
    }

}