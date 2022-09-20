package com.example.cs496_pj2_ui.board

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs496_pj2_ui.R
import com.example.cs496_pj2_ui.databinding.BoardMainFragmentBinding
import com.example.cs496_pj2_ui.profile.ProfileMainAdapter
import com.example.cs496_pj2_ui.service.RetrofitService
import com.example.cs496_pj2_ui.service.model.Board
import com.example.cs496_pj2_ui.service.model.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardMainFragment : Fragment() {

    private lateinit var binding: BoardMainFragmentBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: BoardMainAdapter

    private lateinit var id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        id = arguments?.getString("id")!!

        binding = BoardMainFragmentBinding.inflate(inflater, container, false)

        binding.fabBoardAdd.setOnClickListener {
            val call = RetrofitService.retrofitInterface.getUserById(id)
            call.enqueue(object: Callback<UserData> {
                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    Log.e(RetrofitService.TAG, t.message!!)
                }

                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    if (response.isSuccessful) {
                        val myData = response.body()!!

                        val intent = Intent(context, BoardAddActivity::class.java)
                        intent.putExtra("id", id)
                        intent.putExtra("name", myData.name)
                        startActivity(intent)
                    }
                }
            })
        }

        recyclerView = binding.rvBoardMain
        recyclerAdapter = BoardMainAdapter(requireContext(), id)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = recyclerAdapter
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val call = RetrofitService.retrofitInterface.getBoards()
        call.enqueue(object: Callback<ArrayList<Board>> {
            override fun onFailure(call: Call<ArrayList<Board>>, t: Throwable) {
                Log.e(RetrofitService.TAG, t.message!!)
            }

            override fun onResponse(
                call: Call<ArrayList<Board>>,
                response: Response<ArrayList<Board>>
            ) {
                if (response.body() != null) {
                    binding.tvBoardEmpty.visibility = View.INVISIBLE
                    binding.rvBoardMain.visibility = View.VISIBLE
                    recyclerAdapter.updateBoards(response.body()!!)
                } else {
                    binding.tvBoardEmpty.visibility = View.VISIBLE
                    binding.rvBoardMain.visibility = View.INVISIBLE
                }
            }
        })
    }
}