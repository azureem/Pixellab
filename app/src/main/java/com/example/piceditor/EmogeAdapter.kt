package com.example.piceditor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.piceditor.databinding.EmojiContainerBinding

class EmogeAdapter() : ListAdapter<EmojiData, EmogeAdapter.ViewHolder>(EmogeDifutil) {
    var onclick: ((EmojiData) -> Unit)? = null
    var clickEmoji: ((touchX: Float, touchY: Float, emoji: EmojiData) -> Unit)? = null
    var deleteOnclick: ((Int) -> Unit)? = null

    fun clicking(block: (touchX: Float, touchY: Float, emoji: EmojiData) -> Unit) {
        this.clickEmoji = block
    }

    inner class ViewHolder(private var binding: EmojiContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(emoji: EmojiData) {
            getItem(adapterPosition).apply {
                binding.image.setImageResource(this.image)
                binding.buttonClear.visibility = View.INVISIBLE
            }


            binding.root.setOnClickListener {
                onclick!!.invoke(getItem(adapterPosition))
            }

            binding.root.setOnClickListener { view ->
                val touchX = view.x
                val touchY = view.y
                clickEmoji?.invoke(touchX, touchY, emoji) // Pass emoji and coordinates
            }

            binding.buttonClear.setOnClickListener {
                deleteOnclick!!.invoke(adapterPosition)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            EmojiContainerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object EmogeDifutil : DiffUtil.ItemCallback<EmojiData>() {
    override fun areItemsTheSame(oldItem: EmojiData, newItem: EmojiData): Boolean {
        return oldItem.image == newItem.image
    }

    override fun areContentsTheSame(oldItem: EmojiData, newItem: EmojiData): Boolean {
        return oldItem == newItem
    }

}
