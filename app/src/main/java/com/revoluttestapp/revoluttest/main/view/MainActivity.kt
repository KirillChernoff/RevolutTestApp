package com.revoluttestapp.revoluttest.main.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.EditText
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.mikepenz.fastadapter_extensions.drag.ItemTouchCallback
import com.mikepenz.fastadapter_extensions.drag.SimpleDragCallback
import com.revoluttestapp.revoluttest.R
import com.revoluttestapp.revoluttest.app.App
import com.revoluttestapp.revoluttest.main.CurrencyItem
import com.revoluttestapp.revoluttest.main.CurrencyViewHolder
import com.revoluttestapp.revoluttest.main.presenter.MainActivityPresenter
import com.revoluttestapp.revoluttest.main.utils.CustomTextWatcher
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

class MainActivity : MvpAppCompatActivity(), IMainActivityView, ItemTouchCallback {
    private val currencyAdapter = FastItemAdapter<CurrencyItem>()

    @InjectPresenter(tag = "MainActivityPresenter", type = PresenterType.WEAK)
    internal lateinit var presenter: MainActivityPresenter

    @Inject
    internal lateinit var presenterProvider: Provider<MainActivityPresenter>

    @ProvidePresenter(tag = "MainActivityPresenter", type = PresenterType.WEAK)
    fun providePresenter(): MainActivityPresenter {
        return presenterProvider.get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.mainActivityComponent().inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
    }

    private val itemEventHook = object : ClickEventHook<CurrencyItem>() {
        override fun onClick(v: View, position: Int, fastAdapter: FastAdapter<CurrencyItem>, item: CurrencyItem) {
            if (v is EditText) {
                v.isFocusable = true
                v.addTextChangedListener(object : CustomTextWatcher() {
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        s?.let {
                            presenter.setConversionFactor(it.toString())
                        }
                    }
                })
            }
        }

        override fun onBind(viewHolder: RecyclerView.ViewHolder): EditText? {
            if (viewHolder is CurrencyViewHolder) {
                return viewHolder.currencyEditText
            }
            return null
        }
    }

    private fun initRecyclerView() {
        val dragCallback = SimpleDragCallback(this)
        val touchHelper = ItemTouchHelper(dragCallback)
        touchHelper.attachToRecyclerView(currenciesRecyclerView)

        currencyAdapter.withOnClickListener { v, adapter, item, position ->
            if (item is CurrencyItem) {
                presenter.startWatchNewBaseCurrency(item.curName)
                itemTouchOnMove(position, 0)
                currenciesRecyclerView.scrollToPosition(0)
            }
            return@withOnClickListener true
        }.withEventHook(itemEventHook)

        with(currenciesRecyclerView) {
            adapter = currencyAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

    }

    override fun itemTouchOnMove(oldPosition: Int, newPosition: Int): Boolean {
        Collections.swap(currencyAdapter.adapterItems, oldPosition, newPosition)
        currencyAdapter.notifyAdapterItemMoved(oldPosition, newPosition)
        return true
    }

    override fun itemTouchDropped(oldPosition: Int, newPosition: Int) {

    }

    override fun updateList(currencies: Map<String, BigDecimal>, needUpdateBase: Boolean) {
        var isNewItem: Boolean
        currencies.forEach {
            val curItem = CurrencyItem(it.key, it.value)
            isNewItem = true
            currencyAdapter.adapterItems.forEach { currencyItem: CurrencyItem ->
                if (currencyItem.curName == curItem.curName) {
                    isNewItem = false
                    currencyItem.curValue = curItem.curValue
                }
            }

            if (isNewItem) {
                currencyAdapter.add(curItem)
                currencyAdapter.notifyDataSetChanged()
            }

            if (needUpdateBase) {
                currencyAdapter.notifyItemChanged(0)
            } else {
                currencyAdapter.notifyItemRangeChanged(1, currencyAdapter.itemCount - 1)
            }
        }
    }
}
