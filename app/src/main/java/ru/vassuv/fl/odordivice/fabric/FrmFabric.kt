package ru.vassuv.fl.odordivice.fabric

import android.os.Bundle
import ru.vassuv.fl.odordivice.ui.fragment.device.DevicesListFragment

enum class FrmFabric {
    EMPTY,
    MAIN,
    DEVICE_LIST;

    fun create(bundle: Bundle): IFragment {
        return when (this) {
            DEVICE_LIST -> DevicesListFragment.newInstance(bundle)
            else -> DevicesListFragment.newInstance(bundle)
        }
    }
}