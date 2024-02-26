package com.manish.hotelbookingapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.manish.hotelbookingapp.BuildConfig
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.data.local_database.DatabaseHelper
import com.manish.hotelbookingapp.data.local_database.PreferenceHelper
import com.manish.hotelbookingapp.data.web_server.HotelsApiService
import com.manish.hotelbookingapp.databinding.ActivityMainBinding
import com.manish.hotelbookingapp.ui.fragment.FavoritesFragment
import com.manish.hotelbookingapp.ui.fragment.HomeFragment
import com.manish.hotelbookingapp.ui.fragment.MyBookingsFragment
import com.manish.hotelbookingapp.ui.fragment.ProfileFragment
import com.manish.hotelbookingapp.ui.fragment.SearchResultFragment
import com.manish.hotelbookingapp.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var navigationItems: List<NavigationItem>
    private var searchResultOpened = false
    private var selectedFragment = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PreferenceHelper.initialize(applicationContext)

        // Check User
        if (!viewModel.isAuthenticated()) {
            val welcomeIntent = Intent(this, WelcomeActivity::class.java)
            startActivity(welcomeIntent)
            finish()
        }

        initNavigationBar()

        // Back press
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (searchResultOpened) {
                    searchResultOpened = false
                    selectFragment(HomeFragment())
                } else if (selectedFragment == 0) {
                    finish()
                } else {
                    onClickNavigationBar(0)
                }
            }
        })
    }

    private fun initNavigationBar() {
        navigationItems = listOf(
            NavigationItem(
                binding.imageHome,
                binding.txtHome,
                R.drawable.home,
                R.drawable.home_selected
            ),
            NavigationItem(
                binding.imageFavorites,
                binding.txtFavorites,
                R.drawable.favorites,
                R.drawable.favorites_selected
            ),
            NavigationItem(
                binding.imageBooking,
                binding.txtBooking,
                R.drawable.bookings,
                R.drawable.bookings_selected
            ),
            NavigationItem(
                binding.imageProfile,
                binding.txtProfile,
                R.drawable.profile,
                R.drawable.profile_selected
            )
        )

        // Home
        binding.layoutHome.setOnClickListener {
            onClickNavigationBar(0)
        }
        // Favorites
        binding.layoutFavorites.setOnClickListener {
            onClickNavigationBar(1)
        }
        // Bookings
        binding.layoutBookings.setOnClickListener {
            onClickNavigationBar(2)
        }
        // Profile
        binding.layoutProfile.setOnClickListener {
            onClickNavigationBar(3)
        }
        // Search Result
        viewModel.setNavigationCallback { id ->
            Log.d("TAGH", "Opening search result for $id")
            selectFragment(SearchResultFragment())
            searchResultOpened = true
        }

        onClickNavigationBar(0)
    }

    private fun onClickNavigationBar(index: Int) {
        Log.d("TAGH", "onClickNavigationBar: $index")
        when (index) {
            0 -> selectFragment(HomeFragment())
            1 -> selectFragment(FavoritesFragment())
            2 -> selectFragment(MyBookingsFragment())
            3 -> selectFragment(ProfileFragment())
        }

        navigationItems.forEachIndexed { i, item ->
            item.changeSelection(i == index)
        }
        selectedFragment = index
    }

    private fun selectFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }

    data class NavigationItem(
        val image: ImageView,
        val text: TextView,
        val normalImage: Int,
        val selectedImage: Int
    ) {
        fun changeSelection(isSelected: Boolean) {
            if (isSelected) {
                image.setImageResource(selectedImage)
                text.setTextColor(text.context.getColor(R.color.surface_color_1))
            } else {
                image.setImageResource(normalImage)
                text.setTextColor(text.context.getColor(R.color.txt_navigation_txt))
            }
        }
    }

}