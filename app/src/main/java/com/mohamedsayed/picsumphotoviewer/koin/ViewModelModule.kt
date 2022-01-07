package com.mohamedsayed.picsumphotoviewer.koin

import com.mohamedsayed.picsumphotoviewer.viewmodel.ImagesListVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModel = module {
    viewModel {
        ImagesListVM(get(),get())
    }
}