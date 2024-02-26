package com.manish.hotelbookingapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.manish.hotelbookingapp.databinding.FragmentSearchResultBinding
import com.manish.hotelbookingapp.ui.adapters.HotelSearchResultAdapter
import com.manish.hotelbookingapp.ui.dialogs.FilterDialog
import com.manish.hotelbookingapp.ui.models.FilterResult
import com.manish.hotelbookingapp.ui.models.SearchFragmentUiModel
import com.manish.hotelbookingapp.ui.models.SearchResult
import com.manish.hotelbookingapp.ui.models.Sort
import com.manish.hotelbookingapp.ui.viewmodels.MainViewModel
import com.manish.hotelbookingapp.util.Utils
import java.util.Calendar

class SearchResultFragment : Fragment() {
    private lateinit var binding: FragmentSearchResultBinding
    private var data: SearchFragmentUiModel? = null
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapterRecommended: HotelSearchResultAdapter
    private lateinit var adapterBusiness: HotelSearchResultAdapter
    private lateinit var defaultFilter: FilterResult
    private lateinit var currFilter: FilterResult

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
            append(' ')
            append(monthNames[data?.startDate?.get(Calendar.MONTH) ?: 12])
            append(" - ")
            append(data?.endDate?.get(Calendar.DAY_OF_MONTH))
            append(' ')
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

        binding.rvBuisnessAccomodates.adapter = adapterBusiness
        binding.rvRecommandedHotels.adapter = adapterRecommended

        // Filter Dialog
        defaultFilter = FilterResult(
            Sort.REVIEW, 3, Pair(500, 5000)
        )
        currFilter = defaultFilter

        binding.imgFilters.setOnClickListener {
            val filterDialog = FilterDialog(
                listOf(
                    Sort.REVIEW,
                    Sort.RECOMMENDED,
                    Sort.DISTANCE,
                    Sort.PRICE_LOW_TO_HIGH,
                    Sort.PROPERTY_CLASS,
                    Sort.PRICE_RELEVANT
                ), defaultFilter
            ) { result ->
                currFilter = result
                updateHotels()
                Log.d("TAGH", "onViewCreated: $result")
            }
            filterDialog.show(
                parentFragmentManager, "MyFilterDialogTAG"
            )
        }

        // Notifications
        binding.imgNotifications.setOnClickListener {
            Toast.makeText(context, "This feature will be available soon!", Toast.LENGTH_SHORT)
                .show()
        }

        // Hotels Recycler view
        viewModel.dataRecommendedHotel.observe(context as LifecycleOwner) {
            if (!::adapterRecommended.isInitialized){

            }
                setUpRecyclerView(true, it)
        }
        viewModel.dataBusinessHotels.observe(context as LifecycleOwner) {
            setUpRecyclerView(false, it)
        }
        updateHotels()
    }

    private fun updateHotels() {
        // Recommended
        viewModel.updateHotels(
            cityId = data!!.cityId,
            currFilter,
            data!!.startDate,
            data!!.endDate,
            data!!.guestsCount
        )
    }

    private fun setUpRecyclerView(isRecommended: Boolean, result: SearchResult?) {
        Log.d("TAGH", "setUpRecyclerView: Updated Result")
        result?.let {
            if (it.isLoading) {
                if (isRecommended) {
                    adapterRecommended.setList(null)
                } else {
                    adapterBusiness.setList(null)
                }
            } else if (it.error != null) {
                it.error.consume()?.let { msg ->
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                if (isRecommended) {
                    adapterRecommended.setList(it.result)
                } else {
                    adapterBusiness.setList(it.result)
                }
            }
        }
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