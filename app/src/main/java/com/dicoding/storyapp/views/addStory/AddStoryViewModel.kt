package com.dicoding.storyapp.views.addStory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.remote.response.FileUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: StoryRepository): ViewModel() {
    suspend fun postStory(
        file: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<FileUploadResponse>> = repository.postStory(file, description)
}