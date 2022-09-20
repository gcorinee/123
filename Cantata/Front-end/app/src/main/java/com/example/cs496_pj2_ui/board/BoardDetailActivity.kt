package com.example.cs496_pj2_ui.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cs496_pj2_ui.databinding.BoardDetailActivityBinding
import com.example.cs496_pj2_ui.service.RetrofitService
import com.example.cs496_pj2_ui.service.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BoardDetailActivity : AppCompatActivity() {

    private lateinit var binding: BoardDetailActivityBinding
    private lateinit var id: String
    private lateinit var board: Board

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BoardDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id")!!
        board = intent.getParcelableExtra("board")!!

        binding.tvTitleBoardDetail.text = board.title
        binding.tvUserBoardDetail.text = board.username
        binding.tvVotesBoardDetail.text = board.votes?.let {
            it.toString() + " votes"
        } ?: "0 vote"
        binding.tvViewBoardDetail.text = board.views?.let {
            it.toString() + " views"
        } ?: "0 view"
        binding.tvContentBoardDetail.text = board.content ?: ""

        val adapter = BoardCommentsAdapter(this, id, board.boardId, board.comments ?: arrayListOf())
        binding.rvCommentBoardDetail.adapter = adapter
        binding.rvCommentBoardDetail.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.btnSaveComment.setOnClickListener {
            val commentString = binding.etCommentAdd.text.toString()
            if (commentString.isNotEmpty()) {
                val comment = Comment(id, commentString)
                val req = WriteCommentRequest(board.boardId, id, comment)
                val call = RetrofitService.retrofitInterface.writeComment(req)
                call.enqueue(object: Callback<ResponseCode> {
                    override fun onFailure(call: Call<ResponseCode>, t: Throwable) {
                        Log.e(RetrofitService.TAG, t.message!!)
                    }

                    override fun onResponse(
                        call: Call<ResponseCode>,
                        response: Response<ResponseCode>
                    ) {
                        if (response.isSuccessful) {
                           adapter.addComment(comment)
                        }
                    }
                })
            }
        }

        //val board = binding.btnDeleteBoard
        if (id != board.userId) {
            binding.btnDeleteBoard.visibility = View.INVISIBLE
            binding.btnDeleteBoard.isEnabled = false
        } else {
            binding.btnDeleteBoard.visibility = View.VISIBLE
            binding.btnDeleteBoard.isEnabled = true

            binding.btnDeleteBoard.setOnClickListener {
                val req = DeleteBoardRequest(board.boardId, id)
                val call = RetrofitService.retrofitInterface.deleteBoard(req)
                call.enqueue(object: Callback<ResponseCode> {
                    override fun onFailure(call: Call<ResponseCode>, t: Throwable) {
                        Log.e(RetrofitService.TAG, t.message!!)
                    }

                    override fun onResponse(
                        call: Call<ResponseCode>,
                        response: Response<ResponseCode>
                    ) {
                        if (response.isSuccessful) {
                            finish()
                        }
                    }
                })
            }
        }
    }
}