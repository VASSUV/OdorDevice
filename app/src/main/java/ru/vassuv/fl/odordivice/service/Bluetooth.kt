package ru.vassuv.fl.odordivice.service

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import ru.vassuv.fl.odordivice.App


object Bluetooth {
    val REQUEST_ENABLE_BT = 1
    private val SCAN_PERIOD: Long = 10000
    private var mScanning: Boolean = false

    var startActivityLambda: ((intent: Intent) -> Unit)? = null
    var scanCallBack: BluetoothAdapter.LeScanCallback? = null

    private val bluetoothManager: BluetoothManager = App.context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter


    fun check() {
        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityLambda?.invoke(enableBtIntent)
        }
    }

//    fun scanLeDevice(enable: Boolean, scanFinish : () -> Unit) = async(UI) {
//        bg {
//            if (enable) {
//                mScanning = true
//                bluetoothAdapter.startLeScan(scanCallBack)
//            } else {
//                mScanning = false
//                bluetoothAdapter.stopLeScan(scanCallBack)
//            }
//            if (enable) {
//                Thread.sleep(SCAN_PERIOD)
//                mScanning = false
//                bluetoothAdapter.stopLeScan(scanCallBack)
//            }
//        }.await()
//        scanFinish()
//    }
//
//    fun sendStat() {
//        App.log("""address: ${bluetoothAdapter.address}, name: ${bluetoothAdapter.name}, state: ${bluetoothAdapter.state}""")
//    }
}