package ru.vassuv.fl.odordivice.fabric

import android.os.Bundle

enum class FrmFabric {
    EMPTY,
    MAIN,
    PICTURE;

    companion object {
        fun getById(id: Int) = when (id) {
            else -> EMPTY
        }
    }

//    fun create(bundle: Bundle): IFragment {
//        return when (this) {
//            else ->
//        }
//    }
}