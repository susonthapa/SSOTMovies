package np.com.susonthapa.ssotmovies.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import io.reactivex.disposables.Disposable
import np.com.susonthapa.ssotmovies.R
import np.com.susonthapa.ssotmovies.databinding.ActivityMainBinding
import np.com.susonthapa.ssotmovies.di.ViewModelFactory
import np.com.susonthapa.ssotmovies.repository.Lce
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MoviesViewModel

    private lateinit var binding: ActivityMainBinding

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, viewModelFactory)[MoviesViewModel::class.java]

        disposable = viewModel.getAllMovies()
            .subscribe({
                when (it) {
                    is Lce.Loading -> {

                    }

                    is Lce.Content -> {

                    }

                    is Lce.Error -> {

                    }
                }
            }, {
                it.printStackTrace()
                Timber.w("movies stream error")
            })

    }

}