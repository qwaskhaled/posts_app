package com.example.test.ui.singlePost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.test.databinding.FragmentSinglePostBinding
import com.example.test.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SinglePostFragment : BaseFragment() {

    private val viewModel: SinglePostViewModel by viewModels()
    private val args: SinglePostFragmentArgs by navArgs()

    private var _binding: FragmentSinglePostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSinglePostBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        args.post?.let {
            binding.tvPostId.text = it.id.toString()
            binding.tvPostTitle.text = it.name
            Glide.with(requireView()).load(it.thumbnail).into(binding.ivThumbnail)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}