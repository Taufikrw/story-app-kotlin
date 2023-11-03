package com.dicoding.storyapp.views.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.storyapp.data.StoryRepository

class LoginViewModel(private val repository: StoryRepository): ViewModel() {
    suspend fun login(email: String, password: String) = repository.login(email, password)

    fun getToken(): LiveData<String> = repository.getSession().asLiveData()

    suspend fun saveToken(token: String) = repository.saveToken(token)

//    fun destroyToken() {
//        viewModelScope.launch {
//            pref.destroyToken()
//        }
//    }
}