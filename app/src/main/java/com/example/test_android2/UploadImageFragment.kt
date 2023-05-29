package com.example.test_android2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.test_android2.databinding.FragmentUploadImageBinding

class UploadImageFragment : Fragment() {

    private var _binding: FragmentUploadImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUploadImageBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

}