package ru.vassuv.fl.odordivice.service

import android.bluetooth.*
import android.content.Intent
import ru.vassuv.fl.odordivice.App
import java.util.*
import android.bluetooth.BluetoothGattCharacteristic

object Gatt {
    val TAG = "Gatt"

    val STATE_DISCONNECTED = 0
    val STATE_CONNECTING = 1
    val STATE_CONNECTED = 2

    val mBluetoothManager: BluetoothManager? = null
    val mBluetoothAdapter: BluetoothAdapter? = null
    val mBluetoothDeviceAddress: String? = null
    var mBluetoothGatt: BluetoothGatt? = null
    var characteristic: BluetoothGattCharacteristic? = null
    var enabled: Boolean = false

    var mConnectionState = STATE_DISCONNECTED

    val ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED"
    val ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED"
    val ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED"
    val ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE"
    val EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA"

    val UUID_HEART_RATE_MEASUREMENT: UUID = UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb")

    var sendBroadcastLambda: ((intent: Intent) -> Unit)? = null

    fun connectDevice(device: BluetoothDevice) {
        mBluetoothGatt = device.connectGatt(App.context, false, gattCallBack)
    }

    fun readDevice() {
        mBluetoothGatt?.readCharacteristic(characteristic)
    }

    fun writeDevice(characteristic: BluetoothGattCharacteristic) {
        mBluetoothGatt?.writeCharacteristic(characteristic)
    }

    fun broadcastUpdate(intentAction: String) {
        sendBroadcastLambda?.invoke(Intent(intentAction));
    }

    fun broadcastUpdate(action: String, characteristic: BluetoothGattCharacteristic?) {
        val intent = Intent(action)

        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic?.uuid)) {
            val flag = characteristic?.properties
            var format = -1
            if (flag?.and(0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16
                App.log(TAG, "Heart rate format UINT16.")
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8
                App.log(TAG, "Heart rate format UINT8.")
            }
            val heartRate = characteristic?.getIntValue(format, 1)!!
            App.log(TAG, String.format("Received heart rate: %d", heartRate))
            intent.putExtra(EXTRA_DATA, heartRate.toString())
        } else {
            // For all other profiles, writes the data formatted in HEX.
            val data = characteristic?.value
            App.log(TAG, String.format("Received: " + data))
            if (data != null && data.isNotEmpty()) {
                val stringBuilder = StringBuilder(data.size)
                for (byteChar in data)
                    stringBuilder.append(String.format("%02X ", byteChar))
                intent.putExtra(EXTRA_DATA, String(data) + "\n" +
                        stringBuilder.toString())
            }
        }
        sendBroadcastLambda?.invoke(intent)
    }

    fun close() {
        mBluetoothGatt?.close()
    }

}

val gattCallBack = object : BluetoothGattCallback() {
    override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
        App.log(Gatt.TAG, "onCharacteristicRead received: $status $characteristic")
        if (status == BluetoothGatt.GATT_SUCCESS) {
            Gatt.broadcastUpdate(Gatt.ACTION_DATA_AVAILABLE, characteristic);
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            Gatt.broadcastUpdate(Gatt.ACTION_GATT_SERVICES_DISCOVERED);
        }
        App.log(Gatt.TAG, "onServicesDiscovered received: " + status);
    }

    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        val intentAction: String
        App.log(Gatt.TAG, "onConnectionStateChange - " + gatt?.device?.address + ", " + gatt?.device?.name)
        if (newState === BluetoothProfile.STATE_CONNECTED) {
            intentAction = Gatt.ACTION_GATT_CONNECTED
            Gatt.mConnectionState = Gatt.STATE_CONNECTED
            Gatt.broadcastUpdate(intentAction)
            App.log(Gatt.TAG, "Attempting to start service discovery:" + Gatt.mBluetoothGatt?.discoverServices())
            Gatt.readDevice()
        } else if (newState === BluetoothProfile.STATE_DISCONNECTED) {
            intentAction = Gatt.ACTION_GATT_DISCONNECTED
            Gatt.mConnectionState = Gatt.STATE_DISCONNECTED
            App.log(Gatt.TAG, "Disconnected from GATT server.")
            Gatt.broadcastUpdate(intentAction)
        }
    }
}