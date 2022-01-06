package com.mohamedsayed.picsumphotoviewer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mohamedsayed.picsumphotoviewer.model.network.retrofit.ApiResponse
import com.mohamedsayed.picsumphotoviewer.model.network.retrofit.ApiService
import com.mohamedsayed.picsumphotoviewer.model.objects.PicsumImage

class ImagesListVM (private val apiService: ApiService) : ViewModel() {

    fun getImagesList(page:Int,limit:Int = 10): LiveData<ApiResponse<List<PicsumImage>>> {
        return apiService.getImagesList(page,limit)
    }
}