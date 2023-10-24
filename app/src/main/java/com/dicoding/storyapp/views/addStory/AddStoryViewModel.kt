package com.dicoding.storyapp.views.addStory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.remote.response.ErrorResponse
import com.dicoding.storyapp.data.remote.response.FileUploadResponse
import com.dicoding.storyapp.data.remote.retrofit.ApiConfig
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _uploadMessage = MutableLiveData<String>()
    val uploadMessage: LiveData<String> = _uploadMessage

    private val _isErrorResponse = MutableLiveData<Boolean>()
    val isErrorResponse: LiveData<Boolean> = _isErrorResponse

    fun postStory(token: String, photo: MultipartBody.Part, description: RequestBody){
        _isLoading.value = true
        val client = ApiConfig.getApiService().uploadImage(token, photo, description)
        client.enqueue(object: Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _uploadMessage.value = response.body()?.message!!
                    _isErrorResponse.value = false
                }else{
                    _isErrorResponse.value = true
                    val jsonInString = response.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                    _uploadMessage.value = errorBody.message!!
                }
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })

    }

    companion object {
        const val TAG = "AddStoryViewModel"
    }
}