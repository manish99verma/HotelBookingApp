package com.manish.hotelbookingapp.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.data.model.BookedHotel
import com.manish.hotelbookingapp.databinding.FragmentMyBookingsBinding
import com.manish.hotelbookingapp.ui.adapters.BookedHotelsAdapter
import com.manish.hotelbookingapp.ui.viewmodels.MainViewModel


class MyBookingsFragment : Fragment() {
    private lateinit var binding: FragmentMyBookingsBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: BookedHotelsAdapter
    private var bookedHotels: List<BookedHotel> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyBookingsBinding.inflate(layoutInflater, container, false)

        // Time bar
        binding.bookedCard.setOnClickListener {
            refreshTimeBar(false)
        }
        binding.historyCard.setOnClickListener {
            refreshTimeBar(true)
        }

        // back btn
        binding.backBtn.setOnClickListener {
            (context as Activity).onBackPressed()
        }

        // sort
        binding.btnSort.setOnClickListener {
            Toast.makeText(requireContext(), "Available soon!", Toast.LENGTH_SHORT)
                .show()
        }

        // RecyclerView
        adapter = BookedHotelsAdapter()
        binding.rvBookedHotels.layoutManager =
            LinearLayoutManager(requireContext())
        binding.rvBookedHotels.adapter = adapter

        viewModel.bookedHotels.observe(requireActivity()) {
            Log.d("TAGH", "onCreateView: $it")
            setUpRecyclerView(it)
        }


        refreshTimeBar(false)
        return binding.root
    }

    private fun refreshTimeBar(isHistory: Boolean) {
        if (isHistory) {
            binding.bookedCard.setCardBackgroundColor(resources.getColor(R.color.fully_transparent_color))
            binding.historyCard.setCardBackgroundColor(resources.getColor(R.color.white))

            binding.txtBooked.setTextColor(resources.getColor(R.color.grey))
            binding.txtHistory.setTextColor(resources.getColor(R.color.black))

            setUpRecyclerView(listOf())
        } else {
            binding.bookedCard.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.historyCard.setCardBackgroundColor(resources.getColor(R.color.fully_transparent_color))

            binding.txtBooked.setTextColor(resources.getColor(R.color.black))
            binding.txtHistory.setTextColor(resources.getColor(R.color.grey))

            setUpRecyclerView(bookedHotels)
        }
    }

    private fun setUpRecyclerView(list: List<BookedHotel>?) {
        if (list.isNullOrEmpty()) {
            binding.rvBookedHotels.visibility = View.INVISIBLE
            binding.emptyView.visibility = View.VISIBLE
            adapter.setList(listOf())
        } else {
            binding.rvBookedHotels.visibility = View.VISIBLE
            binding.emptyView.visibility = View.INVISIBLE
            adapter.setList(list)
            bookedHotels = list
        }
    }

}