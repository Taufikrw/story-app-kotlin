package com.dicoding.storyapp.views.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.storyapp.views.MainActivity
import com.dicoding.storyapp.R
import com.dicoding.storyapp.customview.EmailEditText
import com.dicoding.storyapp.customview.PasswordEditText
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.UserPreferences
import com.dicoding.storyapp.data.dataStore
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.views.ViewModelFactory
import com.dicoding.storyapp.views.register.RegisterActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding
    private lateinit var passwordEditText: PasswordEditText
    private lateinit var emailEditText: EmailEditText
    private var correctEmail = false
    private var correctPassword = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        playAnimation()
        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing TODO
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val emailRegex = Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
                correctEmail = if (!emailRegex.matches(s.toString().trim())) {
                    binding.emailEditTextLayout.error = "Email must be filled and valid"
                    false
                } else {
                    binding.emailEditTextLayout.error = null
                    true
                }
                enableButton()
            }

            override fun afterTextChanged(s: Editable?) {
                // Nothing TODO
            }
        })

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing TODO
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                correctPassword = if (s.toString().length < 8) {
                    binding.passwordEditTextLayout.error = "Password must be filled and min 8 char"
                    false
                } else {
                    binding.passwordEditTextLayout.error = null
                    true
                }
                enableButton()
            }

            override fun afterTextChanged(s: Editable?) {
                // Nothing TODO
            }
        })

        binding.tvAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            lifecycleScope.launch {
                viewModel.login(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                ).observe(this@LoginActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }

                            is Result.Success -> {
                                showLoading(false)
                                AlertDialog.Builder(this@LoginActivity).apply {
                                    setTitle(R.string.success)
                                    setMessage(R.string.login_success_desc)
                                    setPositiveButton(R.string.next) { _, _ ->
                                        saveSession(result.data.loginResult?.token.toString())
                                        val loginIntent =
                                            Intent(this@LoginActivity, MainActivity::class.java)
                                        ViewModelFactory.clearInstance()
                                        startActivity(loginIntent)
                                        finish()
                                    }
                                    create()
                                    show()
                                }
                            }

                            is Result.Error -> {
                                showLoading(false)
                                Toast.makeText(this@LoginActivity, result.error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

        // CEK TOKEN LOGIN DataStore
        viewModel.getToken().observe(this) {
            if (it.isNotEmpty()) {
                startActivity(
                    Intent(this@LoginActivity, MainActivity::class.java)
                )
                finish()
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.loginIcon, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.welcomeTitle, View.ALPHA, 1f).setDuration(500)
        val subtitle = ObjectAnimator.ofFloat(binding.welcomeSubTitle, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val account = ObjectAnimator.ofFloat(binding.tvAccount, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                subtitle,
                email,
                password,
                account,
                login
            )
            start()
        }
    }

    private fun saveSession(token: String) {
        lifecycleScope.launch {
            viewModel.saveToken(token)
        }
    }

    private fun enableButton() {
        binding.btnLogin.isEnabled = correctEmail && correctPassword
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}