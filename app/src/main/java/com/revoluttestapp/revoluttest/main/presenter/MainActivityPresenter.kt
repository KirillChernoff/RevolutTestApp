package com.revoluttestapp.revoluttest.main.presenter

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.revoluttestapp.bl.currencies.ICurrenciesService
import com.revoluttestapp.revoluttest.main.view.IMainActivityView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

@InjectViewState
class MainActivityPresenter(private val currenciesService: ICurrenciesService) : MvpPresenter<IMainActivityView>() {

    private val delay = 1L
    private val _baseCur = "EUR"
    private val _baseConversionFactor = BigDecimal(1.00)
    private var currenciesSubscription: Disposable? = null
    private var conversationFactor = _baseConversionFactor
    private var currenrCurrency = _baseCur
    private var needUpdateBase = false

    override fun attachView(view: IMainActivityView?) {
        startWatchingCurrencies(_baseCur)
        super.attachView(view)
    }

    override fun destroyView(view: IMainActivityView?) {
        stopWatchingCurrencies()
        super.destroyView(view)
    }

    private fun startWatchingCurrencies(baseCurrency: String) {
        stopWatchingCurrencies()
        currenciesSubscription = currenciesService.getCurrencies(baseCurrency)
            .repeatWhen { completed -> completed.delay(delay, TimeUnit.SECONDS) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (!it.isNullOrEmpty()) {
                    updateListView(it)
                }
            }, {
                Log.e("MainActivityPresenter", it.message)
            })
    }

    private fun updateListView(currenciesMap: Map<String, BigDecimal>) {
        val convertedMap = currenciesMap
            .map { Pair(it.key, it.value * conversationFactor) }
            .toMap()
        viewState.updateList(convertedMap, needUpdateBase)
        needUpdateBase = false
    }

    private fun stopWatchingCurrencies() {
        currenciesSubscription?.dispose()
        currenciesSubscription = null
    }

    fun startWatchNewBaseCurrency(newBaseCurrency: String?) {
        newBaseCurrency?.let {
            currenrCurrency = it
            needUpdateBase = true
            startWatchingCurrencies(currenrCurrency)
        }
    }

    fun setConversionFactor(multFactor: String) {
        if (!multFactor.isNullOrEmpty()) {
            stopWatchingCurrencies()
            conversationFactor = currenciesService.convertToDecimal(multFactor) / _baseConversionFactor
            startWatchingCurrencies(currenrCurrency)
        }
    }
}
