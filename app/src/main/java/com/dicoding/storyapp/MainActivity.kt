package com.dicoding.storyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.data.UserPreferences
import com.dicoding.storyapp.data.dataStore
import com.dicoding.storyapp.views.ViewModelFactory
import com.dicoding.storyapp.views.login.LoginViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val pref = UserPreferences.getInstance(application.dataStore)
        val viewModel = ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    companion object {
        const val USER_TOKEN = "user_token"
    }
}