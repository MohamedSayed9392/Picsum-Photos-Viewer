package com.mohamedsayed.picsumphotoviewer.helpers

import com.mohamedsayed.picsumphotoviewer.model.objects.AdObject

object FakeData {
    fun getFakeAd(id: Int? = null): AdObject {
        return AdObject("Ad PlaceHolder\n${id ?: System.currentTimeMillis()}")
    }
}