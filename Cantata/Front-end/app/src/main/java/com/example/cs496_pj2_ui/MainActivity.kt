package com.example.cs496_pj2_ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cs496_pj2_ui.board.BoardMainFragment
import com.example.cs496_pj2_ui.databinding.MainActivityBinding
import com.example.cs496_pj2_ui.profile.ProfileMainFragment
import com.example.cs496_pj2_ui.profile.ProfileMonthlyScheduleActivity
import com.example.cs496_pj2_ui.promise.PromiseActivity
import com.example.cs496_pj2_ui.service.RetrofitService
import com.example.cs496_pj2_ui.service.SocketService
import com.example.cs496_pj2_ui.service.model.PromiseRequest
import com.example.cs496_pj2_ui.service.model.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val NUM_PAGES = 2

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar Config
        setSupportActionBar(findViewById(R.id.main_toolbar))
        //supportActionBar?.setDisplayShowTitleEnabled(false)

        id = intent.getStringExtra("id")!!

        // Pager Config
        binding.pagerMain.adapter = PagerAdapter(supportFragmentManager, lifecycle)

        // Navigation Bar Config
        binding.navMain.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.page_profile -> {
                    binding.pagerMain.setCurrentItem(0)
                    true
                }
                R.id.page_board -> {
                    binding.pagerMain.setCurrentItem(1)
                    true
                }
            }
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_promise -> {
                val intent = Intent(this, PromiseActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
                return true
            }
            R.id.action_make_promise -> {
                val call = RetrofitService.retrofitInterface.getUserById(id)
                call.enqueue(object: Callback<UserData> {
                    override fun onFailure(call: Call<UserData>, t: Throwable) {
                        Log.e(RetrofitService.TAG, t.message!!)
                    }

                    override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                        if (response.isSuccessful) {
                            val data = response.body()!!
                            val intent = Intent(baseContext, ProfileMonthlyScheduleActivity::class.java)
                            intent.putExtra("sender", data)
                            intent.putExtra("receiver", data)
                            startActivity(intent)
                        }
                    }
                })
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    inner class PagerAdapter(fm: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fm, lifecycle) {
        override fun createFragment(position: Int): Fragment {
            val bundle: Bundle = Bundle()
            bundle.putString("id", id)

            when (position) {
                0 -> {
                    binding.navMain.selectedItemId = R.id.page_profile
                    val profileMainFragment = ProfileMainFragment()
                    profileMainFragment.arguments = bundle
                    return profileMainFragment
                }

                1 -> {
                    binding.navMain.selectedItemId = R.id.page_board
                    val boardMainFragment = BoardMainFragment()
                    boardMainFragment.arguments = bundle
                    return boardMainFragment
                }

                else -> {
                    val profileMainFragment = ProfileMainFragment()
                    profileMainFragment.arguments = bundle
                    return profileMainFragment
                }
            }
        }

        override fun getItemCount(): Int {
            return NUM_PAGES
        }
    }
}