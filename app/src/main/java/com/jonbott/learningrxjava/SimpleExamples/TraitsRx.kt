package com.jonbott.learningrxjava.SimpleExamples

import com.jonbott.learningrxjava.Common.disposedBy
import com.jonbott.learningrxjava.SimpleExamples.SimpleRx.bag
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Created by Mike on 14/9/2019.
 */
object TraitsRx {
    fun traits_single() {
        val single = Single.create<String> { single ->
            //do some logic here
            val success = true

            if (success) {
                single.onSuccess("Nice work")
            } else {
                val someException = IllegalArgumentException("some fake error")
                single.onError(someException)
            }
        }

        single.subscribe({ result ->
            println("¬¬ single: ${result}")
        }, { error ->
            //do somethign for error
        }).disposedBy(bag)
    }

    fun traits_completable() {
        val completable = Completable.create { completable ->
            //do logic here
            val success = true

            if (success) {
                completable.onComplete()
            } else {
                val someException = IllegalArgumentException("some fake error")
                completable.onError(someException)
            }
        }

        completable.subscribe({
            println("AAAA completable completed")
        }, { error ->
            //do somethign for error
        }).disposedBy(bag)
    }


    fun traits_maybe() {
        val maybe = Maybe.create<String> { maybe ->
            //do logic here
            val success = true
            val hasResult = true

            if (success) {
                if (hasResult) {
                    maybe.onSuccess("some result")
                } else {
                    maybe.onComplete()
                }
            } else {
                val someException = IllegalArgumentException("some fake error")
                maybe.onError(someException)
            }
        }

        maybe.subscribe({ result ->
            println("AAAA Maybe - result: ${result}")
        }, { error ->
            //do somethign for error
        }, {
            //do something about comleting
            println("Maybe - completed")
        }).disposedBy(bag)
    }

}