package np.com.susonthapa.ssotmovies.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.FlowPreview
import np.com.susonthapa.ssotmovies.BaseApplication
import np.com.susonthapa.ssotmovies.databinding.ActivityMainBinding
import np.com.susonthapa.ssotmovies.di.ViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MoviesViewModel

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: MoviesAdapter

    @FlowPreview
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

        binding.swipeToRefresh.setOnRefreshListener {
            fetchMovies()
        }
        setupObservers()
        fetchMovies()
    }

    private fun setupObservers() {
        viewModel.movies.observe(this) {
            if (it.isEmpty()) {
                binding.moviesRecycler.showEmptyView()
            } else {
                binding.moviesRecycler.hideAllViews()
            }
            adapter.submitList(it)
            binding.swipeToRefresh.isRefreshing = false
        }

        viewModel.isLoading.observe(this) {
            if (adapter.itemCount == 0 && !binding.swipeToRefresh.isRefreshing) {
                binding.moviesRecycler.showLoadingView()
            } else {
                binding.swipeToRefresh.isRefreshing = it
            }
        }
    }

    @FlowPreview
    private fun fetchMovies() {
        viewModel.getAllMovies()
    }

}