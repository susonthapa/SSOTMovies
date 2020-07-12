package np.com.susonthapa.ssotmovies.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import np.com.susonthapa.ssotmovies.persistance.MovieDatabase
import np.com.susonthapa.ssotmovies.persistance.MoviesDao
import javax.inject.Singleton

/**
 * Created by suson on 7/12/20
 */

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideMoviesDatabase(context: Context): MovieDatabase {
        return Room.databaseBuilder(context, MovieDatabase::class.java, "movies.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMoviesDao(db: MovieDatabase): MoviesDao {
        return db.moviesDao()
    }

}