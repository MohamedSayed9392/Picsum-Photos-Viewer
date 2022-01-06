package com.mohamedsayed.picsumphotoviewer.view.imageslist

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mohamedsayed.picsumphotoviewer.R
import com.mohamedsayed.picsumphotoviewer.databinding.LayoutRecyclerBinding
import com.mohamedsayed.picsumphotoviewer.model.network.retrofit.ApiResponse
import com.mohamedsayed.picsumphotoviewer.model.network.retrofit.Status
import com.mohamedsayed.picsumphotoviewer.model.objects.PicsumImage
import com.mohamedsayed.picsumphotoviewer.view.MainActivity
import com.mohamedsayed.picsumphotoviewer.viewmodel.ImagesListVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImagesListFragment : Fragment() {

    private var TAG = "ImagesListFragment"

    val imagesListVM: ImagesListVM by viewModel()
    private var _binding: LayoutRecyclerBinding? = null
    private val binding get() = _binding!!

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

        getImagesList()
    }

    var page = 1
    var observer: Observer<ApiResponse<List<PicsumImage>>>? = null
    private fun getImagesList() {
        observer = Observer<ApiResponse<List<PicsumImage>>> {
            when (it.status) {
                Status.LOADING -> {
                    Log.d(TAG, "Status.LOADING")
                    binding.progressBar.visibility = View.VISIBLE
                    binding.fabRefresh.visibility = View.GONE
                }
                Status.EMPTY -> {
                    Log.d(TAG, "Status.EMPTY")
                    binding.progressBar.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    Log.d(TAG, "Status.SUCCESS")
                    binding.progressBar.visibility = View.GONE
                    setupList(it.data!!)
                }
                Status.ERROR -> {
                    Log.d(TAG, "Status.ERROR")
                    binding.progressBar.visibility = View.GONE
                    binding.fabRefresh.visibility = View.VISIBLE
                    binding.fabRefresh.setOnClickListener { getImagesList() }
                    imagesListVM.getImagesList(page).removeObserver(observer!!)
                }
            }
        }

        imagesListVM.getImagesList(page).observe(viewLifecycleOwner, observer!!)
    }

    private fun setupList(list: List<PicsumImage>) {
        binding.recyclerView.layoutManager = GridLayoutManager(mContext, 2)
        binding.recyclerView.adapter = ImagesListAdapter(mContext!!, list,
        ) { item ->
           Log.d(TAG,"${item.url}")
        }
        binding.recyclerView.visibility = View.VISIBLE
    }

    private var mContext: MainActivity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }
}