package com.mohamedsayed.picsumphotoviewer.model.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.mohamedsayed.picsumphotoviewer.model.objects.PicsumImage

@Dao
interface ImagesDao {
    @Insert(onConflict = REPLACE)
    suspend fun saveImages(redditPosts: List<PicsumImage>)

    @Query("SELECT * FROM images")
    suspend fun getImages(): List<PicsumImage>
}