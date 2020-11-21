package com.jonbott.learningrxjava.Activities.DatabaseExample

import android.text.Editable
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jonbott.learningrxjava.ModelLayer.ModelLayer
import com.jonbott.learningrxjava.ModelLayer.PersistenceLayer.PhotoDescription
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DatabaseExamplePresenter {
    fun onClickAddDescription(title: String) {
        modelLayer.insertItem(PhotoDescription(0, 1, 1, title, "http://placehold.it/600/771796", "http://placehold.it/150/771796", System.currentTimeMillis()))
    }

    val modelLayer = ModelLayer.shared //normally injected

    val photoDescriptions: BehaviorRelay<List<PhotoDescription>>
        get() = modelLayer.photoDescriptions //bubbling up from the lower lalyers

    init {
        GlobalScope.launch {
            delay(1000) //1 second
            modelLayer.loadAllPhotoDescriptions()
        }
    }
}