package com.mohamedsayed.picsumphotoviewer.koin

import com.mohamedsayed.picsumphotoviewer.model.network.retrofit.RetrofitService
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {
    single { RetrofitService.apiService }
}