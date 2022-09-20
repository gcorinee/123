package com.example.cs496_pj2_ui.service.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    @SerializedName("id")
    val id: String,

    @SerializedName("username")
    val name: String,

    @SerializedName("imgUrl")
    val imgUrl: String?,
): Parcelable
