package np.com.susonthapa.ssotmovies.di

import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import np.com.susonthapa.ssotmovies.network.ApiService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Created by suson on 7/12/20
 */

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .baseUrl("http://www.omdbapi.com")
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
