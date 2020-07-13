package np.com.susonthapa.ssotmovies.di

import dagger.Component
import np.com.susonthapa.ssotmovies.ui.MainActivity
import javax.inject.Singleton

/**
 * Created by suson on 7/12/20
 */

@Singleton
@Component(modules = [NetworkModule::class, RoomModule::class, ViewModelModule::class, ApplicationModule::class])
interface ApplicationComponent {
    fun inject(activity: MainActivity)
}
