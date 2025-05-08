package com.example.machinetest.Fragmets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.machinetest.AdptersAndDataModule.Adpters.UnFavoriteAdapter
import com.example.machinetest.AdptersAndDataModule.UserRepository
import com.example.machinetest.AdptersAndDataModule.localstorage.AppDatabase
import com.example.machinetest.AdptersAndDataModule.localstorage.UserEntity
import com.example.machinetest.Navitionmodule.Routedetination
import com.example.machinetest.databinding.FragmentUnFavoriteBinding
import kotlinx.coroutines.launch

class UnFavoriteFragment : Fragment() {
    // Step 1: Binding variables declare karein
    private var _binding: FragmentUnFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: UserRepository
    private lateinit var adapter: UnFavoriteAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Step 2: Binding initialize karein
        _binding = FragmentUnFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = AppDatabase.getDatabase(requireContext()).userDao()
        repository = UserRepository(AppDatabase.getDatabase(requireContext()))

        binding.recyclerViewUnFav.layoutManager = LinearLayoutManager(requireContext())

        loadUnFavoriteUsers()

        // Custom back press handling
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Navigate back
            findNavController().navigate(Routedetination.FragmentUserlist.route)
        }

    }

    override fun onDestroyView() {

        _binding = null
        super.onDestroyView()
    }

    private fun loadUnFavoriteUsers() {
        lifecycleScope.launch {
            val unFavUsers = repository.getUnFavoriteUsers()
            adapter = UnFavoriteAdapter(unFavUsers) { user ->
                toggleFavorite(user)
            }
            binding.recyclerViewUnFav.adapter = adapter
        }
    }

    private fun toggleFavorite(user: UserEntity) {
        lifecycleScope.launch {
            repository.toggleFavorite(user)
            loadUnFavoriteUsers() // Refresh list
        }
    }



}
