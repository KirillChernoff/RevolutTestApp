package com.revoluttestapp.repository.currencies

import com.revoluttestapp.repository.currencies.dto.CurrenciesListDto
import io.reactivex.Single

interface ICurrenciesRepository {
    fun getCurrenciesList(baseCurrency: String): Single<CurrenciesListDto>
}
