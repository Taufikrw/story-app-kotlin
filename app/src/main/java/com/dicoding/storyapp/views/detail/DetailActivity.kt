package com.dicoding.storyapp.views.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(STORY_DATA, ListStoryItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(STORY_DATA)
        }
        binding.apply {
            tvName.text = story?.name
            tvCreate.text = story?.createdAt
            tvDesc.text = story?.description
        }
        Glide.with(this)
            .load(story?.photoUrl)
            .into(binding.ivPhotoUrl)
    }

    companion object {
        const val STORY_DATA = "story data"
    }
}