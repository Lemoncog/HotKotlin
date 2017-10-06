package com.lemoncog.hotkotlin

import android.arch.lifecycle.*
import android.os.Bundle

class MainActivity : LifecycleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.content_holder, HeroesFragment()).commit()
        }
    }
}
