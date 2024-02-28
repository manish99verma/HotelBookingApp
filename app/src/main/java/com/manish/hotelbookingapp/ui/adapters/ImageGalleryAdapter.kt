package com.manish.hotelbookingapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.data.model.hotel_search.PropertyImage

class ImageGalleryAdapter(private val context: Context, private val images: List<PropertyImage>) :
    RecyclerView.Adapter<ImageGalleryAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.img_gallery)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.image_gallery_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val url = images[position].image?.url
        Glide.with(context)
            .load(url)
            .placeholder(R.color.place_holder_color)
            .error(R.color.place_holder_color)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return images.size
    }
}