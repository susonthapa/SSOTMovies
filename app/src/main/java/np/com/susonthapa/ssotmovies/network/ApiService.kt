package np.com.susonthapa.ssotmovies.network

import io.reactivex.Observable
import np.com.susonthapa.ssotmovies.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by suson on 7/12/20
 */
interface ApiService {

    @GET("/")
    fun getMovies(
        @Query("s") name: String = "blade",
        @Query("apiKey") apiKey: String = BuildConfig.OMDB_API_KEY
    ): Observable<SearchResponse>

}