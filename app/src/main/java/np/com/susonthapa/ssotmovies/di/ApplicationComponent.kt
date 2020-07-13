package np.com.susonthapa.ssotmovies.di

import np.com.susonthapa.ssotmovies.ui.MainActivity

/**
 * Created by suson on 7/12/20
 */

interface ApplicationComponent {
    fun inject(activity: MainActivity)
}
