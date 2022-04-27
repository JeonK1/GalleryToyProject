package com.xemic.lorempicsum.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xemic.lorempicsum.R
import com.xemic.lorempicsum.models.ImageSimple

class ImagePageAdapter(
    private val imagePage:List<ImageSimple>
): RecyclerView.Adapter<ImagePageAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(holder: ViewHolder, view: View, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.item_image_mar_1_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return LayoutInflater.from(parent.context).inflate(R.layout.item_image_mar_1, parent, false).let{ view ->
            ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(imagePage[position].imageUrl)
            .centerCrop()
            .into(holder.image)

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(holder, it, position)
        }
    }

    override fun getItemCount(): Int = imagePage.size
}