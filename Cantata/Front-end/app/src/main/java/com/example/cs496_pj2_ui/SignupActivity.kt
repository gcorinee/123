package com.example.cs496_pj2_ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.cs496_pj2_ui.databinding.SignupActivityBinding
import com.example.cs496_pj2_ui.service.RetrofitService
import com.example.cs496_pj2_ui.service.SignupRequest
import com.example.cs496_pj2_ui.service.model.ResponseCode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: SignupActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Button setup
        binding.btnSignup.setOnClickListener {
            val name = binding.etName.text.toString()
            val id = binding.etId.text.toString()
            val pw = binding.etPw.text.toString()
            val pwCheck = binding.etPw2.text.toString()

            // PW Validity
            if (pw != pwCheck) {
                Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show()
            } else {
                val signupRequest = SignupRequest(id, pw, name, null)
                val call = RetrofitService.retrofitInterface.executeSignup(signupRequest)
                call.enqueue(object: Callback<ResponseCode> {
                    override fun onFailure(call: Call<ResponseCode>, t: Throwable) {
                        Log.e(RetrofitService.TAG, t.message + "in signup")
                    }

                    override fun onResponse(
                        call: Call<ResponseCode>,
                        response: Response<ResponseCode>
                    ) {
                        if (response.body() == ResponseCode(400)) {
                            Toast.makeText(this@SignupActivity, "회원가입 실패. 다시 시도하세요.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@SignupActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                })
            }
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }
}