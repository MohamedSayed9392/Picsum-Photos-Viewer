package com.mohamedsayed.picsumphotoviewer.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mohamedsayed.picsumphotoviewer.model.database.dao.ImagesDao
import com.mohamedsayed.picsumphotoviewer.model.objects.PicsumImage

@Database(entities = [PicsumImage::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun imagesDao(): ImagesDao
}