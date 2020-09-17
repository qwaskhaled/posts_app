package com.example.test.ui.postsList

import android.os.Bundle
import android.util.Property
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.test.R
import com.example.test.databinding.CreateEditPostViewBinding
import com.example.test.databinding.FragmentPostsListBinding
import com.example.test.model.Action
import com.example.test.model.Post
import com.example.test.ui.BaseFragment
import com.example.test.util.*
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.ReadOnlyProperty


@AndroidEntryPoint
class PostListFragment : BaseFragment() {
    
    private var _binding: FragmentPostsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PostsListViewModel by viewModels()
    private val createEditViewBinding: CreateEditPostViewBinding by lazy { createEditPostBinding() }
    private val createEditPostDialog: AlertDialog by lazy { createEditPostDialog() }

    private lateinit var postsAdapter: PostsListAdapter
    private var currentAction: Action = Action.NONE
    private var currentPost: Post? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostsListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupFab()
        setupPostsListAdapter()
        setupPostsObserver()
        setupSnackbar()
        setupToast()
        setupProgressbar()
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            showCreateEditDialogForAction(action = Action.CREATE)
        }
    }

    private fun createEditPostDialog(): AlertDialog {
        AlertDialog.Builder(requireContext()).apply {
            setCancelable(true)
            create().apply {
                setView(createEditViewBinding.root)
                return this
            }
        }
    }

    private fun createEditPostBinding(): CreateEditPostViewBinding {
        CreateEditPostViewBinding.inflate(LayoutInflater.from(requireContext())).apply {
            btnSave.setOnClickListener {
              onSaveButtonClick()
            }
            return this
        }
    }

    private fun onSaveButtonClick() {
        createEditPostDialog.dismiss()
        val currentTitle = createEditViewBinding.etTitle.text.toString()

        if (currentAction == Action.CREATE) {
            createPost(Post(id = -1, roomId = -1, name = currentTitle, thumbnail = ""))
        } else {
            editPost(currentPost?.copy(name = currentTitle))
        }

        resetCreateEditPostDialog()
    }

    private fun resetCreateEditPostDialog() {
        createEditViewBinding.etTitle.setText("")
    }

    private fun showCreateEditDialogForAction(action: Action, post: Post? = null) {
        currentAction = action
        post?.let {
            currentPost = it
            createEditViewBinding.etTitle.setText(it.name)
        }

        createEditPostDialog.show()
        createEditViewBinding.etTitle.requestFocus()
        delayExecution(400) {
            showSoftInputFromWindow()
        }
    }

    private fun setupPostsListAdapter() {
        postsAdapter = PostsListAdapter { post, action -> onClick(post, action) }
        with(binding.postsList) {
            adapter = postsAdapter
            addItemDecoration(SpacingItemDecoration(8, 16))
        }
    }

    private fun setupPostsObserver() {
        viewModel.posts.observe(viewLifecycleOwner, Observer {
            postsAdapter.submitList(it)
        })
    }

    private fun setupSnackbar() {
        requireView().setupSnackbar(viewLifecycleOwner, viewModel.snackbarEvent, LENGTH_SHORT)
    }

    private fun setupToast() {
        requireView().setupToast(viewLifecycleOwner, viewModel.toastEvent, LENGTH_SHORT)
    }

    private fun setupProgressbar() {
        viewModel.dataLoading.observe(viewLifecycleOwner, EventObserver {
            binding.progressBar.isVisible = it
        })
    }

    private fun onClick(post: Post? = null, action: Action) {
        when (action) {
            Action.NAVIGATE -> navigateToSinglePost(post)
            Action.DELETE -> deletePost(post)
            Action.EDIT -> showCreateEditDialogForAction(action, post)
        }

    }

    private fun navigateToSinglePost(post: Post?) {
        val action = PostListFragmentDirections
            .actionPostListFragmentToSinglePostFragment(post = post)
        findNavController().safeNavigation(R.id.postListFragment, action)
    }

    private fun createPost(post: Post?) {
        post?.let { viewModel.createPost(post = post) }
    }

    private fun editPost(post: Post?) {
        post?.let { viewModel.editPost(post = post) }
    }

    private fun deletePost(post: Post?) {
        post?.let { viewModel.deletePost(post = post) }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}