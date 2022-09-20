package com.example.cs496_pj2_ui.service.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.Duration

@Parcelize
data class PromiseRequest (

    @SerializedName("_id")
    val senderId: String,
    @SerializedName("sender_name")
    val senderName: String,
    @SerializedName("sender_image")
    val senderImg: String?,
    @SerializedName("receiver")
    val receiverId: String,

    @SerializedName("year")
    val year: Int,
    @SerializedName("month")
    val month: Int,
    @SerializedName("date")
    val date: Int,
    @SerializedName("time")
    val time: Int,

    @SerializedName("duration")
    val duration: Int,
    @SerializedName("todo")
    val todo: String?,
    @SerializedName("location")
    val location: String?,

    @SerializedName("message")
    val message: String?
): Parcelable

data class PromiseRequestResponse(
    @SerializedName("_id")
    val requestId: String,
    @SerializedName("sender_id")
    val senderId: String,
    @SerializedName("receiver")
    val receiverId: String,
    @SerializedName("sender_image")
    val imgUrl: String?,
    @SerializedName("sender_name")
    val name: String,

    @SerializedName("year")
    val year: Int,
    @SerializedName("month")
    val month: Int,
    @SerializedName("date")
    val date: Int,
    @SerializedName("time")
    val time: Int,
    @SerializedName("duration")
    val duration: Int,

    @SerializedName("message")
    val message: String?,
)

data class PromiseResponse (

    val promiseId: String,
    val response: Boolean,

    )
