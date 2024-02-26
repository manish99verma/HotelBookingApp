package com.manish.hotelbookingapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.data.local_database.DatabaseHelper
import com.manish.hotelbookingapp.data.local_database.PreferenceHelper
import com.manish.hotelbookingapp.data.model.hotel_search.Property
import com.manish.hotelbookingapp.databinding.HotelOverviewItemBinding
import com.manish.hotelbookingapp.databinding.ItemLoadingBinding
import com.manish.hotelbookingapp.util.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class HotelSearchResultAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //    private val listDiffer = AsyncListDiffer(this, MyDiffCallback())
    private var list: List<Property>? = null
    private var isLoading = false
    private var favorites = mutableMapOf<String, Property>()

    init {
        CoroutineScope(IO).launch {
            val temp = DatabaseHelper.getInstance().getFavoritesList()
            temp.forEach {
                favorites[it.id] = it
            }
        }
    }

    fun setList(list: List<Property>?) {
        if (list == null) {
            this.list = null
            setLoading(true)
        } else {
            this.list = list
            setLoading(false)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val size = list?.size ?: 0
        return if (position == size && isLoading)
            LOADING
        else
            ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM) {
            val binding =
                HotelOverviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            HotelViewHolder(binding, parent.context, favorites)
        } else {
            val view =
                ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            object : RecyclerView.ViewHolder(view.root) {}
        }
    }

    private fun setLoading(loading: Boolean) {
        if (isLoading != loading) {
            isLoading = loading
            if (loading) {
                // Show loading indicator
                notifyDataSetChanged()
            } else {
                // Hide loading indicator
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return (list?.size ?: 0) + if (isLoading) 1 else 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HotelViewHolder) {
            list?.get(position)?.let { holder.bind(it) }
        }
    }

    class HotelViewHolder(
        private val binding: HotelOverviewItemBinding,
        private val context: Context,
        private val favorites: MutableMap<String, Property>,
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

            // Favorites
            updateFavoritesIcon(property, false)
            binding.imgFavorite.setOnClickListener {
                updateFavoritesIcon(property, true)
            }

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
        }

        private fun updateFavoritesIcon(property: Property, isClicked: Boolean) {
            var isInFavorites = favorites.contains(property.id)
            if (isClicked)
                isInFavorites = !isInFavorites

            val image = if (isInFavorites)
                R.drawable.ic_favorites_selected
            else
                R.drawable.ic_heart

            Glide.with(context)
                .load(image)
                .into(binding.imgFavorite)

            CoroutineScope(IO).launch {
                if (!isClicked)
                    return@launch

                if (favorites.contains(property.id)) {
                    favorites.remove(property.id)
                    DatabaseHelper.getInstance().removeFromFavorites(property)
                } else {
                    favorites[property.id] = property
                    DatabaseHelper.getInstance().addToFavorites(property)
                }
            }
        }
    }

    companion object {
        private const val ITEM = 0
        private const val LOADING = 1

//        class MyDiffCallback : DiffUtil.ItemCallback<Property>() {
//            override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean {
//                return oldItem.id == newItem.id
//            }
//
//            override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean {
//                return oldItem == newItem
//            }
//        }
    }
}