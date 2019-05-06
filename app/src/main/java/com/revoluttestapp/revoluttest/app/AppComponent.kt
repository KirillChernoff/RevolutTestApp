package com.revoluttestapp.revoluttest.app

import com.revoluttestapp.bl.currencies.CurrenciesModule
import com.revoluttestapp.revoluttest.main.MainActivityComponent
import com.revoluttestapp.revoluttest.main.MainActivityModule
import dagger.Component
import di.CoreModule
import javax.inject.Singleton

@Singleton
@Component(modules = [CoreModule::class, CurrenciesModule::class, MainActivityModule::class])
interface AppComponent {

    fun mainActivityComponent(): MainActivityComponent
}
