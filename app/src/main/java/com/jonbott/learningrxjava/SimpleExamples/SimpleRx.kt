package com.jonbott.learningrxjava.SimpleExamples

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by Mike on 4/8/2019.
 */
object SimpleRx {
    var bag = CompositeDisposable()

    fun simpleValues() {
        println("--simpleValues--")

        val someInfo = BehaviorRelay.createDefault("1")
        println("(-) someInfo.value${someInfo.value}")

        val plainString = someInfo.value
        println("(-) plainString: $plainString")

        someInfo.accept("2")
        println("someinfo.value  ${someInfo.value}")

        someInfo.subscribe { newValue ->
            println("-> value has changed: $newValue")
        }

        someInfo.accept("3")

        //NOTE: relays will never receive on Error, and onComplete events
    }

    fun subjects() {
        val behaviourSubject = BehaviorSubject.createDefault(24)
        val disposable = behaviourSubject.subscribe({//onNext event
            newValue ->
            println("behaviour subcription: ${newValue}")
        }, { //onError
            error ->
            println("error: ${error.localizedMessage}")
        }, {
            //on Completed
            println("completed")
        }, { //onSubcribed
            disposable ->
            println("subscribed")
        })

        behaviourSubject.onNext(34)
        behaviourSubject.onNext(48)
        behaviourSubject.onNext(48)
    }
}