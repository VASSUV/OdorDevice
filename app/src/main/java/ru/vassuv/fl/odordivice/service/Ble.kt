package ru.vassuv.fl.odordivice.service

object Ble {
    var listener : (() -> Unit)? = null

    var name: String? = null
    var pass: String? = null
    var pcbV: String? = null
    var deviceV: String? = null
    var firmwareV: String? = null

    fun reset() {
        name = null
        pass = null
        pcbV = null
        deviceV = null
        firmwareV = null
    }

}