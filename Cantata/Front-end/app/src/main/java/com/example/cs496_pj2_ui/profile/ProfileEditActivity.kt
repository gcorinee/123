package com.example.cs496_pj2_ui.profile

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cs496_pj2_ui.R
import com.example.cs496_pj2_ui.databinding.ProfileEditActivityBinding
import com.example.cs496_pj2_ui.service.RetrofitService
import com.example.cs496_pj2_ui.service.model.ResponseCode
import com.example.cs496_pj2_ui.service.model.UserData
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileEditActivity : AppCompatActivity() {

    private lateinit var binding: ProfileEditActivityBinding

    private lateinit var data: UserData
    private var imgUrl: String? = null

    private val requiredPermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProfileEditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = intent.getParcelableExtra("data")!!

        imgUrl = data.imgUrl
        RetrofitService.fetchImg(this, imgUrl, binding.imgProfileEdit)

        binding.imgProfileEdit.setOnClickListener {
            val status = ContextCompat.checkSelfPermission(this, "android.permission.CAMERA")
            checkPermissions()

            val code = 1
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent,code)
        }

        binding.etStatusEdit.setText(data.status ?: "")
        binding.etFoodEdit.setText(data.food ?: "")
        binding.etHobbyEdit.setText(data.hobby ?: "")
        binding.etFavoritesEdit.setText(data.favorites ?: "")
        binding.etWeekend.setText(data.weekend ?: "")

        binding.btnSaveEdit.setOnClickListener {
            val userData = UserData(
                data.id, data.name, imgUrl, binding.etStatusEdit.text.toString(),
                binding.etFoodEdit.text.toString(), binding.etHobbyEdit.text.toString(),
                binding.etFavoritesEdit.text.toString(), binding.etWeekend.text.toString(),
            )
            val call = RetrofitService.retrofitInterface.editUser(userData)
            call.enqueue(object: Callback<ResponseCode> {
                override fun onFailure(call: Call<ResponseCode>, t: Throwable) {
                    Log.e(RetrofitService.TAG, t.message!!)
                }

                override fun onResponse(
                    call: Call<ResponseCode>,
                    response: Response<ResponseCode>
                ) {
                    if (response.isSuccessful) {
                        val intent = Intent()
                        intent.putExtra("data", userData)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            })
        }

        binding.btnCancelEdit.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1)
        {
            if(resultCode == RESULT_OK)
            {
                val uri = data?.data
                if (uri != null) {
                    val path = absolutelyPath(uri, this)
                    val file = File(path)
                    val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
                    val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

                    val call = RetrofitService.retrofitInterface.postImg(body)
                    call.enqueue(object: Callback<String> {
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Log.e(RetrofitService.TAG, t.message!!)
                        }

                        override fun onResponse(
                            call: Call<String>,
                            response: Response<String>
                        ) {
                            if (response.isSuccessful) {
                                imgUrl = response.body() ?: null
                                RetrofitService.fetchImg(baseContext, imgUrl, binding.imgProfileEdit)
                            }
                        }
                    })

                }
            }
            else if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    private fun absolutelyPath(path: Uri?, context : Context): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)

        return result!!
    }

    private fun checkPermissions() {
        var rejectedPermissions = arrayListOf<String>()

        for (permission in requiredPermissions) {
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                rejectedPermissions.add(permission)
            }
        }

        if(rejectedPermissions.isNotEmpty()){
            val array = arrayOfNulls<String>(rejectedPermissions.size)
            ActivityCompat.requestPermissions(this, rejectedPermissions.toArray(array), 100)
        }
    }
}