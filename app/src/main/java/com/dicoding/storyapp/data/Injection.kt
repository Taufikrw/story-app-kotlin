package com.dicoding.storyapp.data

import android.content.Context
import com.dicoding.storyapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository = runBlocking  {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = pref.getLoginToken().first()
        val apiService = ApiConfig.getApiService(user)
        StoryRepository.getInstance(apiService, pref)
    }
}