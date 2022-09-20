package com.example.cs496_pj2_ui.profile

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cs496_pj2_ui.R
import com.example.cs496_pj2_ui.databinding.ProfileMonthlyScheduleFragmentBinding
import com.example.cs496_pj2_ui.service.RetrofitService
import com.example.cs496_pj2_ui.service.model.CustomCalendar
import com.example.cs496_pj2_ui.service.model.ScheduleData
import com.example.cs496_pj2_ui.service.model.UserData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProfileMonthlyScheduleFragmentAdapter(val context: Context, val calendarLayout: LinearLayout, val date: Date, val sender: UserData, val receiver: UserData)
    : RecyclerView.Adapter<ProfileMonthlyScheduleFragmentAdapter.CalendarItemHolder>() {

    var dates: ArrayList<Int> = arrayListOf()
    var schedules: ArrayList<ScheduleData> = arrayListOf()
    private var customCalendar: CustomCalendar = CustomCalendar(date)

    private val year = SimpleDateFormat("yyyy", Locale.KOREA).format(date).toInt()
    private val month = SimpleDateFormat("MM", Locale.KOREA).format(date).toInt()

    private var firstDateIndex: Int = 0
    private var lastDateIndex: Int = 0

    private lateinit var binding: ProfileMonthlyScheduleFragmentBinding

    init {
        customCalendar.initBaseCalendar()
        dates = customCalendar.dateList

        firstDateIndex = customCalendar.prevTail
        lastDateIndex = dates.size - customCalendar.nextHead - 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarItemHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.calendar_row, parent, false)
        binding = ProfileMonthlyScheduleFragmentBinding.inflate(LayoutInflater.from(context), parent, false)
        return CalendarItemHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarItemHolder, position: Int) {
        // Configure Layout
        val h = calendarLayout.height / 6
        holder.itemView.layoutParams.height = h

        holder.bind(dates[position], position, context)
    }

    override fun getItemCount(): Int {
        return dates.size
    }

    fun addSchedule(scheduleData: ArrayList<ScheduleData>) {
        this.schedules = scheduleData
        //notifyDataSetChanged()

        // 바뀐 날짜 리스트 생성
        val dateList = schedules.map { it.date }.distinct()

        // 리스트 index만 업로드
        for (changedDate in dateList) {
            notifyItemChanged(firstDateIndex + changedDate - 1)
        }
    }

    inner class CalendarItemHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var itemCalendarDateText: TextView = itemView!!.findViewById(R.id.calendar_row_date)
        val itemCalendarMarkDownText: TextView = itemView!!.findViewById(R.id.tv_markdown)

        fun bind(date: Int, position: Int, context: Context) {

            // 날짜 표시
            itemCalendarDateText.text = date.toString()

            // Schedule 처리
            val dateSchedules = schedules.filter { it.date == date }
            when (dateSchedules.size) {
                0 -> itemCalendarMarkDownText.text = ""
                1 -> itemCalendarMarkDownText.text = "."
                2 -> itemCalendarMarkDownText.text = ".."
                else -> itemCalendarMarkDownText.text = "..."
            }

            // 오늘 날짜 처리
            val dateString: String = SimpleDateFormat("dd", Locale.KOREA).format(Date())
            val dateInt = dateString.toInt()

            if ((date == dateInt) && isMatchYearMonth()) {
                itemCalendarDateText.setTypeface(itemCalendarDateText.typeface, Typeface.BOLD)
                itemCalendarDateText.setTextSize(Dimension.SP, 25F)
            }

            // 현재 월의 1일 이전, 현재 월의 마지막일 이후 값의 텍스트를 회색처리
            if (position < firstDateIndex || position > lastDateIndex) {
                itemCalendarDateText.setTextColor(ContextCompat.getColor(context, R.color.white))
            }

            itemView.setOnClickListener {
                val intent = Intent(context, ProfileDailyScheduleActivity::class.java)
                intent.putExtra("id", receiver.id)
                intent.putExtra("year", year)
                intent.putExtra("month", month)
                intent.putExtra("date", date)
                intent.putExtra("sender", sender)
                intent.putExtra("receiver", receiver)
                context.startActivity(intent)
            }
        }

        private fun isMatchYearMonth(): Boolean {
            //val monthInt = SimpleDateFormat("MM", Locale.KOREA).format(date).toInt()
            val actualMonth = SimpleDateFormat("MM", Locale.KOREA).format(Date()).toInt()
            val actualYear = SimpleDateFormat("yyyy", Locale.KOREA).format(Date()).toInt()

            return month == actualMonth && year == actualYear
        }

    }
}

