package com.mohamedsayed.picsumphotoviewer

import android.app.Application
import com.mohamedsayed.picsumphotoviewer.koin.appModule
import com.mohamedsayed.picsumphotoviewer.koin.dbModule
import com.mohamedsayed.picsumphotoviewer.koin.viewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module

class PicsumApplication : Application() {

    companion object {
        lateinit var instance: PicsumApplication
    }


    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            modules(getModules())
            androidContext(instance)
        }
    }

    private fun getModules(): List<Module> {
        return listOf(viewModel, appModule, dbModule)
    }

}