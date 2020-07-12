package np.com.susonthapa.ssotmovies.persistance

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * Created by suson on 7/13/20
 */

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies")
    fun getAllMovies(): Observable<List<Movies>>


    @Delete
    fun removeMovie(movie: Movies): Completable

    @Insert(onConflict = REPLACE)
    fun insertAll(movies: List<Movies>)

}

