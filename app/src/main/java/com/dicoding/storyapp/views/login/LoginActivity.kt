package com.dicoding.storyapp.views.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.views.MainActivity
import com.dicoding.storyapp.R
import com.dicoding.storyapp.customview.EmailEditText
import com.dicoding.storyapp.customview.PasswordEditText
import com.dicoding.storyapp.data.UserPreferences
import com.dicoding.storyapp.data.dataStore
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.views.ViewModelFactory
import com.dicoding.storyapp.views.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var passwordEditText: PasswordEditText
    private lateinit var emailEditText: EmailEditText
    private var correctEmail = false
    private var correctPassword = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val pref = UserPreferences.getInstance(application.dataStore)
        val viewModel = ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

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

        viewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }

        viewModel.isErrorResponse.observe(this) {
            viewModel.loginMessage.observe(this) { message ->
                if (it) {
                    Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.loginResult.observe(this) { result ->
                        viewModel.saveToken("Bearer " + result.token)
                        AlertDialog.Builder(this).apply {
                            setTitle(R.string.success)
                            setMessage(R.string.login_success_desc)
                            setPositiveButton("Lanjut") { _, _ ->
                                val loginIntent = Intent(this@LoginActivity, MainActivity::class.java)
                                loginIntent.putExtra(MainActivity.USER_TOKEN,"Bearer " + result.token)
                                startActivity(loginIntent)
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                }
            }
        }

        // TODO DataStore Sementara
        viewModel.getToken().observe(this) {
            if (it.isNotEmpty()) {
                startActivity(
                    Intent(this@LoginActivity, MainActivity::class.java)
                        .putExtra(MainActivity.USER_TOKEN, it)
                )
                finish()
            }
        }
    }

    private fun enableButton() {
        binding.btnLogin.isEnabled = correctEmail && correctPassword
    }

    companion object {
        const val EXTRA_MESSAGE = "extra_message"
    }
}