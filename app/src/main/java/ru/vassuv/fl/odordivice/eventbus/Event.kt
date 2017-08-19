package ru.vassuv.fl.odordivice.eventbus

import android.bluetooth.BluetoothDevice
import android.view.View

data class PreloaderVisibilityEvent(var visibility: Int = View.GONE)

data class FindNewDeviceEvent(var device: BluetoothDevice)

