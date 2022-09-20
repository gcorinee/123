package com.example.cs496_pj2_ui.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cs496_pj2_ui.databinding.BoardAddActivityBinding
import com.example.cs496_pj2_ui.service.RetrofitService
import com.example.cs496_pj2_ui.service.model.ResponseCode
import com.example.cs496_pj2_ui.service.model.WriteBoardRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardAddActivity : AppCompatActivity() {

    private lateinit var binding: BoardAddActivityBinding
    private lateinit var id: String
    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BoardAddActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id")!!
        name = intent.getStringExtra("name")!!

        binding.btnSaveBoardAdd.setOnClickListener {
            val req = WriteBoardRequest(id, name, binding.etTitleAdd.text.toString(), binding.evContentAdd.text.toString())
            val call = RetrofitService.retrofitInterface.writeBoard(req)
            call.enqueue(object: Callback<ResponseCode> {
                override fun onFailure(call: Call<ResponseCode>, t: Throwable) {
                    Log.e(RetrofitService.TAG, t.message!!)
                }

                override fun onResponse(
                    call: Call<ResponseCode>,
                    response: Response<ResponseCode>
                ) {
                    if (response.isSuccessful) {
                        finish()
                    }
                }
            })
        }

        binding.btnSaveBoardCancel.setOnClickListener {
            finish()
        }
    }
}