package com.example.cs496_pj2_ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.cs496_pj2_ui.databinding.ProfileMonthlyScheduleActivityBinding
import com.example.cs496_pj2_ui.service.model.UserData

class ProfileMonthlyScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ProfileMonthlyScheduleActivityBinding
    private lateinit var sender: UserData
    private lateinit var receiver: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProfileMonthlyScheduleActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sender = intent.getParcelableExtra("sender")!!
        receiver = intent.getParcelableExtra("receiver")!!

        val pager = binding.pagerCalendar
        val adapter = ProfileMonthlyScheduleAdapter(this)
        pager.adapter = adapter
        pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        pager.setCurrentItem(adapter.firstFragmentPosition, false)
    }

    inner class ProfileMonthlyScheduleAdapter(fm: FragmentActivity): FragmentStateAdapter(fm) {
        val firstFragmentPosition = Int.MAX_VALUE / 2

        override fun getItemCount(): Int = Int.MAX_VALUE

        override fun createFragment(position: Int): Fragment {
            var bundle = Bundle()
            bundle.putInt("position", position)
            bundle.putParcelable("sender", sender)
            bundle.putParcelable("receiver", receiver)
            val profileMonthlyScheduleFragment = ProfileMonthlyScheduleFragment()
            profileMonthlyScheduleFragment.arguments = bundle
            return profileMonthlyScheduleFragment
        }
    }
}
