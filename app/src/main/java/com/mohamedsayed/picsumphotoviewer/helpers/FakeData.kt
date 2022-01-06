package com.mohamedsayed.picsumphotoviewer.helpers

import com.mohamedsayed.picsumphotoviewer.model.objects.AdObject

object FakeData {
    fun getFakeAd():AdObject{
        return AdObject("Ad PlaceHolder\n${System.currentTimeMillis()}")
    }
}