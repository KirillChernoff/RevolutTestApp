package com.revoluttestapp.repository.currencies.impl

import com.revoluttestapp.repository.currencies.ICurrenciesRepository
import com.revoluttestapp.repository.currencies.api.ICurrenciesApi
import com.revoluttestapp.repository.currencies.dto.CurrenciesListDto
import io.reactivex.Single

class CurrenciesRepositoryImpl(
    private val api: ICurrenciesApi
) : ICurrenciesRepository {
    override fun getCurrenciesList(baseCurrency: String): Single<CurrenciesListDto> {
        return api.getCurrenciesList(baseCurrency)
    }
}
