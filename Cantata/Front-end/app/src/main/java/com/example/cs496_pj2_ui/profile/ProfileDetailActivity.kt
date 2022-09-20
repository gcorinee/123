package com.example.cs496_pj2_ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cs496_pj2_ui.R
import com.example.cs496_pj2_ui.databinding.ProfileDetailActivityBinding
import com.example.cs496_pj2_ui.databinding.ProfileMonthlyScheduleActivityBinding
import com.example.cs496_pj2_ui.service.RetrofitService
import com.example.cs496_pj2_ui.service.model.UserData
import com.kakao.sdk.user.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileDetailActivity : AppCompatActivity() {

    private lateinit var binding: ProfileDetailActivityBinding
    private lateinit var id: String
    lateinit var data: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProfileDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        data = intent.getParcelableExtra("data")!!

        //if intent does not have id, it is myinfo
        id = intent.getStringExtra("id") ?: data.id

        binding.tvNameProfileDetail.text = data.name
    }

    override fun onResume() {
        super.onResume()

        binding.tvStatusProfileDetail.text = data.status ?: ""

        RetrofitService.fetchImg(this, data.imgUrl, binding.imgProfileDetail)

        binding.tvFood.text = data.food?.let {
            if (it.isNotEmpty()) {
                it + "를 먹고싶어요!"
            } else {
                "좋아하는 음식을 알려주세요!"
            }
        } ?: "좋아하는 음식을 알려주세요!"
        binding.tvHobby.text = data.hobby?.let {
            if (it.isNotEmpty()) {
                it + "하는 것을 좋아해요!"
            } else {
                "좋아하는 취미를 알려주세요!"
            }
        } ?: "좋아하는 취미를 알려주세요!"
        binding.tvFavorites.text = data.favorites?.let {
            if (it.isNotEmpty()) {
                it + "를 좋아해요!"
            } else {
                "관심사를 알려주세요!"
            }
        } ?: "관심사를 알려주세요!"
        binding.tvWeekend.text = data.weekend?.let {
            if (it.isNotEmpty()) {
                it + "를 하면서 쉬어요!"
            } else {
                "주말에 하는 일을 공유해요!"
            }
        } ?: "주말에 하는 일을 공유해요!"

        binding.cvProfileDetail.setOnClickListener {
            // TODO: Image Detail View
        }

        // Disable Send button if Detail view is showing my Info
        if (id == data.id) {
            binding.imgPromisProfile.visibility = View.INVISIBLE
            binding.imgPromisProfile.isEnabled = false
        }

        binding.imgScheduleProfile.setOnClickListener {
            val call = RetrofitService.retrofitInterface.getUserById(id)
            call.enqueue(object: Callback<UserData> {
                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    Log.e(RetrofitService.TAG, t.message!!)
                }

                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    if (response.isSuccessful) {
                        val sender = response.body()!!

                        val intent = Intent(baseContext, ProfileMonthlyScheduleActivity::class.java)
                        intent.putExtra("receiver", data)
                        intent.putExtra("sender", sender)
                        startActivity(intent)
                    }
                }
            })
        }

        binding.imgPromisProfile.setOnClickListener {
            val call = RetrofitService.retrofitInterface.getUserById(id)
            call.enqueue(object: Callback<UserData> {
                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    Log.e(RetrofitService.TAG, t.message!!)
                }

                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    if (response.isSuccessful) {
                        val sender = response.body()!!

                        val intent = Intent(baseContext, ProfileDailyScheduleAddActivity::class.java)
                        intent.putExtra("receiver", data)
                        intent.putExtra("sender", sender)
                        startActivity(intent)
                    }
                }
            })

        }

        binding.imgDmProfile.setOnClickListener {

        }

        if (id != data.id) {
            binding.btnEdit.visibility = View.INVISIBLE
            binding.btnEdit.isEnabled = false
        } else {
            binding.btnEdit.setOnClickListener {
                val intent = Intent(baseContext, ProfileEditActivity::class.java)
                intent.putExtra("data", data)
                startActivityForResult(intent, 200)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                this.data = data?.getParcelableExtra("data")!!
            }
        }
    }
}