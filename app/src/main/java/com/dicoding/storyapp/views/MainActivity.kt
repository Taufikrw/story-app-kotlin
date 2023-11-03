package com.dicoding.storyapp.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.SharedData
import com.dicoding.storyapp.data.UserPreferences
import com.dicoding.storyapp.data.dataStore
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.views.addStory.AddStoryActivity
import com.dicoding.storyapp.views.listStory.StoryViewModel
import com.dicoding.storyapp.views.login.LoginActivity
import com.dicoding.storyapp.views.login.LoginViewModel
import com.dicoding.storyapp.views.maps.MapsActivity

class MainActivity : AppCompatActivity() {
    private val viewModel: StoryViewModel by viewModels {
        com.dicoding.storyapp.views.listStory.ViewModelFactory(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val pref = UserPreferences.getInstance(application.dataStore)
        val loginViewModel = ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu1 -> {
                    startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        loginViewModel.getToken().observe(this) {
            SharedData.token = it
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        showLoading(true)
        viewModel.getStories().observe(this) {
            val adapter = StoryAdapter()
            adapter.submitData(lifecycle, it)
            showLoading(false)
            binding.rvStory.adapter = adapter
        }

        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        binding.fabLogout.setOnClickListener {
            loginViewModel.destroyToken()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getStories()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}