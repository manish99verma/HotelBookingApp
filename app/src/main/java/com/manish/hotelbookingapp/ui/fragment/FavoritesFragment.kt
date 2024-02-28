package com.manish.hotelbookingapp.ui.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.data.local_database.DatabaseHelper
import com.manish.hotelbookingapp.databinding.FragmentFavoritesBinding
import com.manish.hotelbookingapp.ui.adapters.FavoritesHotelsAdapter
import com.manish.hotelbookingapp.ui.adapters.HotelSearchResultAdapter
import com.manish.hotelbookingapp.ui.viewmodels.MainViewModel

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: FavoritesHotelsAdapter
    private lateinit var context: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recycler view
        adapter = FavoritesHotelsAdapter()

        binding.rvFavories.layoutManager =
            GridLayoutManager(context, 2)

        binding.rvFavories.adapter = adapter

        DatabaseHelper.getInstance().getFavoritesListAsync().observe(context as LifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.rvFavories.visibility = View.INVISIBLE
                binding.emptyView.visibility = View.VISIBLE
            } else {
                binding.rvFavories.visibility = View.VISIBLE
                binding.emptyView.visibility = View.INVISIBLE
            }

            adapter.setList(it)
        }

        // Back
        binding.backBtn.setOnClickListener {
            (context as Activity).onBackPressed()
        }

        binding.btnSort.setOnClickListener {
            Toast.makeText(context, "Available soon!", Toast.LENGTH_SHORT)
                .show()
        }
        binding.btnFilter.setOnClickListener {
            Toast.makeText(context, "Available soon!", Toast.LENGTH_SHORT)
                .show()
        }
    }

}