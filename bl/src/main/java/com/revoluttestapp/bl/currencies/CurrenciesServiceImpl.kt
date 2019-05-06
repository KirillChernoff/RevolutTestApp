package com.revoluttestapp.bl.currencies

import com.revoluttestapp.repository.currencies.ICurrenciesRepository
import io.reactivex.Single
import java.math.BigDecimal
import java.math.RoundingMode

class CurrenciesServiceImpl(
    private val currenciesRepository: ICurrenciesRepository
) : ICurrenciesService {
    private val _baseValue = BigDecimal(1.00)

    //Подумать, как достать base из curListDto

    override fun getCurrencies(baseCurrency: String): Single<Map<String, BigDecimal>> {
        return currenciesRepository.getCurrenciesList(baseCurrency)
            .map { curListDto ->
                curListDto.rates.map {
                    Pair(it.key, convertToDecimal(it.value))
                }
            }
            .map { t: List<Pair<String, BigDecimal>> ->
                val mutableList = t.toMutableList()
                mutableList.add(0, Pair(baseCurrency, _baseValue))
                mutableList.toMap()
            }
    }

    override fun convertToDecimal(number: String): BigDecimal {
        return BigDecimal(number).setScale(2, RoundingMode.HALF_EVEN)
    }
}
