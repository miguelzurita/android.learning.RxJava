package com.jonbott.learningrxjava.SimpleExamples

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jonbott.learningrxjava.Common.disposedBy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.toObservable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

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
            println("¬ behaviourSubjct subcription: ${newValue}")
        }, { //onError
            error ->
            println("¬ error: ${error.localizedMessage}")
        }, {
            //on Completed
            println("¬ completed")
        }, { //onSubcribed
            disposable ->
            println("¬ subscribed")
        })

        behaviourSubject.onNext(34)
        behaviourSubject.onNext(48)
        behaviourSubject.onNext(48)

//        val someException = IllegalArgumentException("some fake error")
//        behaviourSubject.onError(someException)
//        behaviourSubject.onNext(109)

        //2 On complete
//        behaviourSubject.onComplete()
//        behaviourSubject.onNext(10893)


    }

    fun basicObservable() {
        //The observable
        val observable = Observable.create<String> { observer ->
            println(">>> observable logic being triggered")
            GlobalScope.launch {
                delay(1000)
                observer.onNext("some value 23")
                observer.onComplete()
            }
        }

        observable.subscribe { someString ->
            println("new value: $someString")
        }.disposedBy(bag)

        val observer = observable.subscribe { someString ->
            println("¬¬ Another subscriber: $someString")
        }
        observer.disposedBy(bag)
    }

    fun creatingObservables() {
//        val observable = Observable.just(23)
//        val observable = Observable.interval(300, TimeUnit.MILLISECONDS).timeInterval(AndroidSchedulers.mainThread())
        val userIds = arrayOf(1, 2, 3, 4, 5, 6)
//        val observable = Observable.fromArray(*userIds)
        val observable = userIds.toObservable()
    }
}