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

fun toString(group: Group) : String {
    return when(group) {
        Group.Specialist -> "Specialist"
        Group.Assassin -> "Assassin"
        Group.Warrior -> "Warrior"
        Group.Support -> "Support"
        Group.Unknown -> "Unknown"
    }
}

enum class Group {
    Specialist, Assassin, Warrior, Support, Unknown;
}