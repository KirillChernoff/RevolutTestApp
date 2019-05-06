package com.revoluttestapp.revoluttest.main

import android.view.View
import com.mikepenz.fastadapter.items.AbstractItem
import com.revoluttestapp.revoluttest.R
import java.math.BigDecimal

class CurrencyItem(val curName: String, var curValue: BigDecimal) : AbstractItem<CurrencyItem, CurrencyViewHolder>() {
    override fun getType() = R.id.currencyItemLayout

    override fun getViewHolder(v: View) = CurrencyViewHolder(v)

    override fun getLayoutRes() = R.layout.currency_item
}
