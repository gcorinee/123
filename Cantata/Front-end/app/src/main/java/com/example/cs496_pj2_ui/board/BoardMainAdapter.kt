package com.example.cs496_pj2_ui.board

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cs496_pj2_ui.R
import com.example.cs496_pj2_ui.profile.ProfileDetailActivity
import com.example.cs496_pj2_ui.profile.ProfileMainAdapter
import com.example.cs496_pj2_ui.service.RetrofitService
import com.example.cs496_pj2_ui.service.model.Board
import com.example.cs496_pj2_ui.service.model.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardMainAdapter(val context: Context, val id: String): RecyclerView.Adapter<BoardMainAdapter.CustomViewHolder>() {

    var boards: ArrayList<Board> = arrayListOf()

    override fun getItemCount(): Int {
        return boards.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BoardMainAdapter.CustomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.board_row, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardMainAdapter.CustomViewHolder, position: Int) {
        holder.username.text = boards[position].username
        holder.title.text = boards[position].title
        holder.votes.text = boards[position].votes.toString() + "votes"
        holder.views.text = boards[position].views.toString() + "views"
        holder.comments.text = boards[position].comments?.let {
            it.size.toString() + "comment"
        } ?: "0 comment"

        holder.bind(boards[position])
    }

    fun updateBoards(boards: ArrayList<Board>) {
        this.boards = boards
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val username = itemView.findViewById<TextView>(R.id.tv_user_board)!!
        val title = itemView.findViewById<TextView>(R.id.tv_title_board)!!
        val votes = itemView.findViewById<TextView>(R.id.num_votes_board)!!
        val views = itemView.findViewById<TextView>(R.id.num_view_board)!!
        val comments = itemView.findViewById<TextView>(R.id.num_comment_board)!!

        fun bind(board: Board) {
            itemView.setOnClickListener {
                val call = RetrofitService.retrofitInterface.findBoardById(board.boardId)
                call.enqueue(object: Callback<Board> {
                    override fun onFailure(call: Call<Board>, t: Throwable) {
                        Log.e(RetrofitService.TAG, t.message!!)
                    }

                    override fun onResponse(call: Call<Board>, response: Response<Board>) {
                        if (response.isSuccessful) {
                            val board = response.body()!!
                            val intent = Intent(context, BoardDetailActivity::class.java)
                            intent.putExtra("id", id)
                            intent.putExtra("board", board)
                            context.startActivity(intent)
                        }
                    }
                })
            }
        }
    }

}