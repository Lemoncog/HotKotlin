package com.lemoncog.hotkotlin.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lemoncog.hotkotlin.R
import com.lemoncog.hotkotlin.model.Group
import com.lemoncog.hotkotlin.model.HeroesList
import java.util.*

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