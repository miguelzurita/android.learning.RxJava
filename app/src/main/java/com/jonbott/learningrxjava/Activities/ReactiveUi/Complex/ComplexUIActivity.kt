package com.jonbott.learningrxjava.Activities.ReactiveUi.Complex

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.jonbott.learningrxjava.Common.disposedBy
import com.jonbott.learningrxjava.R
import com.jonbott.learningrxjava.databinding.ActivityComplexUiBinding
import com.jonbott.learningrxjava.databinding.ItemReactiveUiBinding
import com.minimize.android.rxrecycleradapter.RxDataSource
import io.reactivex.disposables.CompositeDisposable


//https://github.com/ahmedrizwan/RxRecyclerAdapter

private enum class CellType {
    ITEM,
    ITEM2
}

class ReactiveUIActivity : Activity() {

    private val dataSet = (0..100).toList().map { it.toString() }

    private val bag = CompositeDisposable()

    lateinit var boundActivity: ActivityComplexUiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complex_ui)

        commonInit()
        showSimpleBindingExample()
    }

    private fun commonInit() {
        boundActivity = DataBindingUtil.setContentView(this, R.layout.activity_complex_ui)
        boundActivity.reactiveUIRecyclerView.layoutManager = LinearLayoutManager(this)
    }


    private fun showSimpleBindingExample() {
        val rxDataSources = RxDataSource<ItemReactiveUiBinding, String>(R.layout.item_reactive_ui, dataSet)
        rxDataSources.bindRecyclerView(boundActivity.reactiveUIRecyclerView)

        rxDataSources.map { it.toUpperCase() }
                .asObservable()
                .subscribe {
                    val ui = it.viewDataBinding ?: return@subscribe
                    val data = it.item

                    ui.textViewItem.text = data
                }.disposedBy(bag)
    }

    override fun onDestroy() {
        super.onDestroy()
        bag.clear()
    }
}