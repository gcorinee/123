package com.example.cs496_pj2_ui.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs496_pj2_ui.R
import com.example.cs496_pj2_ui.databinding.ProfileDailyScheduleActivityBinding
import com.example.cs496_pj2_ui.service.model.ScheduleData
import com.example.cs496_pj2_ui.service.model.UserData
import java.util.*
import kotlin.collections.ArrayList

class ProfileDailyScheduleActivityAdapter(val context: Context)
    : RecyclerView.Adapter<ProfileDailyScheduleActivityAdapter.CustomViewHolder>() {

    private lateinit var binding: ProfileDailyScheduleActivityBinding
    private var schedules: ArrayList<ScheduleData> = arrayListOf()

    override fun getItemCount(): Int {
        return schedules.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileDailyScheduleActivityAdapter.CustomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.calendar_detail_row, parent, false)
        binding = ProfileDailyScheduleActivityBinding.inflate(LayoutInflater.from(context), parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProfileDailyScheduleActivityAdapter.CustomViewHolder,
        position: Int
    ) {
        holder.time.text = (schedules[position].time/100).toString() +":" + (schedules[position].time%100).toString()
        holder.todo.text = schedules[position].todo ?: ""
        holder.friends.text = schedules[position].friends?.let {
            it.toString()
        } ?: ""
        holder.location.text = schedules[position].location ?: ""

    }

    fun addDailySchedule(schedules: ArrayList<ScheduleData>) {
        this.schedules = schedules
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val time = itemView.findViewById<TextView>(R.id.tv_time)!!
        val todo = itemView.findViewById<TextView>(R.id.tv_todo)!!
        val friends = itemView.findViewById<TextView>(R.id.tv_friends)!!
        val location = itemView.findViewById<TextView>(R.id.tv_location)!!

        fun bind(schedule: ScheduleData) {

        }
    }

}