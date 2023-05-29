package com.example.test_android2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.test_android2.databinding.FragmentUploadFileBinding

class UploadFileFragment : Fragment() {

    private var _binding: FragmentUploadFileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUploadFileBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

}