package com.mohamedsayed.picsumphotoviewer.view.imageslist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mohamedsayed.picsumphotoviewer.databinding.LayoutRecyclerBinding
import com.mohamedsayed.picsumphotoviewer.view.MainActivity
import com.mohamedsayed.picsumphotoviewer.viewmodel.ImagesListVM
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImagesListFragment : Fragment() {

    private var TAG = "ImagesListFragment"

    private val imagesListVM: ImagesListVM by viewModel()
    private var _binding: LayoutRecyclerBinding? = null
    private val binding get() = _binding!!
    private lateinit var imagesListAdapter: ImagesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imagesListAdapter = ImagesListAdapter(
            mContext!!,
        ) { item ->
            Log.d(TAG, "${item?.url}")
        }

        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = imagesListAdapter.withLoadStateHeaderAndFooter(
            header = ImagesLoadStateAdapter { imagesListAdapter.retry() },
            footer = ImagesLoadStateAdapter { imagesListAdapter.retry() }
        )
        binding.recyclerView.visibility = View.VISIBLE

        lifecycleScope.launch {
            imagesListVM.imagesList.collectLatest { pagedData ->
                imagesListAdapter.submitData(pagedData)
            }
        }
    }

    private var mContext: MainActivity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }
}