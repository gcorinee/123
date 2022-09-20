package com.example.cs496_pj2_ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cs496_pj2_ui.databinding.LoginActivityBinding
import com.example.cs496_pj2_ui.service.LoginRequest
import com.example.cs496_pj2_ui.service.LoginResponse
import com.example.cs496_pj2_ui.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Button Listeners
        binding.btnLogin.setOnClickListener {
            val id = binding.etId.text.toString()
            val pw = binding.etPw.text.toString()
            executeLogin(id, pw)
        }

        binding.btnSignupLogin.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.btnLoginKakao.setOnClickListener {

        }
    }

    private fun executeLogin(id: String, pw: String) {
        val loginRequest = LoginRequest(id, pw, null)
        val call = RetrofitService.retrofitInterface.executeLogin(loginRequest)
        call.enqueue(object: Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(RetrofitService.TAG, t.message + "loginRequest")
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.body() != null) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("id", response.body()?.id!!)
                    startActivity(intent)
                    finish()
                }
            }
        })
    }
}