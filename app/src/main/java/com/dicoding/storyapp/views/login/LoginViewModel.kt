package com.dicoding.storyapp.views.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.UserPreferences
import com.dicoding.storyapp.data.remote.response.ErrorResponse
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.LoginResult
import com.dicoding.storyapp.data.remote.retrofit.ApiConfig
import com.dicoding.storyapp.views.register.RegisterViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreferences): ViewModel() {
    private val _isErrorResponse = MutableLiveData<Boolean>()
    val isErrorResponse: LiveData<Boolean> = _isErrorResponse

    private val _loginMessage = MutableLiveData<String>()
    val loginMessage: LiveData<String> = _loginMessage

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(
        email: String,
        password: String
    ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _loginMessage.value = response.body()?.message!!
                    _loginResult.value = response.body()?.loginResult!!
                    _isErrorResponse.value = false
                } else {
                    _isErrorResponse.value = true
                    val jsonInString = response.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                    _loginMessage.value = errorBody.message!!
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(LoginViewModel.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getToken(): LiveData<String> {
        return pref.getLoginToken().asLiveData()
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

    companion object{
        private const val TAG = "LoginViewModel"
    }
}