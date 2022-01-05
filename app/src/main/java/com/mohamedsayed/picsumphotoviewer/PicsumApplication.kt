package com.mohamedsayed.picsumphotoviewer

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class PicsumApplication : Application() {

    companion object {
        lateinit var instance: PicsumApplication
    }


    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            androidLogger()
            androidContext(instance)
            modules(getModules())
        }
    }

    private fun getModules(): List<Module> {
        return ArrayList()
    }

}