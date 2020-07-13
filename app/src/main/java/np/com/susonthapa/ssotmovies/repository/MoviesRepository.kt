package np.com.susonthapa.ssotmovies.repository

import io.reactivex.Completable
import io.reactivex.Observable
import np.com.susonthapa.ssotmovies.network.ApiService
import np.com.susonthapa.ssotmovies.network.SearchResponse
import np.com.susonthapa.ssotmovies.persistance.Movies
import np.com.susonthapa.ssotmovies.persistance.MoviesDao
import javax.inject.Inject

/**
 * Created by suson on 7/13/20
 */

class MoviesRepository @Inject constructor(
    private val moviesDao: MoviesDao,
    private val api: ApiService
) {

    fun removeMovie(movie: Movies): Completable {
        return moviesDao.removeMovie(movie)
    }

    fun getMovies(): Observable<Lce<List<Movies>>> {
        return Observable.merge(getMoviesFromDB(), getMoviesFromServer())
    }

    /**
     * The effect that we are trying to achieve is to prevent the view from displaying emptyView when
     * there is no data in db and instead should display a loader. But for other subsequent emissions
     * from db, we want it to display the emptyView if the data is empty.
     */
    private fun getMoviesFromDB(): Observable<Lce<List<Movies>>> {
        return Observable.concat(
            moviesDao.getAllMovies()
                .take(1)
                .filter {
                    it.isNotEmpty()
                }
                .map { Lce.Content(it) },
            moviesDao.getAllMovies()
                .skip(1)
                .map { Lce.Content(it) })
    }

    private fun getMoviesFromServer(): Observable<Lce<List<Movies>>> {
        return api.getMovies()
            .map<Lce<List<Movies>>> {
                if (it.response == "True") {
                    val movies = convertSearchResponse(it.search)
                    moviesDao.insertAll(movies)
                    Lce.Content(movies)
                } else {
                    Lce.Error(Throwable(it.error))
                }
            }
            .onErrorReturn {
                it.printStackTrace()
                Lce.Error(it)
            }
            .filter {
                it is Lce.Error || (it as Lce.Content).packet.isEmpty()
            }
            .startWith(Lce.Loading())
    }

    private fun convertSearchResponse(response: List<SearchResponse.Search>): List<Movies> {
        return response.map {
            Movies(it.id, it.title, it.year, it.type, it.image)
        }
    }

}