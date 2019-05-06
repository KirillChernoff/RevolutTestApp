package com.revoluttestapp.revoluttest.main

import com.revoluttestapp.bl.currencies.ICurrenciesService
import com.revoluttestapp.revoluttest.main.presenter.MainActivityPresenter
import com.revoluttestapp.revoluttest.main.view.MainActivity
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class MainActivityModule {
    @Provides
    fun provideMainActivityPresenter(currenciesService: ICurrenciesService): MainActivityPresenter {
        return MainActivityPresenter(currenciesService)
    }
}

@Subcomponent(modules = [MainActivityModule::class])
interface MainActivityComponent {
    fun inject(target: MainActivity)
}
