package np.com.susonthapa.ssotmovies.persistance

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Created by suson on 7/13/20
 */

@Dao
abstract class MoviesDao {

    @Query("SELECT * FROM movies")
    abstract fun getAllMovies(): Flow<List<Movies>>

    @Delete
    abstract suspend fun removeMovie(movie: Movies)

    @Insert(onConflict = REPLACE)
    abstract suspend fun insertAll(movies: List<Movies>)

}

