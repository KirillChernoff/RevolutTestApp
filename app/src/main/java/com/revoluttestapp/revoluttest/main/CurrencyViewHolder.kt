package com.revoluttestapp.revoluttest.main

import android.view.View
import android.widget.EditText
import com.mikepenz.fastadapter.FastAdapter
import kotlinx.android.synthetic.main.currency_item.view.*

class CurrencyViewHolder(itemView: View) : FastAdapter.ViewHolder<CurrencyItem>(itemView) {
    private var currencyName = itemView.currencyName
    var currencyEditText: EditText = itemView.currencyValue

    override fun unbindView(item: CurrencyItem) {
        currencyName.text = ""
        currencyEditText.text.clear()
        currencyEditText.text.append((0.00).toString())
    }

    override fun bindView(item: CurrencyItem, payloads: MutableList<Any>) {
        currencyName.text = item.curName
        currencyEditText.text.clear()
        currencyEditText.text.append(item.curValue.toString())
    }
}
