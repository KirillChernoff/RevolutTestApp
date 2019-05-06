package com.revoluttestapp.repository.currencies.api

import com.revoluttestapp.repository.currencies.dto.CurrenciesListDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ICurrenciesApi {

    @GET("/latest")
    fun getCurrenciesList(@Query("base") baseCurrency: String): Single<CurrenciesListDto>
}
