package com.example.cs496_pj2_ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cs496_pj2_ui.databinding.ProfileMonthlyScheduleFragmentBinding
import com.example.cs496_pj2_ui.service.RetrofitService
import com.example.cs496_pj2_ui.service.model.CustomCalendar
import com.example.cs496_pj2_ui.service.model.ScheduleData
import com.example.cs496_pj2_ui.service.model.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProfileMonthlyScheduleFragment : Fragment() {

    private lateinit var binding: ProfileMonthlyScheduleFragmentBinding
    private lateinit var mContext: Context
    private lateinit var adapter: ProfileMonthlyScheduleFragmentAdapter

    private lateinit var scheduleData: ArrayList<ScheduleData>
    private var pageIndex = 0

    private lateinit var sender: UserData
    private lateinit var receiver: UserData

    private lateinit var date: Date

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pageIndex = arguments?.getInt("position") as Int - (Int.MAX_VALUE / 2)

        sender = arguments?.getParcelable("sender")!!
        receiver = arguments?.getParcelable("receiver")!!

        date = Calendar.getInstance().run {
            add(Calendar.MONTH, pageIndex)
            time
        }

        binding = ProfileMonthlyScheduleFragmentBinding.inflate(inflater, container, false)
        binding.calendarDate.text = SimpleDateFormat("yyyy년 MM월", Locale.KOREA).format(date.time).toString()

        adapter = ProfileMonthlyScheduleFragmentAdapter(mContext, binding.calendarLayout, date, sender, receiver)
        binding.rvMonthlyCalendar.layoutManager = GridLayoutManager(mContext, CustomCalendar.DAYS_OF_WEEK)
        binding.rvMonthlyCalendar.adapter = adapter
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onResume() {
        super.onResume()

        val year = SimpleDateFormat("yyyy", Locale.KOREA).format(date.time).toString().toInt()
        val month = SimpleDateFormat("MM", Locale.KOREA).format(date.time).toString().toInt()
        val call = RetrofitService.retrofitInterface.getUserMonthlySchedule(receiver.id, year, month)
        call.enqueue(object: Callback<ArrayList<ScheduleData>> {
            override fun onFailure(call: Call<ArrayList<ScheduleData>>, t: Throwable) {
                Log.e(RetrofitService.TAG, t.message+"in monthly schedule")
            }

            override fun onResponse(
                call: Call<ArrayList<ScheduleData>>,
                response: Response<ArrayList<ScheduleData>>
            ) {
                if (response.body() != null) {
                    scheduleData = response.body()!!
                    adapter.addSchedule(scheduleData)
                } else {
                    scheduleData = arrayListOf()
                }
            }
        })
    }

}