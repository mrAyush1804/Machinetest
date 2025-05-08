// FavoriteFragment.kt
package com.example.machinetest.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.machinetest.AdptersAndDataModule.Adpters.FavoriteUserAdapter
import com.example.machinetest.AdptersAndDataModule.UserRepository
import com.example.machinetest.AdptersAndDataModule.localstorage.AppDatabase
import com.example.machinetest.databinding.FragmentFavFragmnetBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavFragmnetBinding? = null
    private val binding get() = _binding!!
    private lateinit var userAdapter: FavoriteUserAdapter
    private lateinit var userRepository: UserRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavFragmnetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Repository initialize karo
        userRepository = UserRepository(AppDatabase.getDatabase(requireContext()))

        // RecyclerView setup karo
        setupRecyclerView()

        // Favorite users load karo
        loadFavoriteUsers()
    }

    private fun setupRecyclerView() {
        userAdapter = FavoriteUserAdapter { user ->

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {

                    val updatedUser = user.copy(isFavorite = false)
                    userRepository.toggleFavorite(updatedUser)
                }

                loadFavoriteUsers()
            }
        }
        binding.recyclerViewFac.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }
    }

    private fun loadFavoriteUsers() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Loading dikhaye
                binding.progressBar.visibility = View.VISIBLE

                // Room se favorite users fetch karo
                val favoriteUsers = withContext(Dispatchers.IO) {
                    userRepository.getFavoriteUsers()
                }

                // Adapter mein data daalo
                userAdapter.submitList(favoriteUsers)

                // UI update karo
                binding.progressBar.visibility = View.GONE
                binding.recyclerViewFac.visibility = if (favoriteUsers.isNotEmpty()) View.VISIBLE else View.GONE
                binding.errorText.visibility = if (favoriteUsers.isEmpty()) View.VISIBLE else View.GONE
                binding.errorText.text = "Koi favorite user nahi mila."
            } catch (e: Exception) {
                // Error handle karo
                binding.progressBar.visibility = View.GONE
                binding.recyclerViewFac.visibility = View.GONE
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Favorite load nahi hua: ${e.message}"
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}