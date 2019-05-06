package com.revoluttestapp.bl.currencies

import io.reactivex.Single
import java.math.BigDecimal

interface ICurrenciesService {
    fun getCurrencies(baseCurrency: String): Single<Map<String, BigDecimal>>
    fun convertToDecimal(number: String): BigDecimal
}
