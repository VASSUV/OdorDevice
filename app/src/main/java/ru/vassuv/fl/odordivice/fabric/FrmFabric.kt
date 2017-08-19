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

    fun create(data: Bundle?): IFragment {
        return when (this) {
            DEVICE_LIST -> DevicesListFragment.newInstance(data ?: Bundle())
            PASSWORD -> PasswordFragment.newInstance(data ?: Bundle())
            CONTROL -> ControlFragment.newInstance(data ?: Bundle())
            TIME_WORK -> TimeWorkFragment.newInstance(data ?: Bundle())
            CHANGE_PASSWORD -> ChangePasswordFragment.newInstance(data ?: Bundle())
            else -> DevicesListFragment.newInstance(data ?: Bundle())
        }
    }
}