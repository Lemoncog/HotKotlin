package com.lemoncog.hotkotlin.model

fun fromString(value: String) : Group {
    when(value)
    {
        "Specialist" -> return Group.Specialist
        "Assassin" -> return Group.Assassin
        "Warrior" -> return Group.Warrior
        "Support" -> return Group.Support
    }

    return Group.Unknown
}

enum class Group {
    Specialist, Assassin, Warrior, Support, Unknown;
}