package np.com.susonthapa.ssotmovies.di

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import np.com.susonthapa.ssotmovies.ui.MoviesViewModel
import np.com.susonthapa.ssotmovies.repository.MoviesRepository
import javax.inject.Provider

/**
 * Created by suson on 7/13/20
 */

@Module
class ViewModelModule {

    @Provides
    fun provideViewModelFactory(providerMap: MutableMap<Class<out ViewModel>, Provider<ViewModel>>): ViewModelFactory {
        return ViewModelFactory(providerMap)
    }

    @Provides
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    fun provideMoviesViewModel(moviesRepo: MoviesRepository): ViewModel {
        return MoviesViewModel(moviesRepo)
    }
}