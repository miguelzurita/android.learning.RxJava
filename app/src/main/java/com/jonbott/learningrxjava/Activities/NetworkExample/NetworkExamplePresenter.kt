package com.jonbott.learningrxjava.Activities.NetworkExample

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jonbott.learningrxjava.ModelLayer.Entities.Message
import com.jonbott.learningrxjava.ModelLayer.ModelLayer
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class NetworkExamplePresenter {
    //region old way
    private val modelLayer = ModelLayer.shared //normally injected

    private val bag = CompositeDisposable()

    val messages: BehaviorRelay<List<Message>>
        get() = modelLayer.messages
    //endregion

    init {
        modelLayer.getMessages()
    }


    //region new way
    fun getMessagesRx(): Single<List<Message>> {
        return modelLayer.getMessagesRx()
    }
    fun getMessageRx(articleId:String): Single<Message> {
        return modelLayer.getMessageRx(articleId)
    }
    //endregion
}