package com.xemic.lorempicsum.ui.main.adapter

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.xemic.lorempicsum.R
import com.xemic.lorempicsum.models.ImagePage

class ImageListViewPagerAdapter(
    private val imagePages: List<ImagePage>
): RecyclerView.Adapter<ImageListViewPagerAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(holder: ImagePageAdapter.ViewHolder, view: View, position: Int, page: Int)
    }

    var pageItemClickListener: OnItemClickListener? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val recyclerView = itemView.findViewById<RecyclerView>(R.id.item_page_main_recycler_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return LayoutInflater.from(parent.context).inflate(R.layout.item_page_main, parent, false).let{ view ->
            ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, page: Int) {
        holder.recyclerView.adapter = ImagePageAdapter(imagePages[page].imageList).apply {
            itemClickListener = object : ImagePageAdapter.OnItemClickListener {
                override fun onItemClick(
                    holder: ImagePageAdapter.ViewHolder,
                    view: View,
                    position: Int
                ) {
                    pageItemClickListener?.onItemClick(holder, view, position, page)
                }
            }
        }
    }

    override fun getItemCount(): Int = imagePages.size
}