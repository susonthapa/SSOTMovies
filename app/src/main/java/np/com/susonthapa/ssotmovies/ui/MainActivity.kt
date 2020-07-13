package np.com.susonthapa.ssotmovies.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import io.reactivex.disposables.Disposable
import np.com.susonthapa.ssotmovies.BaseApplication
import np.com.susonthapa.ssotmovies.R
import np.com.susonthapa.ssotmovies.databinding.ActivityMainBinding
import np.com.susonthapa.ssotmovies.di.ViewModelFactory
import np.com.susonthapa.ssotmovies.persistance.Movies
import np.com.susonthapa.ssotmovies.repository.Lce
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MoviesViewModel

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: MoviesAdapter

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as BaseApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, viewModelFactory)[MoviesViewModel::class.java]

        adapter = MoviesAdapter {
            viewModel.removeMovie(it)
        }

        binding.moviesRecycler.apply {
            recyclerView.adapter = adapter
            setOnRetryClickListener {
                fetchMovies()
            }
        }

        fetchMovies()

    }

    private fun fetchMovies() {
        // remove any previous subscription
        disposable?.dispose()
        disposable = viewModel.getAllMovies()
            .subscribe({
                when (it) {
                    is Lce.Loading -> {
                        // only show loading if the list is empty
                        if (adapter.itemCount == 0) {
                            binding.moviesRecycler.showLoadingView()
                        }
                    }

                    is Lce.Content -> {
                        // check for empty list, it's better to check in view model
                        if (it.packet.isEmpty()) {
                            binding.moviesRecycler.showEmptyView()
                        } else {
                            binding.moviesRecycler.hideAllViews()
                        }
                        adapter.submitList(it.packet)
                    }

                    is Lce.Error -> {
                        // only show error if the list is empty
                        if (adapter.itemCount == 0) {
                            binding.moviesRecycler.showErrorView(it.throwable?.message)
                        }
                    }
                }
            }, {
                it.printStackTrace()
                Timber.w("movies stream error")
            })
    }

}