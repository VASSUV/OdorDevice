package ru.vassuv.fl.odordivice.fabric

import android.os.Bundle
import ru.vassuv.fl.odordivice.ui.fragment.device.*

enum class FrmFabric {
    EMPTY,
    DEVICE_LIST,
    PASSWORD,
    CONTROL,
    TIME_WORK,
    CHANGE_PASSWORD;

    fun create(data: Any?): IFragment {
        val bundle: Bundle = data as? Bundle ?: Bundle()
        return when (this) {
            DEVICE_LIST -> DevicesListFragment.newInstance(bundle)
            PASSWORD -> PasswordFragment.newInstance(bundle)
            CONTROL -> ControlFragment.newInstance(bundle)
            TIME_WORK -> TimeWorkFragment.newInstance(bundle)
            CHANGE_PASSWORD -> ChangePasswordFragment.newInstance(bundle)
            else -> DevicesListFragment.newInstance(bundle)
        }
    }
}