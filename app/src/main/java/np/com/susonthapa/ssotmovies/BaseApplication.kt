package np.com.susonthapa.ssotmovies

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import np.com.susonthapa.ssotmovies.di.ApplicationModule
import np.com.susonthapa.ssotmovies.di.DaggerApplicationComponent
import timber.log.Timber

/**
 * Created by suson on 7/13/20
 */

class BaseApplication : Application() {

    val appComponent = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        Timber.plant(Timber.DebugTree())
    }

}