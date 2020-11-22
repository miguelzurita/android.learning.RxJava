package com.jonbott.learningrxjava.Activities.TasksExample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jonbott.learningrxjava.Common.disposedBy
import com.jonbott.learningrxjava.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class TasksExampleActivity : AppCompatActivity() {

    private val presenter = TasksExamplePresenter()

    private val bag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks_example)

        presenter.loadPeopleInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ infoList ->
                    println("all process completed successfully")
                    println("network results: \n\t $infoList")
                    infoList.forEach { println("nn: ${it}") }
                }, { error ->
                    println("error: $error")
                }).disposedBy(bag)
    }

    override fun onDestroy() {
        super.onDestroy()
        bag.clear()
    }
}

