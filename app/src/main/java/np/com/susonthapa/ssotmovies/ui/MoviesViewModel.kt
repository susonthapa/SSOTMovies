package np.com.susonthapa.ssotmovies.ui

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import np.com.susonthapa.ssotmovies.persistance.Movies
import np.com.susonthapa.ssotmovies.repository.Lce
import np.com.susonthapa.ssotmovies.repository.MoviesRepository
import javax.inject.Inject

/**
 * Created by suson on 7/13/20
 */

class MoviesViewModel @Inject constructor(
    private val moviesRepo: MoviesRepository
) : ViewModel() {


    fun getAllMovies(): Observable<Lce<List<Movies>>> {
        return moviesRepo.getMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun removeMovie(movie: Movies) {
        moviesRepo.removeMovie(movie)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

}