package com.mohamedsayed.picsumphotoviewer

import com.mohamedsayed.picsumphotoviewer.model.objects.PicsumImage
import java.util.concurrent.atomic.AtomicInteger

class FakeDataFactory {
    private val counter = AtomicInteger(0)
    fun generateImage(downloadUrl : String) : PicsumImage {
        val id = counter.incrementAndGet()
        val image = PicsumImage(
            id = "$id",
            author = "author $id",
            downloadUrl = downloadUrl
        )
        return image
    }
}