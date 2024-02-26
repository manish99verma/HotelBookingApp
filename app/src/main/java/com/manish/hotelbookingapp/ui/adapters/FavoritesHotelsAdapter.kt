package com.manish.hotelbookingapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.data.local_database.DatabaseHelper
import com.manish.hotelbookingapp.data.local_database.PreferenceHelper
import com.manish.hotelbookingapp.data.model.hotel_search.Property
import com.manish.hotelbookingapp.databinding.HotelOverviewItemBinding
import com.manish.hotelbookingapp.databinding.ItemFavoriteHotelBinding
import com.manish.hotelbookingapp.databinding.ItemLoadingBinding
import com.manish.hotelbookingapp.util.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class  FavoritesHotelsAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val listDiffer = AsyncListDiffer(this, MyDiffCallback())

    fun setList(list: List<Property>) {
        listDiffer.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemFavoriteHotelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding, parent.context)
    }


    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FavoriteViewHolder) {
             holder.bind(listDiffer.currentList[position])
        }
    }

    class FavoriteViewHolder(
        private val binding: ItemFavoriteHotelBinding,
        private val context: Context,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(property: Property) {
            // Thumbnail
            Glide.with(context)
                .load(property.propertyImage?.image?.url)
                .placeholder(R.color.place_holder_color)
                .into(binding.imgThumbnail)

            // Offer
            val offerMsg = property.offerBadge?.secondary?.text
            if (offerMsg != null) {
                val percentIndex = offerMsg.indexOf('%')
                var i = percentIndex - 1
                while (i >= 0 && offerMsg[i].isDigit())
                    i--
                if (i < 0 || percentIndex >= offerMsg.length)
                    binding.txtOffer.text = context.getString(R.string.no_offer)
                else
                    binding.txtOffer.text = buildString {
                        append(offerMsg.substring(i + 1, percentIndex + 1).uppercase())
                        append(" OFF")
                    }
            } else {
                binding.txtOffer.text = context.getString(R.string.no_offer)
            }

            // Rating
            val rating = property.reviews?.score?.div(2.0)
            binding.txtRating.text = Utils.trimToOneDecimalPoint(rating ?: 3.0).toString()

            // Display Name
            binding.txtDisplayName.text = property.name

            // Location
            val distance = property.destinationInfo?.distanceFromDestination?.value ?: 1
            binding.txtLocation.text = buildString {
                append(distance)
                append(" KM From ")
                append("City Center")
            }

            // Price
            binding.txtPriceRange.text = buildString {
                append(property.price?.lead?.formatted)
                property.price?.strikeOut?.formatted?.let {
                    append(" - ")
                    append(it)
                }
            }

            // Remove from favorites
            binding.root.setOnLongClickListener {
                CoroutineScope(IO).launch {
                    DatabaseHelper.getInstance().removeFromFavorites(property)
                }
                true
            }
        }
    }

    companion object {
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