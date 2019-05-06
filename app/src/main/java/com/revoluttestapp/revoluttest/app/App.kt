package com.revoluttestapp.revoluttest.app

import androidx.multidex.MultiDexApplication
import com.revoluttestapp.bl.currencies.CurrenciesModule

class App : MultiDexApplication() {
    companion object {
        @JvmStatic
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = buildComponent()
    }

    private fun buildComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .currenciesModule(CurrenciesModule())
            .build()
    }
}
