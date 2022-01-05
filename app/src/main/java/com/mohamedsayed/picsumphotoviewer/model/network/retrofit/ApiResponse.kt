package com.mohamedsayed.picsumphotoviewer.model.network.retrofit

data class ApiResponse<out T>(var status: Status, val data: T? = null) {
    companion object {
        fun <T> success(data: T?): ApiResponse<T> {
            return ApiResponse(
                Status.SUCCESS,
                data
            )
        }

        fun <T> error(): ApiResponse<T> {
            return ApiResponse(
                Status.ERROR
            )
        }

        fun <T> loading(): ApiResponse<T> {
            return ApiResponse(
                Status.LOADING
            )
        }

        fun <T> empty(): ApiResponse<T> {
            return ApiResponse(
                Status.EMPTY
            )
        }
    }
}