package ru.vassuv.fl.odordivice.service

import android.bluetooth.BluetoothDevice
import android.os.Build.MANUFACTURER
import android.os.Build.MODEL
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothConfiguration
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothWriter
import com.github.douglasjunior.bluetoothlowenergylibrary.BluetoothLeService
import ru.vassuv.fl.odordivice.App
import java.util.*


object BLibrary {
    var service: BluetoothService
    lateinit var writer: BluetoothWriter

    var device: BluetoothDevice? = null

    init {
        val config = BluetoothConfiguration()
        config.context = App.context
        config.bluetoothServiceClass = BluetoothLeService::class.java
        config.bufferSize = 1024
        config.characterDelimiter = '\n'
        config.deviceName = """$MANUFACTURER $MODEL"""
        config.callListenersInMainThread = true

        config.uuid = null
        config.uuidService = UUID.fromString("6cb025d9-eb74-4358-b442-dd6f51faec6f")
        config.uuidCharacteristic = UUID.randomUUID()
//        config.uuidCharacteristic = UUID.randomUUID()

        config.transport = 0 // Only for dual-mode devices

        BluetoothLeService.init(config)

        service = BluetoothLeService.getDefaultInstance()
    }

    fun connect() {
        service.configuration.uuidService = device?.uuids?.get(0)?.uuid ?: UUID.randomUUID()
        service.configuration.uuidCharacteristic = UUID.randomUUID()
        service.connect(device)
        writer = BluetoothWriter(service)
    }

    fun disconnect() {
        service.disconnect()
    }
}