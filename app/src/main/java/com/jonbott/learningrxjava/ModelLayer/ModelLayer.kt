package com.jonbott.learningrxjava.ModelLayer

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jonbott.learningrxjava.ModelLayer.Entities.Message
import com.jonbott.learningrxjava.ModelLayer.NetworkLayer.NetworkLayer
import com.jonbott.learningrxjava.ModelLayer.PersistenceLayer.PersistenceLayer
import com.jonbott.learningrxjava.ModelLayer.PersistenceLayer.PhotoDescription
import io.reactivex.Single

class ModelLayer {

    companion object {
        val shared = ModelLayer()
    }

    val photoDescriptions = BehaviorRelay.createDefault(listOf<PhotoDescription>())
    val messages = BehaviorRelay.createDefault(listOf<Message>())

    private val networkLayer = NetworkLayer.instance
    private val persistenceLayer = PersistenceLayer.shared

    fun loadAllPhotoDescriptions() {
        //result may be immediate
        persistenceLayer.loadAllPhotoDescriptions { photoDescriptions ->
            this.photoDescriptions.accept(photoDescriptions)
        }
    }

    fun getMessages() {
        return networkLayer.getMessages({ messages ->
            this.messages.accept(messages)
        }, { errorMessage ->
            notifyError(errorMessage)
        })
    }

    private fun notifyError(errorMessage: String) {
        println(errorMessage)
    }

    fun insertItem(photoDescription: PhotoDescription) {
        persistenceLayer.insertItem(photoDescription)
    }

    fun getMessagesRx(): Single<List<Message>> {
        return networkLayer.getMessagesRx()
    }

    fun getMessageRx(articleId:String): Single<Message> {
        return networkLayer.getMessageRx(articleId)
    }
}