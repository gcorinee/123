package com.example.cs496_pj2_ui.profile

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cs496_pj2_ui.R
import com.example.cs496_pj2_ui.databinding.ProfileRowBinding
import com.example.cs496_pj2_ui.service.RetrofitService
import com.example.cs496_pj2_ui.service.model.UserData

class ProfileMainAdapter(val context: Context, val id: String): RecyclerView.Adapter<ProfileMainAdapter.CustomViewHolder>() {

    private lateinit var binding: ProfileRowBinding
    var friendsData: ArrayList<UserData> = arrayListOf()

    override fun getItemCount(): Int {
        return friendsData.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileMainAdapter.CustomViewHolder {
        //binding = ProfileRowBinding.inflate(LayoutInflater.from(context))
        val view = LayoutInflater.from(context).inflate(R.layout.profile_row, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileMainAdapter.CustomViewHolder, position: Int) {
        holder.name.text = friendsData[position].name
        holder.status.text = friendsData[position].status

        RetrofitService.fetchImg(context, friendsData[position].imgUrl, holder.imgProfile)
        holder.bind(friendsData[position])
    }

    fun addFriendItem(friendData: UserData) {
        this.friendsData.add(friendData)
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgProfile = itemView.findViewById<ImageView>(R.id.img_profile)!!
        val name = itemView.findViewById<TextView>(R.id.tv_name_profile)!!
        val status = itemView.findViewById<TextView>(R.id.tv_status_profile)!!

        fun bind(data: UserData) {
            itemView.setOnClickListener {
                val intent = Intent(context, ProfileDetailActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("data", data)
                context.startActivity(intent)
            }
        }
    }
}