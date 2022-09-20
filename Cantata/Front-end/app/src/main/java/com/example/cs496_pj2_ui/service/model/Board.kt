package com.example.cs496_pj2_ui.service.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Board(
    //todo: Board Id 추가하자고 하기
    @SerializedName("_id")
    val boardId: String,

    @SerializedName("id")
    val userId: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("content")
    val content: String?,

    @SerializedName("votes")
    val votes: Int?,

    @SerializedName("views")
    val views: Int,

    @SerializedName("comments")
    val comments: ArrayList<Comment>?,
): Parcelable

@Parcelize
data class Comment(
    @SerializedName("id")
    val id: String,

    @SerializedName("content")
    val content: String?,

): Parcelable

data class WriteBoardRequest(
    val id: String,
    val username: String,
    val title: String,
    val content: String,
)

data class DeleteBoardRequest(
    val _id: String,
    val id: String
)

data class WriteCommentRequest(
    val _id: String,
    val id: String,
    val comment: Comment
)

data class DeleteCommentRequest(
    val id1: String, // Board id
    val id2: String, // Comment
    val id3: String // User
)