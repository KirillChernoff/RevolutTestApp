package com.revoluttestapp.bl.currencies

import com.revoluttestapp.repository.currencies.CurrenciesRepositoryModule
import com.revoluttestapp.repository.currencies.ICurrenciesRepository
import dagger.Module
import dagger.Provides

@Module(includes = [CurrenciesRepositoryModule::class])
class CurrenciesModule {

    @Provides
    fun provideCurrenciesService(
        currenciesRepository: ICurrenciesRepository
    ): ICurrenciesService {
        return CurrenciesServiceImpl(currenciesRepository)
    }
}
