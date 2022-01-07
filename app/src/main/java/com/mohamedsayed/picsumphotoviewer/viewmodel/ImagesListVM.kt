package com.mohamedsayed.picsumphotoviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.mohamedsayed.picsumphotoviewer.model.data.ImagesDataSource
import com.mohamedsayed.picsumphotoviewer.model.database.dao.ImagesDao
import com.mohamedsayed.picsumphotoviewer.model.network.retrofit.ApiService

class ImagesListVM(
    apiService: ApiService,
    private val imagesDao: ImagesDao
) : ViewModel() {

    val imagesList = Pager(PagingConfig(pageSize = 10)) {
        ImagesDataSource(apiService,imagesDao)
    }.flow.cachedIn(viewModelScope)
}