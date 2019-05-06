package com.revoluttestapp.core.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RevolutTestApiFactory(
    okHttpClient: OkHttpClient
) : ApiFactory() {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://revolut.duckdns.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxAdapterFactory.create())
        .client(okHttpClient)
        .build()

    override fun <T> create(api: Class<T>): T {
        return retrofit.create(api)
    }
}
