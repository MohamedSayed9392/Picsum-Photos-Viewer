package com.mohamedsayed.picsumphotoviewer.model.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mohamedsayed.picsumphotoviewer.model.network.retrofit.ApiService
import com.mohamedsayed.picsumphotoviewer.model.objects.PicsumImage

class ImagesDataSource (
    private val apiService: ApiService
) : PagingSource<Int, PicsumImage>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PicsumImage> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = apiService.getImagesList(nextPageNumber,10)
            val dataList = response ?: ArrayList();
            LoadResult.Page(
                data = dataList,
                prevKey = if (nextPageNumber > 1) nextPageNumber - 1 else null,
                nextKey = if (dataList.isNotEmpty()) nextPageNumber + 1 else null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PicsumImage>): Int? {
        TODO("Not yet implemented")
    }
}