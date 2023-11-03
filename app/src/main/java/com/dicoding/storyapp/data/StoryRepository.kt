package com.dicoding.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyapp.data.remote.response.ErrorResponse
import com.dicoding.storyapp.data.remote.response.FileUploadResponse
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.RegisterResponse
import com.dicoding.storyapp.data.remote.response.StoryResponse
import com.dicoding.storyapp.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class StoryRepository(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {
    suspend fun register(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            emit(Result.Error(errorBody.message.toString()))
        }
    }

    suspend fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            emit(Result.Error(errorBody.message.toString()))
        }
    }

    suspend fun saveToken(token: String) {
        userPreferences.saveToken(token)
    }

    fun getSession(): Flow<String> = userPreferences.getLoginToken()

    suspend fun destroyToken() {
        userPreferences.destroyToken()
    }

    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    suspend fun postStory(
        file: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<FileUploadResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.uploadImage(file, description)
            if (response.error == true) {
                emit(Result.Error(response.message.toString()))
            } else {
                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun getStoryWithLocation(): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoriesWithLocation()
            if (response.error == true) {
                emit(Result.Error(response.message.toString()))
            } else {
                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun clearInstance() {
            instance = null
        }

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreferences
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, userPreference)
            }.also { instance = it }
    }
}