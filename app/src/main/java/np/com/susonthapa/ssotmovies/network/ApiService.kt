package np.com.susonthapa.ssotmovies.network

import np.com.susonthapa.ssotmovies.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by suson on 7/12/20
 */
interface ApiService {

    @GET("/")
    suspend fun getMovies(
        @Query("s") name: String = "blade",
        @Query("apiKey") apiKey: String = BuildConfig.OMDB_API_KEY
    ): SearchResponse

}