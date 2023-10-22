package com.dicoding.storyapp.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.views.addStory.AddStoryActivity
import com.dicoding.storyapp.views.listStory.StoryViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<StoryViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getStory(intent.getStringExtra(USER_TOKEN)!!)
        viewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)
        viewModel.listStory.observe(this) {
            val adapter = StoryAdapter()
            adapter.submitList(it)
            binding.rvStory.adapter = adapter
        }

        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    companion object {
        const val USER_TOKEN = "user_token"
    }
}