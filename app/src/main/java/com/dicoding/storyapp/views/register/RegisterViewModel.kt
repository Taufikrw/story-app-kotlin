package com.dicoding.storyapp.views.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.remote.response.ErrorResponse
import com.dicoding.storyapp.data.remote.response.RegisterResponse
import com.dicoding.storyapp.data.remote.retrofit.ApiConfig
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class RegisterViewModel(private val repository: StoryRepository): ViewModel() {

    suspend fun postRegister(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = repository.register(name, email, password)
}