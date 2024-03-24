package com.manish.hotelbookingapp.ui.activities

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.data.local_database.DatabaseHelper
import com.manish.hotelbookingapp.data.model.BookedHotel
import com.manish.hotelbookingapp.data.model.hotel_search.Property
import com.manish.hotelbookingapp.databinding.ActivityBookingBinding
import com.manish.hotelbookingapp.ui.fragment.SearchResultFragment
import com.manish.hotelbookingapp.ui.models.SearchFragmentUiModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID
import kotlin.math.roundToInt


class BookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingBinding
    private lateinit var property: Property
    private lateinit var bookingDetails: SearchFragmentUiModel
    private val tax = 458
    private var totalAmount = 0

    companion object {
        private const val REQUEST_PAYMENT = 9348
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get Passed Data
        val data = intent.getStringExtra("property")!!
        val type = object : TypeToken<Property>() {}.type
        property = Gson().fromJson(data, type)

        val searchData = intent.getStringExtra("booking_details")!!
        val searchUiType = object : TypeToken<SearchFragmentUiModel>() {}.type
        bookingDetails = Gson().fromJson(searchData, searchUiType)

        // Show to UI
        initUI()

        // back
        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Continue
        binding.btnContinue.setOnClickListener {
            addToBookedList()
            val intent = Intent(this, PaymentSuccessActivity::class.java)
            startActivity(intent)
//            initPaymentApp(binding.txtTotalAmount.text.toString().substring(3).toLong() * 100L)
        }
    }

    private fun addToBookedList() {
        val bookedHotel = BookedHotel(UUID.randomUUID().toString(), property, bookingDetails)

        lifecycleScope.launch(IO) {
            DatabaseHelper.getInstance().addToBookedHotels(bookedHotel)
        }
    }

    private fun initUI() {
        // Image
        Glide.with(this)
            .load(property.propertyImage?.image?.url)
            .placeholder(R.color.place_holder_color)
            .error(R.color.place_holder_color)
            .into(binding.imgThumbnail)

        // Name
        binding.txtDisplayName.text = property.name

        // Location
        binding.txtLocation.text = "${bookingDetails.city},  ${bookingDetails.country}"


        // price
        binding.txtPrice.text = "${property.price?.lead?.formatted}"

        // Booking date
        val currDate = Calendar.getInstance()
        currDate.timeInMillis = System.currentTimeMillis()
        binding.txtBoolingDate.text = formatDate(currDate)

        // check-in date
        binding.txtCheckIn.text = formatDate(bookingDetails.startDate)

        // check out date
        binding.txtCheckOut.text = formatDate(bookingDetails.endDate)

        // guests
        binding.txtGuestsCount.text = bookingDetails.guestsCount.toString()

        // Rooms
        binding.txtRoomsCount.text = bookingDetails.roomsCount.toString()

        // amount
        binding.txtAmountMultiplication.text =
            "Rs ${property.price?.lead?.amount?.roundToInt()} * ${bookingDetails.roomsCount}"

        // tax
        binding.txtTax.text = "Rs $tax"

        // Total
        val price = property.price?.lead?.amount?.roundToInt() ?: 0
        totalAmount = price * bookingDetails.roomsCount + tax

        binding.txtTotalAmount.text = totalAmount.toString()
    }

    private fun formatDate(calendar: Calendar): String {
        return buildString {
            append(calendar.get(Calendar.DAY_OF_MONTH))
            append('-')
            append(SearchResultFragment.monthNames[calendar.get(Calendar.MONTH)])
            append(calendar.get(Calendar.YEAR))
        }
    }

    private fun initPaymentApp(amount: Long) {
        // Build Url
        val baseUri = Uri.parse("https://payment-app.com/")
        val uriBuilder = baseUri.buildUpon()
        uriBuilder.appendQueryParameter("amount", amount.toString())
        val uriWithQuery = uriBuilder.build()

        // Create intent
        val intent = Intent(
            Intent.ACTION_VIEW,
            uriWithQuery
        )

        // Convert the intent to pending intent
        val pendingIntent = TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(
                REQUEST_PAYMENT,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        // Start Payment app
        pendingIntent.send()
    }

}