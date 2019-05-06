package com.revoluttestapp.revoluttest.main.view

import com.arellomobile.mvp.MvpView
import java.math.BigDecimal

interface IMainActivityView : MvpView {

    fun updateList(currencies: Map<String, BigDecimal>, needUpdateBase: Boolean)
}
