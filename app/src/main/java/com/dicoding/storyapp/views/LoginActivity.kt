package com.dicoding.storyapp.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.dicoding.storyapp.customview.EmailEditText
import com.dicoding.storyapp.customview.PasswordEditText
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.views.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
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
            finish()
        }
    }

    private fun enableButton() {
        binding.btnLogin.isEnabled = correctEmail && correctPassword
    }

    companion object {
        const val EXTRA_MESSAGE = "extra_message"
    }
}