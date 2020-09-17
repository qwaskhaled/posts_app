package com.example.test.ui.postsList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test.databinding.PostItemBinding
import com.example.test.model.Action
import com.example.test.model.Post
import com.example.test.ui.postsList.PostsListAdapter.ViewHolder


class PostsListAdapter(
    private val onClick: (post: Post?, action: Action) -> Unit
) : PagedListAdapter<Post, ViewHolder>(PostCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post, onClick)
    }

    class ViewHolder(private val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post?, onClick: (post: Post?, action: Action) -> Unit) {
            with(binding) {
                tvTitle.text = post?.name?.let { if (it.length > 20) it.substring(0, 20) else it }
                Glide.with(root.context).load(post?.thumbnail).into(ivThumbnail)
                cvPost.setOnClickListener { onClick.invoke(post, Action.NAVIGATE) }
                cvEdit.setOnClickListener { onClick.invoke(post, Action.EDIT) }
                cvDelete.setOnClickListener { onClick.invoke(post, Action.DELETE) }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PostItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class PostCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.roomId == newItem.roomId
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}