package com.example.cs496_pj2_ui.service

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.cs496_pj2_ui.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {

    private const val BASE_URL = "http://192.249.18.210"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitInterface: RetrofitInterface by lazy {
        retrofit.create(RetrofitInterface::class.java)
    }

    const val TAG = "APIService"

    fun fetchImg(context: Context, imgUrl: String?, view: ImageView, isOverride: Boolean = false) {
        if (imgUrl != null) {
            if (isOverride) {
                Glide.with(context).load(BASE_URL + "/" + imgUrl)
                    .placeholder(R.drawable.loading_bar)
                    .fallback(R.drawable.account)
                    .error(R.drawable.account)
                    .override(60, 60)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .apply(RequestOptions().centerCrop())
                    .into(view)
            } else {
                Glide.with(context).load(BASE_URL + "/" + imgUrl)
                    .placeholder(R.drawable.loading_bar)
                    .fallback(R.drawable.account)
                    .error(R.drawable.account)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .apply(RequestOptions().centerCrop())
                    .into(view)
            }
        } else {
            view.setImageResource(R.drawable.account)
        }
    }

}