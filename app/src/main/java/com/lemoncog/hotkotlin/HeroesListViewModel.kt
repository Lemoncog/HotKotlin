package com.lemoncog.hotkotlin


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import com.lemoncog.hotkotlin.model.HeroesList
import com.lemoncog.hotkotlin.service.model.HeroesListProvider

class HeroesListViewModel(val heroesListProvider: HeroesListProvider, val runOnThread: RunOnMainThread) : ViewModel() {

    fun getHeroesList() : MutableLiveData<HeroesList> {
        val mutableHeroesList = MutableLiveData<HeroesList>()

        heroesListProvider.get { heroesList ->
            runOnThread.postRunnable {  mutableHeroesList.value = heroesList }
        }

        return mutableHeroesList
    }

}
