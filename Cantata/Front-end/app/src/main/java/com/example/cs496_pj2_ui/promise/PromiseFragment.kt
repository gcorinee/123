package com.example.cs496_pj2_ui.promise

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs496_pj2_ui.databinding.PromiseFragmentBinding
import com.example.cs496_pj2_ui.service.RetrofitService
import com.example.cs496_pj2_ui.service.model.PromiseRequest
import com.example.cs496_pj2_ui.service.model.PromiseRequestResponse
import com.example.cs496_pj2_ui.service.model.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PromiseFragment : Fragment() {

    private lateinit var binding: PromiseFragmentBinding

    private lateinit var adapter: PromiseAdapter
    private lateinit var recyclerView: RecyclerView

    private var position = 0
    private lateinit var id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PromiseFragmentBinding.inflate(inflater, container, false)

        id = arguments?.getString("id")!!
        position = arguments?.getInt("position")!!

        when (position) {
            0 -> binding.tvPromiseTitle.text = "받은 요청"
            1 -> binding.tvPromiseTitle.text = "보낸 요청"
        }

        adapter = PromiseAdapter(this.requireContext(), position)
        recyclerView = binding.rvPromise
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        // Fetch Requests
        when(position) {
            0 -> {
                val call = RetrofitService.retrofitInterface.getReceivedRequestById(id)
                call.enqueue(object: Callback<ArrayList<PromiseRequestResponse>> {
                    override fun onFailure(call: Call<ArrayList<PromiseRequestResponse>>, t: Throwable) {
                        Log.e(RetrofitService.TAG, t.message!!)
                    }

                    override fun onResponse(
                        call: Call<ArrayList<PromiseRequestResponse>>,
                        response: Response<ArrayList<PromiseRequestResponse>>
                    ) {
                        if (response.body() != null) {
                            val result = response.body()!!
                            adapter.clear()
                            adapter.updatePromises(result)
                        }
                    }
                })
            }

            1 -> {
                val call = RetrofitService.retrofitInterface.getSentRequestById(id)
                call.enqueue(object: Callback<ArrayList<PromiseRequestResponse>> {
                    override fun onFailure(call: Call<ArrayList<PromiseRequestResponse>>, t: Throwable) {
                        Log.e(RetrofitService.TAG, t.message!!)
                    }

                    override fun onResponse(
                        call: Call<ArrayList<PromiseRequestResponse>>,
                        response: Response<ArrayList<PromiseRequestResponse>>
                    ) {
                        if (response.body() != null) {
                            val result = response.body()!!
                            adapter.clear()
                            adapter.updatePromises(result)
                        }
                    }
                })
            }
        }
    }
}