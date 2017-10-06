package com.lemoncog.hotkotlin

import android.widget.CheckBox

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