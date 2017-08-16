package ru.vassuv.fl.odordivice.fabric

import ru.vassuv.fl.odordivice.App

interface IFragment {

    fun onBackPressed() {
        App.router.exit()
    }

    val type: FrmFabric
}