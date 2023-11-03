package com.dicoding.storyapp.views.listStory

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.Injection
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.UserPreferences
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.data.remote.response.StoryResponse
import com.dicoding.storyapp.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel(private val repository: StoryRepository): ViewModel() {
    val getStories: LiveData<PagingData<ListStoryItem>> = repository.getStory().cachedIn(viewModelScope)

    fun getToken(): LiveData<String> = repository.getSession().asLiveData()

    suspend fun logout() {
        repository.destroyToken()
    }
}