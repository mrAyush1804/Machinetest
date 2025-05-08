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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.machinetest.AdptersAndDataModule.Adpters.UserAdapter
import com.example.machinetest.AdptersAndDataModule.UserRepository
import com.example.machinetest.databinding.FragmentUserlistBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

        // Initialize RecyclerView
        userAdapter = UserAdapter()
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = userAdapter
        }

        // Fetch and display Paging data
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                userRepository.getUsers().collectLatest { pagingData ->
                    Log.d("FragmentUserlist", "Submitting paging data")
                    userAdapter.submitData(pagingData)
                }
            } catch (e: Exception) {
                Log.e("FragmentUserlist", "Error fetching data: ${e.message}", e)

                requireContext().let {
                    Toast.makeText(it, "Failed to load data: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
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