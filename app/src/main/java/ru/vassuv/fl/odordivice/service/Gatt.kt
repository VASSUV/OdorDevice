package ru.vassuv.fl.odordivice.service

import android.bluetooth.*
import android.bluetooth.BluetoothGatt.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.vassuv.fl.odordivice.App

object Gatt {
    val TAG = "Gatt"

    val STATE_DISCONNECTED = 0
//    val STATE_CONNECTING = 1
    val STATE_CONNECTED = 2

    var mBluetoothGatt: BluetoothGatt? = null
    var characteristic: BluetoothGattCharacteristic? = null
    lateinit var device: BluetoothDevice

    var gattStateChangeLambda: ((b: Boolean) -> Unit)? = null

    var mConnectionState = STATE_DISCONNECTED
    val ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED"
    val ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED"
    val ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED"
    val ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE"

    val EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA"

    var sendBroadcastLambda: ((intent: BroadcastReceiver) -> Unit)? = null

    private var mConnected: Boolean = false

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            when (action) {
                ACTION_GATT_CONNECTED -> mConnected = true
                ACTION_GATT_DISCONNECTED -> mConnected = false
            //                clearUI();
                ACTION_GATT_SERVICES_DISCOVERED -> {
                    //                displayGattServices(getSupportedGattServices());
                }
                ACTION_DATA_AVAILABLE -> {
                    //                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                }
            }
        }
    }

    fun connectDevice() {
        mBluetoothGatt = device.connectGatt(App.context, false, gattCallBack)
    }

    fun readDevice(bytes: ByteArray) {
        App.log("readDevice: " + bytes.toString())
        characteristic = mBluetoothGatt?.getService(device.uuids[0].uuid)?.characteristics?.get(0)
        characteristic?.value = bytes
        mBluetoothGatt?.readCharacteristic(characteristic)
    }

//    fun writeDevice(characteristic: BluetoothGattCharacteristic) {
//        App.log(Gatt.TAG, "writeDevice: " + characteristic.value)
//        mBluetoothGatt?.writeCharacteristic(characteristic)
//    }

    fun broadcastUpdate() {
        sendBroadcastLambda?.invoke(receiver)
    }

    fun broadcastUpdate(action: String, characteristic: BluetoothGattCharacteristic?) {
        val intent = Intent(action)
        val data = characteristic?.value

        App.log(TAG, String.format("Received: " + data))

        if (data != null && data.isNotEmpty()) {
            val stringBuilder = StringBuilder(data.size)
            for (byteChar in data)
                stringBuilder.append(String.format("%02X ", byteChar))
            intent.putExtra(EXTRA_DATA, """${String(data)} $stringBuilder""")
        }

        sendBroadcastLambda?.invoke(receiver)
    }

    fun close() {
        mBluetoothGatt?.close()
    }

    fun take(device: BluetoothDevice) {
        this.device = device
    }

    val gattCallBack = object : BluetoothGattCallback() {
        override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            App.log(TAG, "onCharacteristicRead received: $status $characteristic")
            if (status == GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic)
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status == GATT_SUCCESS) {
                broadcastUpdate()
            }
            App.log(TAG, "onServicesDiscovered received: " + status)
        }

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
//            val intentAction: String
            App.log(TAG, "onConnectionStateChange - " + gatt?.device?.address + ", " + gatt?.device?.name)
            if (newState == BluetoothProfile.STATE_CONNECTED) {
//                intentAction = ACTION_GATT_CONNECTED
                mConnectionState = STATE_CONNECTED
                broadcastUpdate()
                App.log(TAG, "Attempting to start service discovery:" + mBluetoothGatt?.discoverServices())
                gattStateChangeLambda?.invoke(true)
//            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//                intentAction = ACTION_GATT_DISCONNECTED
                mConnectionState = STATE_DISCONNECTED
                App.log(TAG, "Disconnected from GATT server.")
                broadcastUpdate()
                gattStateChangeLambda?.invoke(false)
            } else {
                gattStateChangeLambda?.invoke(false)
            }
        }
    }
}
