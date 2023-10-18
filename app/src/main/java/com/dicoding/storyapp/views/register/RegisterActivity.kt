package com.dicoding.storyapp.views.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.storyapp.customview.EmailEditText
import com.dicoding.storyapp.customview.PasswordEditText
import com.dicoding.storyapp.data.remote.response.ErrorResponse
import com.dicoding.storyapp.databinding.ActivityRegisterBinding
import com.dicoding.storyapp.views.LoginActivity
import com.google.gson.Gson
import retrofit2.HttpException

class RegisterActivity : AppCompatActivity() {
    private val viewModel by viewModels<RegisterViewModel>()
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var passwordEditText: PasswordEditText
    private lateinit var emailEditText: EmailEditText
    private lateinit var nameEditText: EditText
    private var correctEmail = false
    private var correctName = false
    private var correctPassword = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText
        nameEditText = binding.nameEditText

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

        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing TODO
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                correctName = s.toString().isNotEmpty()
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
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

//        viewModel.isLoading.observe(this) {
//            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//        }

        binding.btnSignUp.setOnClickListener {
            viewModel.postRegister(
                nameEditText.text.toString(),
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }

        viewModel.isErrorResponse.observe(this) {
            viewModel.registerMessage.observe(this) { message ->
                if (it) {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                } else {
                    val loginIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    loginIntent.putExtra(LoginActivity.EXTRA_MESSAGE, message)
                    startActivity(loginIntent)
                    finish()
                }
            }
        }
    }

    private fun enableButton() {
        binding.btnSignUp.isEnabled = correctEmail && correctPassword && correctName
    }
}