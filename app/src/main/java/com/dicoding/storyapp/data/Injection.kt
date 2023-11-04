package com.dicoding.storyapp.data

import android.content.Context
import com.dicoding.storyapp.data.database.StoryDatabase
import com.dicoding.storyapp.data.preferences.UserPreferences
import com.dicoding.storyapp.data.preferences.dataStore
import com.dicoding.storyapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository = runBlocking  {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = pref.getLoginToken().first()
        val apiService = ApiConfig.getApiService(user)
        val database = StoryDatabase.getDatabase(context)
        StoryRepository.getInstance(database, apiService, pref)
    }
}