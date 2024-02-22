package com.manish.hotelbookingapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HeaderViewListAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Orientation
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.data.model.hotel_search.Property
import com.manish.hotelbookingapp.databinding.FragmentSearchResultBinding
import com.manish.hotelbookingapp.ui.adapters.HotelSearchResultAdapter
import com.manish.hotelbookingapp.ui.models.SearchFragmentUiModel
import com.manish.hotelbookingapp.ui.viewmodels.MainViewModel
import com.manish.hotelbookingapp.util.Utils
import okio.Utf8
import java.util.Calendar

class SearchResultFragment : Fragment() {
    private lateinit var binding: FragmentSearchResultBinding
    private var data: SearchFragmentUiModel? = null
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapterRecommended: HotelSearchResultAdapter
    private lateinit var adapterBusiness: HotelSearchResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchResultBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        data = viewModel.searchFragmentState

        // Location
        binding.txtLocationHome.text = buildString {
            append(data?.city)
            append(", ")
            append(data?.country)
        }

        // Date
        binding.txtDateRange.text = buildString {
            append(data?.startDate?.get(Calendar.DAY_OF_MONTH))
            append(monthNames[data?.startDate?.get(Calendar.MONTH) ?: 12])
            append(" - ")
            append(data?.endDate?.get(Calendar.DAY_OF_MONTH))
            append(monthNames[data?.endDate?.get(Calendar.MONTH) ?: 12])
        }

        // Guests
        binding.txtGuests.text = buildString {
            append(data?.guestsCount)
            append(" ")
            append(Utils.singularPulral(data?.guestsCount ?: 0, "Guest", "Guests"))
        }

        // Recycler Views
        adapterRecommended = HotelSearchResultAdapter()
        adapterBusiness = HotelSearchResultAdapter()

        binding.rvBuisnessAccomodates.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecommandedHotels.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvRecommandedHotels.setHasFixedSize(true)
        binding.rvBuisnessAccomodates.setHasFixedSize(true)

        binding.rvBuisnessAccomodates.adapter = adapterBusiness
        binding.rvRecommandedHotels.adapter = adapterRecommended
    }

    private fun setUpRecyclerView() {

    }

    companion object {
        val monthNames =
            arrayOf(
                "JAN",
                "FEB",
                "MAR",
                "APR",
                "MAY",
                "JUN",
                "JULY",
                "AUG",
                "SEP",
                "OCT",
                "NOV",
                "DEC",
                "xxx"
            )
    }
}