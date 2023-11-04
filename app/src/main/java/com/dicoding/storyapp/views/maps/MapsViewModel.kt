package com.dicoding.storyapp.views.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.remote.response.StoryResponse

class MapsViewModel(private val repository: StoryRepository): ViewModel() {
    suspend fun getStoryWithLocation(): LiveData<Result<StoryResponse>> = repository.getStoryWithLocation()
}