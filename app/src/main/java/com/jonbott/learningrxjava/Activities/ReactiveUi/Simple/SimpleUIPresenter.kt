package com.jonbott.learningrxjava.Activities.ReactiveUi.Simple

import com.jonbott.learningrxjava.ModelLayer.Entities.Friend
import io.reactivex.subjects.BehaviorSubject

class SimpleUIPresenter {

    val selectedFriend = BehaviorSubject.createDefault(Friend("friend", "nn"))

    fun onItemClick(friend: Friend) {
        selectedFriend.onNext(friend)
//        println("link item:" + friend.firstName + " - " + friend.lastName)
    }

    var friends = listOf(Friend("Debi", "Darlington"),
            Friend("Arlie", "Abalos"),
            Friend("Jessica", "Jetton"),
            Friend("Tonia", "Threlkeld"),
            Friend("Donte", "Derosa"),
            Friend("Nohemi", "Notter"),
            Friend("Rod", "Rye"),
            Friend("Simonne", "Sala"),
            Friend("Kathaleen", "Kyles"),
            Friend("Loan", "Lawrie"),
            Friend("Elden", "Ellen"),
            Friend("Felecia", "Fortin"),
            Friend("Fiona", "Fiorini"),
            Friend("Joette", "July"),
            Friend("Beverley", "Bob"),
            Friend("Artie", "Aquino"),
            Friend("Yan", "Ybarbo"),
            Friend("Armando", "Araiza"),
            Friend("Dolly", "Delapaz"),
            Friend("Juliane", "Jobin"),
            Friend("Juliane1", "Jobin1"),
            Friend("Juliane2", "Jobin2"),
            Friend("Juliane3", "Jobin3"),
            Friend("Juliane4", "Jobin4"),
            Friend("Juliane5", "Jobin5"),
            Friend("Juliane6", "Jobin6"),
            Friend("Juliane7", "Jobin7")
    )
}