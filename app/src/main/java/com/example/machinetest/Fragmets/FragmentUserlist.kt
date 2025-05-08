package com.example.machinetest.Fragmets

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.machinetest.AdptersAndDataModule.Adpters.UserAdapter
import com.example.machinetest.AdptersAndDataModule.UserRepository
import com.example.machinetest.databinding.FragmentUserlistBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.paging.LoadState

class FragmentUserlist : Fragment() {

    private var _binding: FragmentUserlistBinding? = null
    private val binding get() = _binding!!
    private lateinit var userAdapter: UserAdapter
    private val userRepository by lazy { UserRepository() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupAdapterLoadState()
        loadData()
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter()
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
                showErrorState("Data load failed: ${error.message}")
            }

         
            val isEmptyList = loadState.source.refresh is LoadState.NotLoading && userAdapter.itemCount == 0
            if (isEmptyList) {
                showEmptyState()
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
                Log.e("FragmentUserlist", "Error fetching data: ${e.message}", e)
                showErrorState("Failed to load data: ${e.message}")
            }
        }
    }

    private fun showDataLoaded() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.errorText.visibility = View.GONE
    }

    private fun showErrorState(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.errorText.visibility = View.VISIBLE
        binding.errorText.text = message
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun showEmptyState() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.errorText.visibility = View.VISIBLE
        binding.errorText.text = "data not found ."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentUserlist().apply {
                arguments = Bundle().apply {
                    putString("param1", param1)
                    putString("param2", param2)
                }
            }
    }
}
