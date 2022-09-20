package com.example.cs496_pj2_ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cs496_pj2_ui.R
import com.example.cs496_pj2_ui.databinding.ProfileMainFragmentBinding
import com.example.cs496_pj2_ui.service.RetrofitService
import com.example.cs496_pj2_ui.service.model.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileMainFragment : Fragment() {

    private lateinit var binding: ProfileMainFragmentBinding

    private lateinit var id: String
    private var friends: ArrayList<String> = arrayListOf()

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: ProfileMainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        id = arguments?.getString("id") as String

        binding = ProfileMainFragmentBinding.inflate(inflater, container, false)

        recyclerView = binding.rvProfile
        recyclerAdapter = ProfileMainAdapter(requireContext(), id)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = recyclerAdapter
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        // Set up profile
        val myInfoCall = RetrofitService.retrofitInterface.getUserById(id)
        myInfoCall.enqueue(object: Callback<UserData> {
            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e(RetrofitService.TAG, t.message + "My Info call")
            }

            override fun onResponse(
                call: Call<UserData>,
                response: Response<UserData>
            ) {
                if (response.body() != null) {
                    val myData = response.body()!!
                    binding.tvMyNameProfile.text = myData.name
                    binding.tvMyStatusProfile.text = myData.status

                    RetrofitService.fetchImg(requireContext(), myData.imgUrl, binding.imgMyProfile)

                    binding.cvMyProfile.setOnClickListener {
                        val intent = Intent(context, ProfileDetailActivity::class.java)
                        intent.putExtra("data", myData)
                        startActivity(intent)
                    }
                }
            }
        })

        // Fetch friends List
        val call = RetrofitService.retrofitInterface.getUserFriends(id)
        call.enqueue(object: Callback<ArrayList<String>> {
            override fun onFailure(call: Call<ArrayList<String>>, t: Throwable) {
                Log.e(RetrofitService.TAG, t.message + "in get user friends")
            }

            override fun onResponse(call: Call<ArrayList<String>>, response: Response<ArrayList<String>>) {
                if (response.body() != null) {
                    val prevFriends = arrayListOf<String>()
                    prevFriends.addAll(friends)

                    friends.addAll(response.body()!!)
                    friends.distinct()

                    binding.tvEmptyProfile.visibility = View.INVISIBLE

                    for (friendID in friends) {
                        if (!prevFriends.contains(friendID)) {
                            val friendDataCall = RetrofitService.retrofitInterface.getUserById(friendID)
                            friendDataCall.enqueue(object: Callback<UserData> {
                                override fun onFailure(call: Call<UserData>, t: Throwable) {
                                    Log.e(RetrofitService.TAG, t.message+"in get user by id")
                                }

                                override fun onResponse(
                                    call: Call<UserData>,
                                    response: Response<UserData>
                                ) {
                                    if (response.body() == null) {
                                        Log.e(RetrofitService.TAG, "get user by id but no user data")
                                    } else {
                                        recyclerAdapter.addFriendItem(response.body()!!)
                                    }
                                }
                            })
                        }
                    }
                } else {
                    binding.tvEmptyProfile.visibility = View.VISIBLE

                    val myDataCall = RetrofitService.retrofitInterface.getUserById(id)
                    myDataCall.enqueue(object: Callback<UserData> {
                        override fun onFailure(call: Call<UserData>, t: Throwable) {
                            Log.e(RetrofitService.TAG, t.message+"in get user by id")
                        }

                        override fun onResponse(
                            call: Call<UserData>,
                            response: Response<UserData>
                        ) {
                            if (response.body() == null) {
                                Log.e(RetrofitService.TAG, "get user by id but no user data")
                            } else {
                                recyclerAdapter.addFriendItem(response.body()!!)
                            }
                        }
                    })
                }
            }
        })
    }

}

