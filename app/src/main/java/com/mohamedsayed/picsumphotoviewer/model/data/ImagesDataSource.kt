package com.mohamedsayed.picsumphotoviewer.model.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mohamedsayed.picsumphotoviewer.helpers.FakeData
import com.mohamedsayed.picsumphotoviewer.model.database.dao.ImagesDao
import com.mohamedsayed.picsumphotoviewer.model.network.retrofit.ApiService
import com.mohamedsayed.picsumphotoviewer.model.objects.AdObject
import com.mohamedsayed.picsumphotoviewer.model.objects.PicsumImage

class ImagesDataSource(
    private val apiService: ApiService,
    private val imagesDao: ImagesDao
) : PagingSource<Int, Any>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Any> {
        var response: List<PicsumImage>? = null
        val prevKey: Int?
        val nextKey: Int?

        return try {
            val nextPageNumber = params.key ?: 1
            val dataList: ArrayList<Any> = ArrayList()
            try {
                response = apiService.getImagesList(nextPageNumber, 10)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (response != null) { //data was received from network without any problems
                if (nextPageNumber < 3) imagesDao.saveImages(response)
                prevKey = if (nextPageNumber > 1) nextPageNumber - 1 else null
                nextKey =
                    if (response.isNotEmpty() && response.size == 10) nextPageNumber + 1 else null
            } else { // get data from database
                response = imagesDao.getImages()
                prevKey = null
                nextKey = null
            }

            dataList.addAll(response)

            //adding ads to the list every 5 photos
            var adPlaceHolderIndex = 5
            while (dataList.size >= adPlaceHolderIndex) {
                dataList.add(adPlaceHolderIndex, FakeData.getFakeAd(100))
                adPlaceHolderIndex += 6
            }

            LoadResult.Page(
                data = dataList,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Any>): Int? {
        TODO("Not yet implemented")
    }
}