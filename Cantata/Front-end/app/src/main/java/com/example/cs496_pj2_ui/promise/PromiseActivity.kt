package com.example.cs496_pj2_ui.promise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cs496_pj2_ui.R
import com.example.cs496_pj2_ui.databinding.PromiseActivityBinding
import com.example.cs496_pj2_ui.databinding.PromiseFragmentBinding
import com.example.cs496_pj2_ui.service.RetrofitService
import com.example.cs496_pj2_ui.service.model.PromiseRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PromiseActivity : AppCompatActivity() {

    private lateinit var binding: PromiseActivityBinding
    private lateinit var adapter: PagerAdapter

    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PromiseActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        id = intent.getStringExtra("id")!!

        // Pager Config
        adapter = PagerAdapter(supportFragmentManager, lifecycle)
        binding.pagerPromise.adapter = adapter
    }

    inner class PagerAdapter(fm: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fm, lifecycle) {
        override fun createFragment(position: Int): Fragment {
            val bundle: Bundle = Bundle()
            bundle.putString("id", id)

            when (position) {
                0 -> {
                    // Receive Promise
                    val promiseFragment = PromiseFragment()
                    bundle.putInt("position", position)
                    promiseFragment.arguments = bundle
                    return promiseFragment
                }

                1 -> {
                    // Sent Promise
                    val promiseFragment = PromiseFragment()
                    bundle.putInt("position", position)
                    promiseFragment.arguments = bundle
                    return promiseFragment
                }

                else -> {
                    val promiseFragment = PromiseFragment()
                    bundle.putInt("position", 0)
                    promiseFragment.arguments = bundle
                    return promiseFragment
                }
            }
        }

        override fun getItemCount(): Int {
            return 2
        }
    }
}