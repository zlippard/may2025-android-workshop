package com.zaclippard.androidworkshopapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zaclippard.androidworkshopapp.R
import com.zaclippard.androidworkshopapp.databinding.FirstWorkshopFragmentBinding
import com.zaclippard.androidworkshopapp.models.Library

class FirstWorkshopFragment : Fragment() {

    private var _binding: FirstWorkshopFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = FirstWorkshopFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FirstWorkshopFragmentBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.button.setOnClickListener {
            findNavController()
                .navigate(
                    FirstWorkshopFragmentDirections
                        .actionFirstWorkshopFragmentToSecondWorkshopFragment(
                            Library.books.first()
                        )
                )
        }
    }

}
