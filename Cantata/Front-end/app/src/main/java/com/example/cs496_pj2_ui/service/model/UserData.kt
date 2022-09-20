package com.example.cs496_pj2_ui.service.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(
    @SerializedName("_id")
    val id: String,

    @SerializedName("username")
    val name: String,

    @SerializedName("image_url")
    val imgUrl: String?,

    @SerializedName("status")
    var status: String?,

    @SerializedName("food")
    var food: String?,

    @SerializedName("hobby")
    var hobby: String?,

    @SerializedName("favorites")
    var favorites: String?,

    @SerializedName("weekend")
    var weekend: String?,

    ): Parcelable