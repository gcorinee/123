package com.example.cs496_pj2_ui.board

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs496_pj2_ui.R
import com.example.cs496_pj2_ui.service.model.Comment
import com.example.cs496_pj2_ui.service.model.DeleteCommentRequest

class BoardCommentsAdapter(val context: Context, val id: String, val boardId: String, val comments: ArrayList<Comment>): RecyclerView.Adapter<BoardCommentsAdapter.CustomViewHolder>() {
    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BoardCommentsAdapter.CustomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.board_comment_row, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardCommentsAdapter.CustomViewHolder, position: Int) {
        holder.name.text = "익명"
        holder.content.text = comments[position].content
        holder.bind(comments[position])
    }

    fun addComment(comment: Comment) {
        comments.add(comment)
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.tv_username_comment)
        val content = itemView.findViewById<TextView>(R.id.tv_comment_content)
        val btnDelete = itemView.findViewById<ImageButton>(R.id.btn_delete_comment)

        fun bind(comment: Comment) {
            if(id == comment.id) {
                btnDelete.visibility = View.VISIBLE
                btnDelete.isEnabled = true

                btnDelete.setOnClickListener {

                }
            } else {
                btnDelete.visibility = View.INVISIBLE
                btnDelete.isEnabled = false
            }

        }
    }
}