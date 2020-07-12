package np.com.susonthapa.ssotmovies.persistance

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by suson on 7/12/20
 */

@Database(entities = [Movies::class], version = 1, exportSchema = false)
abstract class MovieDatabase  : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
}



