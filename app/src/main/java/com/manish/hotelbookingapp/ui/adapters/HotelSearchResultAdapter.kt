package com.manish.hotelbookingapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.data.local_database.PreferenceHelper
import com.manish.hotelbookingapp.data.model.hotel_search.Property
import com.manish.hotelbookingapp.databinding.HotelOverviewItemBinding
import com.manish.hotelbookingapp.databinding.ItemLoadingBinding
import com.manish.hotelbookingapp.util.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class HotelSearchResultAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val favorites = PreferenceHelper.getFavorites() ?: mutableSetOf()
    private val listDiffer = AsyncListDiffer(this, MyDiffCallback())
    private var isLoading = true

    fun setList(list: List<Property>?) {
        if (list.isNullOrEmpty()) {
            listDiffer.submitList(listOf())
            isLoading = true
        } else {
            isLoading = false
            listDiffer.submitList(list)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == HOTEL_VIEW_TYPE) {
            val binding =
                HotelOverviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return HotelViewHolder(binding, parent.context, favorites)
        }

        val binding =
            ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return object : RecyclerView.ViewHolder(binding.root) {}
    }

    override fun getItemCount(): Int {
        return if (isLoading)
            1
        else
            listDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HotelViewHolder)
            holder.bind(listDiffer.currentList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading)
            LOADING_VIEW_TYPE
        else
            HOTEL_VIEW_TYPE
    }

    class HotelViewHolder(
        private val binding: HotelOverviewItemBinding,
        private val context: Context,
        private val favorites: MutableSet<String>
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(property: Property) {
            // Thumbnail
            Glide.with(context)
                .load(property.propertyImage.image.url)
                .placeholder(R.color.place_holder_color)
                .into(binding.imgThumbnail)

            // Offer
            val offerMsg = property.offerBadge.secondary.text ?: "No Offer"
            binding.txtOffer.text = offerMsg.uppercase()

            // Rating
            val rating = property.reviews.score / 2.0
            binding.txtRating.text = Utils.trimToOneDecimalPoint(rating).toString()

            // Favorites
            updateFavoritesIcon(property, false)
            binding.imgFavorite.setOnClickListener {
                updateFavoritesIcon(property, true)
            }

            // Display Name
            binding.txtDisplayName.text = property.name

            // Location
            binding.txtLocation.text = property.destinationInfo.distanceFromMessaging

            // Price
            binding.txtPriceRange.text = buildString {
                append(property.price.lead.formatted)
                append(" - ")
                append(property.price.strikeOut.formatted)
            }
        }

        fun updateFavoritesIcon(property: Property, isClicked: Boolean) {
            if (isClicked) {
                if (favorites.contains(property.id))
                    favorites.remove(property.id)
                else
                    favorites.add(property.id)

                CoroutineScope(IO).launch {
                    PreferenceHelper.updateFavorites(favorites)
                }
            }

            val img = if (favorites.contains(property.id))
                R.drawable.favorites_selected
            else
                R.drawable.ic_heart

            Glide.with(context)
                .load(img)
                .into(binding.imgFavorite)
        }
    }

    companion object {
        private const val LOADING_VIEW_TYPE = 0
        private const val HOTEL_VIEW_TYPE = 1

        class MyDiffCallback : DiffUtil.ItemCallback<Property>() {
            override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean {
                return oldItem == newItem
            }
        }
    }
}