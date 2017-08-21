package ru.vassuv.fl.odordivice.service

import android.bluetooth.BluetoothDevice
import android.os.Build.MANUFACTURER
import android.os.Build.MODEL
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothConfiguration
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService
import com.github.douglasjunior.bluetoothlowenergylibrary.BluetoothLeService
import ru.vassuv.fl.odordivice.App
import java.util.*

object BLibrary {
    var service: BluetoothService

    var device: BluetoothDevice? = null

    init {
        val config = BluetoothConfiguration()
        config.context = App.context
        config.bluetoothServiceClass = BluetoothLeService::class.java
        config.bufferSize = 1024
        config.characterDelimiter = '\n'
        config.deviceName = """$MANUFACTURER $MODEL"""
        config.callListenersInMainThread = true

// Bluetooth Classic
        config.uuid = null // UUID.randomUUID() //.fromString("00000000-0000-1000-8000-00805F9B34FB")

// Bluetooth LE
//        config.uuidService = UUID.fromString("00000000-0000-1000-8000-00805F9B34FB")
        config.uuidService = UUID.randomUUID()
//        config.uuidCharacteristic = UUID.fromString("bef8d6c9-9c21-4c9e-b632-bd58c1009f9f")
        config.uuidCharacteristic = UUID.randomUUID()

        BluetoothService.init(config)

        service = BluetoothService.getDefaultInstance()
    }

    fun connect() {
//        service.connect(device)
    }

    fun disconnect() {
        service.disconnect()
    }

    /*
         object : BluetoothService.OnBluetoothScanCallback {
            override fun onDeviceDiscovered(device: BluetoothDevice, rssi: Int) {}

            override fun onStartScan() {}

            override fun onStopScan() {}
        }*/
}