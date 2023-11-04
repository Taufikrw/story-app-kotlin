package com.dicoding.storyapp.views.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.remote.response.RegisterResponse

class RegisterViewModel(private val repository: StoryRepository): ViewModel() {

    suspend fun postRegister(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = repository.register(name, email, password)
}