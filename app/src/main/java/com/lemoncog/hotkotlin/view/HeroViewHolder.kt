package com.lemoncog.hotkotlin.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.lemoncog.hotkotlin.R

class HeroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title : TextView = itemView.findViewById(R.id.title) as TextView
    val subGroup : TextView = itemView.findViewById(R.id.subgroup) as TextView
    val rootView = itemView.findViewById(R.id.hero_view_root)
    val groupSymbol = itemView.findViewById(R.id.group_symbol)
    val playsInfo = itemView.findViewById(R.id.plays_info) as TextView

}