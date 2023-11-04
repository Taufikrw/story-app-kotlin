package com.dicoding.storyapp.views.listStory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.remote.response.ListStoryItem

class StoryViewModel(private val repository: StoryRepository): ViewModel() {
    val getStories: LiveData<PagingData<ListStoryItem>> = repository.getStory().cachedIn(viewModelScope)

    fun getToken(): LiveData<String> = repository.getSession().asLiveData()

    suspend fun logout() {
        repository.destroyToken()
    }
}