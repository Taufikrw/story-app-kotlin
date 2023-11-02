package com.dicoding.storyapp.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.UserPreferences
import com.dicoding.storyapp.data.dataStore
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.views.addStory.AddStoryActivity
import com.dicoding.storyapp.views.listStory.StoryViewModel
import com.dicoding.storyapp.views.login.LoginActivity
import com.dicoding.storyapp.views.login.LoginViewModel
import com.dicoding.storyapp.views.maps.MapsActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<StoryViewModel>()
    private lateinit var binding: ActivityMainBinding
    private var token: String? = null

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

        viewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        loginViewModel.getToken().observe(this) {
            viewModel.getStory(it)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        viewModel.listStory.observe(this) {
            if (it.isNullOrEmpty()) {
                binding.tvIsEmpty.visibility = View.VISIBLE
            } else {
                binding.tvIsEmpty.visibility = View.GONE
            }
            val adapter = StoryAdapter()
            adapter.submitList(it)
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
        viewModel.getStory(token.toString())
    }
}