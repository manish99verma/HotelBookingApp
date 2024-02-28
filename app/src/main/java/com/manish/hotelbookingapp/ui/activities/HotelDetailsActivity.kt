package com.manish.hotelbookingapp.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.data.local_database.DatabaseHelper
import com.manish.hotelbookingapp.data.model.hotel_details.HotelDetailsResult
import com.manish.hotelbookingapp.data.model.hotel_search.Property
import com.manish.hotelbookingapp.databinding.ActivityHotelDetailsBinding
import com.manish.hotelbookingapp.ui.adapters.ImageGalleryAdapter
import com.manish.hotelbookingapp.ui.viewmodels.HotelDetailsViewModel
import com.manish.hotelbookingapp.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HotelDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHotelDetailsBinding
    private val viewModel: HotelDetailsViewModel by viewModels()
    private lateinit var property: Property
    private lateinit var imageGalleryAdapter: ImageGalleryAdapter
    private var favorites = mutableMapOf<String, Property>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getStringExtra("property")!!
        val type = object : TypeToken<Property>() {}.type
        property = Gson().fromJson(data, type)

        // Fetch Data
        viewModel.hotelDetailsData.observe(this) {
            manageProgressBar(it.isLoading)

            if (it.data != null)
                convertDataToUI(it.data)
            if (it.error != null) {
                val error = it.error.consume()
                Toast.makeText(this, error, Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
        viewModel.getHotelDetails(property.id)

        // Back
        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Share
        binding.shareBtn.setOnClickListener {
            Toast.makeText(this, "Available Soon!", Toast.LENGTH_SHORT)
                .show()
        }

        // Favorites
        CoroutineScope(Dispatchers.IO).launch {
            val temp = DatabaseHelper.getInstance().getFavoritesList()
            temp.forEach {
                favorites[it.id] = it
            }

            withContext(Dispatchers.Main) {
                updateFavoritesIcon(property, false)
            }
        }
        binding.heartBtn.setOnClickListener {
            updateFavoritesIcon(property, true)
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

        Glide.with(this)
            .load(image)
            .into(binding.heartBtn)

        CoroutineScope(Dispatchers.IO).launch {
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

    private fun manageProgressBar(visible: Boolean) {
        if (visible) {
            binding.loadingView.visibility = View.VISIBLE
            binding.mainLayout.visibility = View.GONE
            binding.bottomLayout.visibility = View.GONE
        } else {
            binding.loadingView.visibility = View.GONE
            binding.mainLayout.visibility = View.VISIBLE
            binding.bottomLayout.visibility = View.VISIBLE
        }
    }

    private fun convertDataToUI(data: HotelDetailsResult) {
        // Thumbnail
        Glide.with(this)
            .load(property.propertyImage?.image?.url)
            .placeholder(R.color.place_holder_color)
            .into(binding.imgThumbnail)

        // Hotel Name
        val name = property.name ?: "Hotel Name"
        binding.txtDisplayName.text = name

        // Offer
        val offerMsg = property.offerBadge?.secondary?.text
        if (offerMsg != null) {
            val percentIndex = offerMsg.indexOf('%')
            var i = percentIndex - 1
            while (i >= 0 && offerMsg[i].isDigit())
                i--
            if (i < 0 || percentIndex >= offerMsg.length)
                binding.txtOffer.text = getString(R.string.no_offer)
            else
                binding.txtOffer.text = buildString {
                    append(offerMsg.substring(i + 1, percentIndex + 1).uppercase())
                    append(" OFF")
                }
        } else {
            binding.txtOffer.text = getString(R.string.no_offer)
        }

        // Review
        val rating = property.reviews?.score?.div(2.0)
        val ratingTrimmed = Utils.trimToOneDecimalPoint(rating ?: 3.0).toString()
        val count = property.reviews?.total ?: 0
        val msg = "$ratingTrimmed ($count)"
        binding.txtRating.text = String.format(getString(R.string.underline_text_format), msg)

        // Location
        binding.txtLocation.text = data.summary.location.address.addressLine

        // Description
        val descriptionMsg: String = try {
            data.propertyContentSectionGroups.aboutThisProperty.sections[0]
                .bodySubSections[0].elements[0].items[0].content?.text!!
        } catch (e: Exception) {
            "No Information Available about this hotel!"
        }
        binding.txtDescription.text = descriptionMsg

        // Price
        binding.txtPrice.text = property.price?.lead?.formatted ?: "Rs1000"

        // Gallery
        data.propertyGallery.images.forEach {
            Log.d("TAGH", "image: ${it.image?.url}")
        }
        imageGalleryAdapter = ImageGalleryAdapter(this, data.propertyGallery.images)
        binding.viewPagerGallery.adapter = imageGalleryAdapter
    }

}