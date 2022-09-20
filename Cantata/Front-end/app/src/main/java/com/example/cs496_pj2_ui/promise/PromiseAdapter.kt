package com.example.cs496_pj2_ui.promise

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cs496_pj2_ui.R
import com.example.cs496_pj2_ui.databinding.ProfileRowBinding
import com.example.cs496_pj2_ui.service.RetrofitService
import com.example.cs496_pj2_ui.service.SocketService
import com.example.cs496_pj2_ui.service.model.PromiseRequest
import com.example.cs496_pj2_ui.service.model.PromiseRequestResponse
import com.example.cs496_pj2_ui.service.model.ResponseCode
import com.example.cs496_pj2_ui.service.model.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PromiseAdapter(val context: Context, val pos: Int): RecyclerView.Adapter<PromiseAdapter.CustomViewHolder>() {

    private lateinit var binding: ProfileRowBinding
    var responses: ArrayList<PromiseRequestResponse> = arrayListOf()

    override fun getItemCount(): Int {
        return responses.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        //binding = ProfileRowBinding.inflate(LayoutInflater.from(context))
        val view = LayoutInflater.from(context).inflate(R.layout.promise_row, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        // Profile
        RetrofitService.fetchImg(context, responses[position].imgUrl, holder.imgProfile)

        // Contents
        if (responses[position].message == null) {
            holder.message.text = responses[position].name + "님이 요청을 보내셨습니다."
        } else {
            holder.message.text = responses[position].name + " : " + responses[position].message
        }

        // Date Time
        val timeString = (responses[position].time/100).toString() + ":" + (responses[position].time%100).toString()
        val dateString = "${responses[position].year}년${responses[position].month}월${responses[position].date}일 " + timeString
        holder.dateTime.text = dateString
        holder.bind(position, responses[position])
    }

    fun updatePromises(response: ArrayList<PromiseRequestResponse>, isAccepted: Boolean = false) {
        this.responses = response
        notifyDataSetChanged()
    }

    fun clear() {
        this.responses.clear()
    }

    inner class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgProfile = itemView.findViewById<ImageView>(R.id.img_profile_promise)!!
        val message = itemView.findViewById<TextView>(R.id.tv_promise_content)!!
        val dateTime = itemView.findViewById<TextView>(R.id.tv_date_time)!!
        val accept = itemView.findViewById<ImageButton>(R.id.btn_accept)!!
        val reject = itemView.findViewById<ImageButton>(R.id.btn_reject)!!

        fun bind(index: Int, promise: PromiseRequestResponse) {
            if (pos == 0) {
                accept.setOnClickListener {
                    val call = RetrofitService.retrofitInterface.sendResponse(promise.requestId, true)
                    call.enqueue(object: Callback<ResponseCode> {
                        override fun onFailure(call: Call<ResponseCode>, t: Throwable) {
                            Log.e(RetrofitService.TAG, t.message!!)
                        }

                        override fun onResponse(
                            call: Call<ResponseCode>,
                            response: Response<ResponseCode>
                        ) {
                            if (response.isSuccessful) {
                                responses.removeAt(index)
                                notifyDataSetChanged()
                            }
                        }
                    })
                }

                reject.setOnClickListener {
                    val call = RetrofitService.retrofitInterface.sendResponse(promise.requestId, false)
                    call.enqueue(object: Callback<ResponseCode> {
                        override fun onFailure(call: Call<ResponseCode>, t: Throwable) {
                            Log.e(RetrofitService.TAG, t.message!!)
                        }

                        override fun onResponse(
                            call: Call<ResponseCode>,
                            response: Response<ResponseCode>
                        ) {
                            if (response.isSuccessful) {
                                responses.removeAt(index)
                                notifyDataSetChanged()
                            }
                        }
                    })
                }
            } else {
                accept.visibility = View.INVISIBLE
                reject.visibility = View.INVISIBLE
                accept.isEnabled = false
                reject.isEnabled = false
            }

        }
    }
}