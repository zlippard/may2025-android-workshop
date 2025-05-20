package com.zaclippard.androidworkshopapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zaclippard.androidworkshopapp.BookCard
import com.zaclippard.androidworkshopapp.R
import com.zaclippard.androidworkshopapp.databinding.SecondWorkshopFragmentBinding
import com.zaclippard.androidworkshopapp.ui.theme.AndroidWorkshopAppTheme

class SecondWorkshopFragment : Fragment() {

    private var _binding: SecondWorkshopFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val args by navArgs<SecondWorkshopFragmentArgs>()

    companion object {
        fun newInstance() = SecondWorkshopFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SecondWorkshopFragmentBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.composeView.setContent {
            AndroidWorkshopAppTheme {
                BookCard(args.book) {
                    findNavController().navigateUp()
                }
            }
        }
    }

}
