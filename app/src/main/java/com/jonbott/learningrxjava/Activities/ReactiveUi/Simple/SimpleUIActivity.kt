package com.jonbott.learningrxjava.Activities.ReactiveUi.Simple

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jonbott.learningrxjava.Common.disposedBy
import com.jonbott.learningrxjava.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_simple_ui.*

class SimpleUIActivity : AppCompatActivity() {

    private val presenter = SimpleUIPresenter()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var simpleAdapter: SimpleAdapter

    var bag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_ui)
        rxExamples()
    }

    private fun rxExamples() {
        rxBindTitle()
        rxSimpleListBind()
    }

    private fun rxBindTitle() {
        presenter.title.observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn { "Default Value" }
                .share()
                .subscribe(RxTextView.text(simpleUITitleTextView2))
                .disposedBy(bag)
    }


    private fun rxSimpleListBind() {
//        val listItems = presenter.friends.map { it.toString() }
//        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
//        simpleUIListView.adapter = adapter
        simpleAdapter = SimpleAdapter(this, presenter)
        simpleUIListView.adapter = simpleAdapter

        presenter.selectedFriend.subscribe(Consumer {
            println("selected friend:")
            println(it)
            simpleUITitleTextView.text = it.firstName
//            println("link item:" + it.firstName + " - " + it.lastName)
        }).disposedBy(bag)

        simpleUIAcceptButton.setOnClickListener(View.OnClickListener {
            presenter.clearFriend()
        })
    }

    override fun onStop() {
        super.onStop()
        bag.dispose()
    }
}
