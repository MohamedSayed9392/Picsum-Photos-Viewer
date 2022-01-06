package com.mohamedsayed.picsumphotoviewer.model.network.retrofit

import androidx.lifecycle.LiveData
import com.mohamedsayed.picsumphotoviewer.helpers.Q
import com.mohamedsayed.picsumphotoviewer.model.objects.PicsumImage
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(Q.API_LIST_IMAGES)
    fun getImagesList(@Query("page") page:Int,@Query("limit") limit:Int): LiveData<ApiResponse<List<PicsumImage>>>
}