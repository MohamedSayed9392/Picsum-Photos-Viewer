package com.mohamedsayed.picsumphotoviewer.koin

import androidx.room.Room
import com.mohamedsayed.picsumphotoviewer.model.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val dbModule: Module = module {
    single { Room.databaseBuilder(androidContext(), AppDatabase::class.java, "tmdb")
        .fallbackToDestructiveMigration().build() }
    single { get<AppDatabase>().imagesDao() }
}