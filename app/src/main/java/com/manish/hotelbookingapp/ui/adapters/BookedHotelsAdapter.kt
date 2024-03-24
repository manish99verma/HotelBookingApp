package com.manish.hotelbookingapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.data.local_database.DatabaseHelper
import com.manish.hotelbookingapp.data.model.BookedHotel
import com.manish.hotelbookingapp.data.model.hotel_search.Property
import com.manish.hotelbookingapp.databinding.ItemBookedLayoutBinding
import com.manish.hotelbookingapp.databinding.ItemFavoriteHotelBinding
import com.manish.hotelbookingapp.ui.models.SearchFragmentUiModel
import com.manish.hotelbookingapp.util.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.Calendar

class BookedHotelsAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val listDiffer = AsyncListDiffer(this, MyDiffCallback())

    fun setList(list: List<BookedHotel>) {
        listDiffer.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemBookedLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookedHotelViewHolder(binding, parent.context)
    }


    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BookedHotelViewHolder) {
            holder.bind(listDiffer.currentList[position])
        }
    }

    class BookedHotelViewHolder(
        private val binding: ItemBookedLayoutBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookedHotel: BookedHotel) {
            // Thumbnail
            Glide.with(context)
                .load(bookedHotel.property.propertyImage?.image?.url)
                .placeholder(R.color.place_holder_color)
                .into(binding.imgThumbnail)

            // Rating
            val rating = bookedHotel.property.reviews?.score?.div(2.0)
            val count = bookedHotel.property.reviews?.total ?: 0

            binding.txtRating.text = Utils.trimToOneDecimalPoint(rating ?: 3.0).toString()
            binding.txtRatingCount.text = "($count)"

            // Display Name
            binding.txtHotelName.text = bookedHotel.property.name

            // Location
            binding.txtHotelLocation.text =
                "${bookedHotel.bookingDetails.city}, ${bookedHotel.bookingDetails.country}"

            // Price
            binding.txtPrice.text = bookedHotel.property.price!!.lead!!.formatted

            // Check-in
            val checkInDay = bookedHotel.bookingDetails.startDate.get(Calendar.DAY_OF_MONTH)
            val checkInMonthName = monthNames[bookedHotel.bookingDetails.startDate.get(Calendar.MONTH)]
            val checkOutDay = bookedHotel.bookingDetails.endDate.get(Calendar.DAY_OF_MONTH)
            val checkOutMonthName = monthNames[bookedHotel.bookingDetails.endDate.get(Calendar.MONTH)]

            binding.checkInDate.text = "$checkInDay $checkInMonthName"
            binding.checkOutDate.text = "$checkOutDay $checkOutMonthName"

            // Open Hotel
            binding.root.setOnClickListener {
                Utils.openHotelDetailsActivity(
                    context,
                    bookedHotel.property,
                    bookedHotel.bookingDetails
                )
            }
        }
    }

    companion object {
        class MyDiffCallback : DiffUtil.ItemCallback<BookedHotel>() {
            override fun areItemsTheSame(oldItem: BookedHotel, newItem: BookedHotel): Boolean {
                return oldItem.bookingId == newItem.bookingId
            }

            override fun areContentsTheSame(oldItem: BookedHotel, newItem: BookedHotel): Boolean {
                return oldItem == newItem
            }
        }

        val monthNames = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
    }
}