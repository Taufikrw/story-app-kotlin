package com.dicoding.storyapp.views.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.data.remote.response.StoryResponse
import com.dicoding.storyapp.data.remote.retrofit.ApiConfig
import com.dicoding.storyapp.views.listStory.StoryViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val repository: StoryRepository): ViewModel() {
    suspend fun getStoryWithLocation(): LiveData<Result<StoryResponse>> = repository.getStoryWithLocation()
}