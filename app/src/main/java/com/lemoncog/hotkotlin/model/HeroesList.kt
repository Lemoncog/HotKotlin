package com.lemoncog.hotkotlin.model

data class Hero(val id: Long, val name: String, val subGroup: String, val group : Group, val played: Int = 0, val wins: Int = 0, val losses : Int = 0)

data class HeroesList(val heroesList: List<Hero>)
