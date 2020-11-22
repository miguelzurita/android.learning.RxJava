package com.jonbott.learningrxjava.ModelLayer.NetworkLayer

import com.jonbott.datalayerexample.DataLayer.NetworkLayer.EndpointInterfaces.JsonPlaceHolder
import com.jonbott.datalayerexample.DataLayer.NetworkLayer.Helpers.ServiceGenerator
import com.jonbott.learningrxjava.Common.NullBox
import com.jonbott.learningrxjava.Common.StringLambda
import com.jonbott.learningrxjava.Common.VoidLambda
import com.jonbott.learningrxjava.ModelLayer.Entities.Message
import com.jonbott.learningrxjava.ModelLayer.Entities.Person
import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import com.github.kittinunf.result.Result
import io.reactivex.rxkotlin.zip


typealias MessageLambda = (Message?) -> Unit
typealias MessagesLambda = (List<Message>?) -> Unit

class NetworkLayer {
    companion object {
        val instance = NetworkLayer()
    }

    private val placeHolderApi: JsonPlaceHolder

    init {
        placeHolderApi = ServiceGenerator.createService(JsonPlaceHolder::class.java)
    }


    //region rx methods
    fun getMessageRx(articleId: String): Single<Message> {
        return placeHolderApi.getMessageRx(articleId)
    }

    fun getMessagesRx(): Single<List<Message>> {
        return placeHolderApi.getMessagesRx()
    }

    fun postm(message: Message): Single<Message> {
        return placeHolderApi.postMessageRx(message)
    }

    //endregion


    //region End Point - SemiRx Way

    fun getMessages(success: MessagesLambda, failure: StringLambda) {
        val call = placeHolderApi.getMessages()

        call.enqueue(object : Callback<List<Message>> {
            override fun onResponse(call: Call<List<Message>>?, response: Response<List<Message>>?) {
                val article = parseRespone(response)
                success(article)
            }

            override fun onFailure(call: Call<List<Message>>?, t: Throwable?) {
                println("Failed to GET Message: ${t?.message}")
                failure(t?.localizedMessage ?: "Unknown error occured")
            }
        })
    }

    fun getMessage(articleId: String, success: MessageLambda, failure: VoidLambda) {
        val call = placeHolderApi.getMessage(articleId)

        call.enqueue(object : Callback<Message> {
            override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                val article = parseRespone(response)
                success(article)
            }

            override fun onFailure(call: Call<Message>?, t: Throwable?) {
                println("Failed to GET Message: ${t?.message}")
                failure()
            }
        })
    }

    fun postMessage(message: Message, success: MessageLambda, failure: VoidLambda) {
        val call = placeHolderApi.postMessage(message)

        call.enqueue(object : Callback<Message> {
            override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                val article = parseRespone(response)
                success(article)
            }

            override fun onFailure(call: Call<Message>?, t: Throwable?) {
                println("Failed to POST Message: ${t?.message}")
                failure()
            }
        })
    }

    private fun <T> parseRespone(response: Response<T>?): T? {
        val article = response?.body() ?: null

        if (article == null) {
            parseResponeError(response)
        }

        return article
    }

    private fun <T> parseResponeError(response: Response<T>?) {
        if (response == null) return

        val responseBody = response.errorBody()

        if (responseBody != null) {
            try {
                val text = "responseBody = ${responseBody.string()}"
                println("$text")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            val text = "responseBody == null"
            println("$text")
        }
    }

    //endregion


    //region task example

    //Make one Observable for each person in a list
    fun loadInfoFor(people: List<Person>): Observable<List<String>> {
        //Foreach person make a network call
        val networkObservables = people.map(::buildGetInfoNetworkCallFor)

        //when all server results have returned zip observables into a single observable
        return networkObservables.zip { list ->
            list.filter { box -> box.value != null }
                    .map { it.value!! }

        }
    }

    //Wrap task in reactive observable
    //This pattern is used often for units of work
    private fun buildGetInfoNetworkCallFor(person: Person): Observable<NullBox<String>> {
        return Observable.create { observer ->
            //Execute request - Do actual work here
            getInfoFor(person) { result ->
                result.fold({ info ->
                    observer.onNext(info)
                    observer.onComplete()
                }, { error ->
                    observer.onError(error)
                })
            }
        }
    }

    //Create a network task
    fun getInfoFor(person: Person, finished: (Result<NullBox<String>, Exception>) -> Unit) {
        //Execute on Background Thread
        //Do your task here
        GlobalScope.launch {
            println("start network call: $person")
            val randomTime: Long = person.age * 1000L //to milliseconds
            delay(randomTime)
            println("finished network call: $person")

            //just randomly make odd people null
            var result = Result.of(NullBox(person.toString()))
            finished(result)
        }
    }

    //endregion


}