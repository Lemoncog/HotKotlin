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
import android.widget.TextView
import com.lemoncog.hotkotlin.model.Group
import com.lemoncog.hotkotlin.model.Hero
import com.lemoncog.hotkotlin.model.HeroesList
import com.lemoncog.hotkotlin.service.model.HeroesListProvider
import java.util.*

class RunOnMainThread(val activity: Activity) {
    fun postRunnable(runnable: () -> Unit) {
        activity.runOnUiThread(runnable)
    }
}

class CheckedFilters {
    var supportChecked : Boolean = true
    var assassinChecked : Boolean = true
    var specialistChecked : Boolean = true
    var warriorChecked : Boolean = true
}

class FilterChecksView(val supportCheckBox: CheckBox, val assassinCheckBox: CheckBox, val specialistCheckBox: CheckBox, val warriorCheckBox: CheckBox) {
    lateinit var onSupportCheckChanged : (Boolean) -> Unit;
    lateinit var onAssassinCheckChanged : (Boolean) -> Unit;
    lateinit var onSpecialistCheckChanged : (Boolean) -> Unit;
    lateinit var onWarriorCheckChanged : (Boolean) -> Unit;

    init {
        supportCheckBox.setOnCheckedChangeListener(
                { _, checked ->
                    onSupportCheckChanged(checked)
                })

        assassinCheckBox.setOnCheckedChangeListener(
                { _, checked ->
                    onAssassinCheckChanged(checked)
                })

        specialistCheckBox.setOnCheckedChangeListener(
                { _, checked ->
                    onSpecialistCheckChanged(checked)
                })

        warriorCheckBox.setOnCheckedChangeListener(
                { _, checked ->
                    onWarriorCheckChanged(checked)
                })
    }
}

class HeroesFragment : LifecycleFragment() {
    lateinit var rootView : View
    lateinit var filterChecks : FilterChecksView
    val checkedFilters = CheckedFilters()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.heroes_list, container, false)

        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val runOnMain = RunOnMainThread(activity)
        val heroesListViewModel = ViewModelProviders.of(this,  object : ViewModelProvider.Factory {
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

            heroesAdapter.heroesList = heroesListViewModel!!
            heroesAdapter.notifyDataSetChanged()

            val supportCheckbox = rootView.findViewById(R.id.type_filter_support) as CheckBox
            val assassinCheckbox = rootView.findViewById(R.id.type_filter_assassin) as CheckBox
            val specialistCheckbox = rootView.findViewById(R.id.type_filter_specialist) as CheckBox
            val warriorCheckbox = rootView.findViewById(R.id.type_filter_warrior) as CheckBox

            val checkedFilters = CheckedFilters()

            val filterChecksView = FilterChecksView(supportCheckbox, assassinCheckbox, specialistCheckbox, warriorCheckbox)
            filterChecksView.onSupportCheckChanged = { checked ->
                checkedFilters.supportChecked = checked

                heroesAdapter.heroesList = filter(checkedFilters, heroesListViewModel)
                heroesAdapter.notifyDataSetChanged()
            }

            filterChecksView.onAssassinCheckChanged = { checked ->
                checkedFilters.assassinChecked = checked

                heroesAdapter.heroesList = filter(checkedFilters, heroesListViewModel)
                heroesAdapter.notifyDataSetChanged()
            }

            filterChecksView.onSpecialistCheckChanged = { checked ->
                checkedFilters.specialistChecked = checked

                heroesAdapter.heroesList = filter(checkedFilters, heroesListViewModel)
                heroesAdapter.notifyDataSetChanged()
            }

            filterChecksView.onWarriorCheckChanged = { checked ->
                checkedFilters.warriorChecked = checked

                heroesAdapter.heroesList = filter(checkedFilters, heroesListViewModel)
                heroesAdapter.notifyDataSetChanged()
            }
        });
    }

    fun filter(checkedFilters: CheckedFilters, heroesList: HeroesList): HeroesList {
        val filterHeroes = mutableListOf<Hero>()
        filterHeroes.addAll(heroesList.heroesList)

        if(!checkedFilters.assassinChecked) {
            filterHeroes.removeAll { it.group == Group.Assassin }
        }

        if(!checkedFilters.specialistChecked) {
            filterHeroes.removeAll { it.group == Group.Specialist }
        }

        if(!checkedFilters.supportChecked) {
            filterHeroes.removeAll { it.group == Group.Support }
        }

        if(!checkedFilters.warriorChecked) {
            filterHeroes.removeAll { it.group == Group.Warrior }
        }


        return HeroesList(filterHeroes)
    }
}

class HeroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title : TextView = itemView.findViewById(R.id.title) as TextView
    val subGroup : TextView = itemView.findViewById(R.id.subgroup) as TextView
    val rootView = itemView.findViewById(R.id.hero_view_root)
    val groupSymbol = itemView.findViewById(R.id.group_symbol)

}

class HeroesAdapter(val inflater: LayoutInflater) : RecyclerView.Adapter<HeroViewHolder>() {
    var heroesList = HeroesList(Collections.emptyList());

    override fun getItemCount(): Int {
        return heroesList.heroesList.size
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        val hero = heroesList.heroesList.get(position)
        holder.title.text = hero.name
        holder.subGroup.text = hero.subGroup

        var backgroundColor : Int = 0

        when(hero.group) {
            Group.Specialist -> backgroundColor = R.color.class_specialist
            Group.Warrior -> backgroundColor = R.color.class_warrior
            Group.Support -> backgroundColor = R.color.class_support
            Group.Assassin -> backgroundColor = R.color.class_assassin
            Group.Unknown -> backgroundColor = R.color.class_unknown
        }

        holder.groupSymbol.setBackgroundResource(backgroundColor);
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HeroViewHolder {
        return HeroViewHolder(inflater.inflate(R.layout.hero_view, parent, false))
    }

}