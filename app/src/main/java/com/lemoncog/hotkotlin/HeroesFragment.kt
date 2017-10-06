package com.lemoncog.hotkotlin

import android.app.Activity
import android.arch.lifecycle.*
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.lemoncog.hotkotlin.model.Group
import com.lemoncog.hotkotlin.model.Hero
import com.lemoncog.hotkotlin.model.HeroesList
import com.lemoncog.hotkotlin.service.model.HeroesListProvider
import com.lemoncog.hotkotlin.view.HeroesAdapter

class RunOnMainThread(val activity: Activity) {
    fun postRunnable(runnable: () -> Unit) {
        activity.runOnUiThread(runnable)
    }
}

fun HeroesList.filter(checkedFilters: CheckedFilters): HeroesList {
    val filterHeroes = mutableListOf<Hero>()
    filterHeroes.addAll(heroesList)

    if (!checkedFilters.assassinChecked) {
        filterHeroes.removeAll { it.group == Group.Assassin }
    }

    if (!checkedFilters.specialistChecked) {
        filterHeroes.removeAll { it.group == Group.Specialist }
    }

    if (!checkedFilters.supportChecked) {
        filterHeroes.removeAll { it.group == Group.Support }
    }

    if (!checkedFilters.warriorChecked) {
        filterHeroes.removeAll { it.group == Group.Warrior }
    }


    return HeroesList(filterHeroes)
}

class HeroesFragment : LifecycleFragment() {
    lateinit var rootView: View
    lateinit var filterChecks: FilterChecksView
    val checkedFilters = CheckedFilters()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.heroes_list, container, false)

        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val runOnMain = RunOnMainThread(activity)

        val heroesListViewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(aClass: Class<T>): T {
                val heroesListProvider = HeroesListProvider()
                return HeroesListViewModel(heroesListProvider, runOnMain) as T
            }
        }).get(HeroesListViewModel::class.java);

        val heroesRecyclerView = rootView.findViewById(R.id.heroes_list) as RecyclerView
        val heroesAdapter = HeroesAdapter(layoutInflater)
        heroesAdapter.setHasStableIds(true)
        heroesRecyclerView.adapter = heroesAdapter
        heroesRecyclerView.layoutManager = GridLayoutManager(context, 2);
        heroesRecyclerView.itemAnimator

        heroesListViewModel.getHeroesList().observe(this, Observer { heroesListViewModel ->
            onLoad(heroesAdapter, heroesListViewModel!!)
        });
    }

    fun onLoad(adapter: HeroesAdapter, heroesListViewModel: HeroesList) {
        adapter.heroesList = heroesListViewModel
        adapter.notifyDataSetChanged()

        val supportCheckbox = rootView.findViewById(R.id.type_filter_support) as CheckBox
        val assassinCheckbox = rootView.findViewById(R.id.type_filter_assassin) as CheckBox
        val specialistCheckbox = rootView.findViewById(R.id.type_filter_specialist) as CheckBox
        val warriorCheckbox = rootView.findViewById(R.id.type_filter_warrior) as CheckBox

        val checkedFilters = CheckedFilters()

        val filterChecksView = FilterChecksView(supportCheckbox, assassinCheckbox, specialistCheckbox, warriorCheckbox)
        filterChecksView.onSupportCheckChanged = { checked ->
            checkedFilters.supportChecked = checked

            adapter.heroesList = heroesListViewModel.filter(checkedFilters)
            adapter.notifyDataSetChanged()
        }

        filterChecksView.onAssassinCheckChanged = { checked ->
            checkedFilters.assassinChecked = checked

            adapter.heroesList = heroesListViewModel.filter(checkedFilters)
            adapter.notifyDataSetChanged()
        }

        filterChecksView.onSpecialistCheckChanged = { checked ->
            checkedFilters.specialistChecked = checked

            adapter.heroesList = heroesListViewModel.filter(checkedFilters)
            adapter.notifyDataSetChanged()
        }

        filterChecksView.onWarriorCheckChanged = { checked ->
            checkedFilters.warriorChecked = checked

            adapter.heroesList = heroesListViewModel.filter(checkedFilters)
            adapter.notifyDataSetChanged()
        }
    }
}

