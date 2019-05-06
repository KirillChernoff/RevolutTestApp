package com.revoluttestapp.repository.currencies

import com.revoluttestapp.core.network.RevolutTestApiFactory
import com.revoluttestapp.repository.currencies.api.ICurrenciesApi
import com.revoluttestapp.repository.currencies.impl.CurrenciesRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class CurrenciesRepositoryModule {

    @Provides
    fun provideCurrenciesRepository(apiFactory: RevolutTestApiFactory): ICurrenciesRepository {
        return CurrenciesRepositoryImpl(apiFactory.build<ICurrenciesApi>())
    }
}
