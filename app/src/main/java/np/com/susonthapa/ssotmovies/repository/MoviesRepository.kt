package np.com.susonthapa.ssotmovies.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import np.com.susonthapa.ssotmovies.network.ApiService
import np.com.susonthapa.ssotmovies.network.SearchResponse
import np.com.susonthapa.ssotmovies.persistance.Movies
import np.com.susonthapa.ssotmovies.persistance.MoviesDao
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by suson on 7/13/20
 */

class MoviesRepository @Inject constructor(
    private val moviesDao: MoviesDao,
    private val api: ApiService,
) {

    suspend fun removeMovie(movie: Movies) {
        withContext(Dispatchers.IO) {
            moviesDao.removeMovie(movie)
        }
    }

    @FlowPreview
    fun getMovies(): Flow<Lce<List<Movies>>> {
        return getMoviesFromDB()
    }

    @FlowPreview
    fun getMoviesFromDB(): Flow<Lce<List<Movies>>> {
        return moviesDao.getAllMovies()
            .take(1)
            .flatMapConcat {
                if (it.isEmpty()) {
                    Timber.d("DB is empty, requesting network")
                    getMoviesFromServer()
                } else {
                    Timber.d("Data in DB, emitting DB data then requesting network")
                    flowOf(Lce.Content(it) as Lce<List<Movies>>)
                        .onCompletion {
                            emitAll(getMoviesFromServer())
                        }
                }
            }
            .onCompletion {
                emitAll(moviesDao.getAllMovies()
                    .map {
                        Lce.Content(it)
                    }
                )
            }
    }

    /**
     * The effect that we are trying to achieve is to prevent the view from displaying emptyView when
     * there is no data in db and instead should display a loader. But for other subsequent emissions
     * from db, we want it to display the emptyView if the data is empty.
     */
//    private fun getMoviesFromDB(): Observable<Lce<List<Movies>>> {
//        return moviesDao
//            .getAllMoviesObs()
//            .publish { o ->
//                Observable.concatEager(
//                    arrayListOf(
//                        o.take(1)
//                            .flatMap {
//                                if (it.isEmpty()) {
//                                    Timber.d("DB is empty, requesting network")
//                                    getMoviesFromServer()
//                                } else {
//                                    Timber.d("Data in DB, emitting DB data then requesting network")
//                                    // we want to first emit the DB content and then request the network
//                                    Observable.concat(
//                                        Observable.just(Lce.Content(it)),
//                                        getMoviesFromServer()
//                                    )
//                                }
//                            },
//
//                        o.skip(1)
//                            .map { Lce.Content(it) })
//                    )
//            }
//    }

    private suspend fun getMoviesFromServer(): Flow<Lce<List<Movies>>> {
        val response = api.getMovies()
        return flowOf(response)
            .map {
                if (it.response == "True") {
                    val movies = convertSearchResponse(it.search)
                    Timber.d("saving to DB: ${it.search.size}")
                    moviesDao.insertAll(movies)
                    Lce.Content(movies)
                } else {
                    Lce.Error(Throwable(it.error))
                }
            }
            .catch { e ->
                e.printStackTrace()
                emit(Lce.Error(e))
            }
            .filter { it is Lce.Error || (it as Lce.Content).packet.isEmpty() }
            .onStart { emit(Lce.Loading()) }
    }

//        return if (response.response == "True") {
//            val movies = convertSearchResponse(response.search)
//            Timber.d("saving to DB: ${response.search.size}")
//            moviesDao.insertAll(movies)
//            Lce.Content(movies)
//        } else {
//            Lce.Error(Throwable(response.error))
//        }
//        return api.getMovies()
//            .map<Lce<List<Movies>>> {
//                if (it.response == "True") {
//                    val movies = convertSearchResponse(it.search)
//                    Timber.d("saving to DB: ${it.search.size}")
//                    moviesDao.insertAll(movies)
//                    Lce.Content(movies)
//                } else {
//                    Lce.Error(Throwable(it.error))
//                }
//            }
//            .onErrorReturn {
//                it.printStackTrace()
//                Lce.Error(it)
//            }
//            .filter {
//                it is Lce.Error || (it as Lce.Content).packet.isEmpty()
//            }
//            .startWith(Lce.Loading())
}

private fun convertSearchResponse(response: List<SearchResponse.Search>): List<Movies> {
    return response.map {
        Movies(it.id, it.title, it.year, it.type, it.image)
    }
}
