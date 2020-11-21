package com.jonbott.learningrxjava.Activities.NetworkExample

import android.media.audiofx.PresetReverb
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.jonbott.learningrxjava.Activities.NetworkExample.Recycler.MessageViewAdapter
import com.jonbott.learningrxjava.Common.disposedBy
import com.jonbott.learningrxjava.ModelLayer.Entities.Message
import com.jonbott.learningrxjava.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_network_example.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NetworkExampleActivity : AppCompatActivity() {

    private val presenter = NetworkExamplePresenter()
    lateinit var adapter: MessageViewAdapter

    private val bag = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_example)


        //region old way
//        presenter.messages.observeOn(AndroidSchedulers.mainThread())
//                .subscribe { messages ->
//                    adapter.messages.accept(messages)
//                }.disposedBy(bag)
        //endregion

        //region new way
        GlobalScope.launch {
            loadData()
        }
        //endregion

        attachUI()
    }

    private fun loadData() {
        presenter.getMessagesRx()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { messages -> adapter.messages.accept(messages) }
                .disposedBy(bag)
    }

    private fun attachUI() {
        val linearLayoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        networkRecyclerView.layoutManager = linearLayoutManager
        networkRecyclerView.setHasFixedSize(true)
        networkRecyclerView.addItemDecoration(dividerItemDecoration)

        initializeListView()
    }

    private fun initializeListView() {
        adapter = MessageViewAdapter { view, position -> rowTapped(position) }
        networkRecyclerView.adapter = adapter
    }

    private fun rowTapped(position: Int) {
        println("🍄")
        val message: Message = adapter.messages.value[position]
        println("message selected:" + message.id.toString())
        println(message)
        //get selected post by id
        GlobalScope.launch {
            getPostFromServer(message)
        }

    }

    private fun getPostFromServer(message: Message) {
        presenter.getMessageRx(message.id.toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ message ->
                    println("post_title:" + message.title)
                }, { errorMessage ->
                    println(errorMessage)
                }).disposedBy(bag)
    }

    override fun onDestroy() {
        super.onDestroy()
        bag.clear()
    }
}

