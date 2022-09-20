package com.example.cs496_pj2_ui.service

import com.example.cs496_pj2_ui.service.model.*
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetrofitInterface {

    //region Login
    @POST("/user/login")
    fun executeLogin(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/user/signup")
    fun executeSignup(@Body signupRequest: SignupRequest): Call<ResponseCode>
    //endregion

    //region Image
    @Multipart
    @POST("/upload")
    fun postImg(@Part photo: MultipartBody.Part): Call<String>
    //endregion

    //region User Data
    @GET("/user/{id}")
    fun getUserById(@Path("id") id: String): Call<UserData>

    @GET("/user/friends/{id}")
    fun getUserFriends(@Path("id") id: String): Call<ArrayList<String>>

    @POST("/user/update")
    fun editUser(@Body userData: UserData): Call<ResponseCode>

    @GET("/user/schedule/{id}/{year}/{month}")
    fun getUserMonthlySchedule(@Path("id") id: String, @Path("year") year: Int, @Path("month") month: Int): Call<ArrayList<ScheduleData>>

    @GET("/user/schedule/date/{id}/{year}/{month}/{date}")
    fun getUserDailySchedule(@Path("id") id: String, @Path("year") year: Int, @Path("month") month: Int, @Path("date") date: Int): Call<ArrayList<ScheduleData>>
    //endregion

    //@GET("/")
    //fun getChatsById(@Path("id") id: String): Call<ArrayList<Chat>>

    //region Promise
    @POST("/request")
    fun sendRequest(@Body promiseRequest: PromiseRequest): Call<DefaultResponse>

    @GET("/request/receive/{id}")
    fun getReceivedRequestById(@Path("id") id: String): Call<ArrayList<PromiseRequestResponse>>

    @GET("/request/accept/{id}")
    fun getAcceptedRequestById(@Path("id") id: String): Call<ArrayList<PromiseRequestResponse>>

    @GET("/request/sent/{id}")
    fun getSentRequestById(@Path("id") id: String): Call<ArrayList<PromiseRequestResponse>>

    @GET("/request/{id}/{accept}")
    fun sendResponse(@Path("id") id: String, @Path("accept") accept: Boolean): Call<ResponseCode>
    //endregion

    //region Board
    @GET("/board")
    fun getBoards(): Call<ArrayList<Board>>

    @POST("/board/write")
    fun writeBoard(@Body writeBoardRequest: WriteBoardRequest): Call<ResponseCode>

    @GET("/board/{id}")
    fun findBoardById(@Path("id") id: String): Call<Board>

    @POST("/board/delete")
    fun deleteBoard(@Body deleteBoardRequest: DeleteBoardRequest): Call<ResponseCode>

    @POST("/board/comment")
    fun writeComment(@Body writeCommentRequest: WriteCommentRequest): Call<ResponseCode>

    @POST("/board/comment/delete")
    fun deleteComment(@Body deleteCommentRequest: DeleteCommentRequest): Call<ResponseCode>
    //endregion
}

data class DefaultResponse (
    @SerializedName("ResponseCode")
    val code: Int
)