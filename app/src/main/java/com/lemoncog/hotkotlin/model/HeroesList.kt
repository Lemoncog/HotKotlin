package com.lemoncog.hotkotlin.model

data class Hero(val name: String, val subGroup: String, val group : Group)

data class HeroesList(val heroesList: List<Hero>)
