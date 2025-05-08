// FragmentUserlist.kt
package com.example.machinetest.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.paging.LoadState
import com.example.machinetest.AdptersAndDataModule.Adpters.UserAdapter
import com.example.machinetest.AdptersAndDataModule.UserRepository
import com.example.machinetest.AdptersAndDataModule.data.Data

import com.example.machinetest.AdptersAndDataModule.localstorage.AppDatabase
import com.example.machinetest.AdptersAndDataModule.localstorage.UserEntity
import com.example.machinetest.R

import com.example.machinetest.databinding.FragmentUserlistBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentUserlist : Fragment() {

    private var _binding: FragmentUserlistBinding? = null
    private val binding get() = _binding!!
    private lateinit var userAdapter: UserAdapter
    private lateinit var userRepository: UserRepository
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Database aur Repository initialize karo
        database = AppDatabase.getDatabase(requireContext())
        userRepository = UserRepository(database)

        setupRecyclerView()
        setupAdapterLoadState()
        loadData()
        setupNavigation()
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(database) { data ->
            // Heart icon click pe favorite toggle karo
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    // Data ko UserEntity mein convert karo
                    val userEntity = UserEntity(
                        id = data.id,
                        email = data.email,
                        firstName = data.first_name,
                        lastName = data.last_name,
                        avatar = data.avatar,
                        isFavorite = database.userDao().getUserById(data.id)?.isFavorite?.not() ?: true
                    )
                    userRepository.toggleFavorite(userEntity)
                }
                // UI refresh karo
                userAdapter.notifyItemChanged(userAdapter.snapshot().items.indexOf(data))
            }
        }
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = userAdapter
        }
    }

    private fun setupAdapterLoadState() {
        userAdapter.addLoadStateListener { loadState ->
            binding.progressBar.visibility =
                if (loadState.source.refresh is LoadState.Loading) View.VISIBLE else View.GONE

            binding.recyclerView.visibility =
                if (loadState.source.refresh is LoadState.NotLoading && userAdapter.itemCount > 0) View.VISIBLE else View.GONE

            if (loadState.source.refresh is LoadState.Error) {
                val error = (loadState.source.refresh as LoadState.Error).error
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Data load nahi hua: ${error.message}"
                Toast.makeText(requireContext(), "Data load nahi hua: ${error.message}", Toast.LENGTH_LONG).show()
            }

            val isEmptyList = loadState.source.refresh is LoadState.NotLoading && userAdapter.itemCount == 0
            if (isEmptyList) {
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Koi data nahi mila."
            }
        }
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                userRepository.getUsers().collectLatest { pagingData ->
                    userAdapter.submitData(pagingData)
                }
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Data load nahi hua: ${e.message}"
                Toast.makeText(requireContext(), "Data load nahi hua: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupNavigation() {
        binding.btnFavorite.setOnClickListener {
            findNavController().navigate(R.id.nav_host_fragment)
        }
        binding.btnUnfavorite.setOnClickListener {
            Toast.makeText(context, "Unfavorite abhi implement nahi hua", Toast.LENGTH_SHORT).show()
            // Agar UnFavoriteFragment implement karna hai, to yahan navigation add karo
            // findNavController().navigate(R.id.action_userlist_to_unfavorite)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}