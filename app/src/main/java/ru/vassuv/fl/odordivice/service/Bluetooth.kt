package ru.vassuv.fl.odordivice.service

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import ru.vassuv.fl.odordivice.App

object Bluetooth {
    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10000
    private var mScanning: Boolean = false
    private var mHandler: Handler? = null

    val REQUEST_ENABLE_BT = 1

    var startActivityLambda: ((intent: Intent) -> Unit)? = null
        set(value) {
            field = value
        }

    var scanCallBack: BluetoothAdapter.LeScanCallback? = null
        set(value) {
            field = value
        }

    private val bluetoothManager: BluetoothManager = App.context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter


    fun check() {
        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityLambda?.invoke(enableBtIntent)
        }
    }

    fun scanLeDevice(enable: Boolean) {
        if (enable) {
            if (mHandler == null) mHandler = Handler()

            mHandler!!.postDelayed({
                mScanning = false
                bluetoothAdapter.stopLeScan(scanCallBack)
            }, SCAN_PERIOD)

            mScanning = true
            bluetoothAdapter.startLeScan(scanCallBack)
        } else {
            mScanning = false
            bluetoothAdapter.stopLeScan(scanCallBack)
        }
    }

    fun sendStat() {
        App.log("""address: ${bluetoothAdapter.address}, name: ${bluetoothAdapter.name}, state: ${bluetoothAdapter.state}""")
    }
}