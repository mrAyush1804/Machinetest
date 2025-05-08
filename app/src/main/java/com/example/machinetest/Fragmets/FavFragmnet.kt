package com.example.machinetest.Fragmets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.machinetest.R
import com.example.machinetest.databinding.FragmentFavFragmnetBinding

class FavFragmnet : Fragment() {

    private var _binding: FragmentFavFragmnetBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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


    }

    override fun onDestroyView() {

        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            FavFragmnet().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"