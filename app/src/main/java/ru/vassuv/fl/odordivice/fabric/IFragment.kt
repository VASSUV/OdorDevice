package ru.vassuv.fl.odordivice.fabric

import android.os.Bundle
import ru.vassuv.fl.odordivice.App

interface IFragment {

    fun onBackPressed() {
        App.router.exit()
    }

    fun onResult(data: Bundle) = Unit

    val type: FrmFabric
}