package com.dicoding.storyapp.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.SharedData
import com.dicoding.storyapp.data.UserPreferences
import com.dicoding.storyapp.data.dataStore
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.views.adapter.LoadingStateAdapter
import com.dicoding.storyapp.views.adapter.StoryAdapter
import com.dicoding.storyapp.views.addStory.AddStoryActivity
import com.dicoding.storyapp.views.listStory.StoryViewModel
import com.dicoding.storyapp.views.login.LoginActivity
import com.dicoding.storyapp.views.login.LoginViewModel
import com.dicoding.storyapp.views.maps.MapsActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel: StoryViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = StoryAdapter()

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu1 -> {
                    startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        showLoading(true)
        viewModel.getToken().observe(this) {
            lifecycleScope.launch {
                viewModel.getStories.observe(this@MainActivity) {
                    adapter.submitData(lifecycle, it)
                    showLoading(false)
                }
            }
        }

        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            this.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
        }

        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        binding.fabLogout.setOnClickListener {
            lifecycleScope.launch {
                viewModel.logout()
            }
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getStories
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}