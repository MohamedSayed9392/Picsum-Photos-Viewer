package com.mohamedsayed.picsumphotoviewer.model.network.retrofit

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class LiveDataCallAdapter<T>(private val responseType: Type):
    CallAdapter<T, LiveData<ApiResponse<T>>> {
    override fun adapt(call: Call<T>): LiveData<ApiResponse<T>> {
        return object : LiveData<ApiResponse<T>>() {
            private var isSuccess = false

            override fun onActive() {
                super.onActive()
                if (!isSuccess){
                    postValue(ApiResponse.loading())
                    enqueue()
                }
            }

            override fun onInactive() {
                super.onInactive()
                dequeue()
            }

            private fun dequeue() {
                if (call.isExecuted) {
                    call.cancel()
                }
            }

            private fun enqueue() {
                call.enqueue(object : Callback<T> {
                    override fun onFailure(call: Call<T>, t: Throwable) {
                        postValue(ApiResponse.error())
                    }

                    override fun onResponse(call: Call<T>, response: Response<T>) {
                        if(response.code() == 200 || response.code() == 201) {
                            postValue(ApiResponse.success(response.body()))
                        }else{
                            postValue(ApiResponse.error())
                        }
                        isSuccess = true
                    }
                })
            }
        }
    }

    override fun responseType(): Type = responseType
}