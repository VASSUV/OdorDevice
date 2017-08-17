package ru.vassuv.fl.odordivice.ui.activity.main

import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_main.*
import ru.vassuv.fl.odordivice.App
import ru.vassuv.fl.odordivice.R
import ru.vassuv.fl.odordivice.presentation.presenter.main.MainPresenter
import ru.vassuv.fl.odordivice.presentation.view.main.MainView
import ru.vassuv.fl.odordivice.service.Bluetooth
import java.util.*


class MainActivity : MvpAppCompatActivity(), MainView {
    @InjectPresenter
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.onCreate(supportFragmentManager, savedInstanceState ?: Bundle())

        Bluetooth.startActivityLambda = {
            startActivityForResult(it, Bluetooth.REQUEST_ENABLE_BT)
        }

        Bluetooth.scanCallBack = BluetoothAdapter.LeScanCallback { p0, p1, p2 ->
            App.log("Найдено устройство: p0-$p0, p1-$p1, p2-$p2")
        }

        Bluetooth.scanLeDevice(true)
        Bluetooth.sendStat()

        // Регистрируем BroadcastReceiver
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(mReceiver, filter)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        presenter.onSaveInstanceState(outState)
    }

    override fun showMessage(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG)
    }

    override fun setVisibilityPreloader(visibility: Int) {
        progressBar.visibility = visibility
    }

    private val TAG = "MAIN_ACTIVITY"

    private val mBluetoothGatt: BluetoothGatt? = null

    private val STATE_DISCONNECTED = 0
    private val STATE_CONNECTING = 1
    private val STATE_CONNECTED = 2

    private var mConnectionState = STATE_DISCONNECTED

    val ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED"
    val ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED"
    val ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED"
    val ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE"
    val EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA"

    // Устанавливаем UUID, который используется для услуг измерения пульса
        val UUID_HEART_RATE_MEASUREMENT = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb")


    // Создаем BroadcastReceiver для ACTION_FOUND
    val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            // Когда найдено новое устройство
            if (BluetoothDevice.ACTION_FOUND == action) {
                // Получаем объект BluetoothDevice из интента
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                //Добавляем имя и адрес в array adapter, чтобы показвать в ListView
                App.log("Найдено устройство: name-${device.name}, address-${device.address}, type-${device.type}, uuids-${device.uuids}")
                device.connectGatt(context, false, object : BluetoothGattCallback() {
                    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                        if (status == BluetoothGatt.GATT_SUCCESS) {
                            broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                        } else {
                            App.log("""onServicesDiscovered received: $status""");
                        }
                    }

                    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                        val intentAction: String
                        if (newState === BluetoothProfile.STATE_CONNECTED) {
                            intentAction = ACTION_GATT_CONNECTED
                            mConnectionState = STATE_CONNECTED
                            broadcastUpdate(intentAction)
                            App.log(TAG, "Connected to GATT server.")
                            App.log(TAG, "Attempting to start service discovery:" + mBluetoothGatt?.discoverServices())

                        } else if (newState === BluetoothProfile.STATE_DISCONNECTED) {
                            intentAction = ACTION_GATT_DISCONNECTED
                            mConnectionState = STATE_DISCONNECTED
                            App.log(TAG, "Disconnected from GATT server.")
                            broadcastUpdate(intentAction)
                        }
                    }
                })
            }
        }

    }

    private fun broadcastUpdate(action: String) {
        val intent = Intent(action)
        sendBroadcast(intent)
    }

    private fun broadcastUpdate(action: String,
                                characteristic: BluetoothGattCharacteristic) {
        val intent = Intent(action)

        // Это специальная обработка для пульсометра
        // Извлечение данных осуществляется согласно спецификации профиля
        if (UUID_HEART_RATE_MEASUREMENT == characteristic.uuid) {
            val flag = characteristic.properties
            var format = -1
            if (flag and 0x01 != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16
                App.log(TAG, "Heart rate format UINT16.")
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8
                App.log(TAG, "Heart rate format UINT8.")
            }
            val heartRate = characteristic.getIntValue(format, 1)!!
            App.log(TAG, String.format("Received heart rate: %d", heartRate))
            intent.putExtra(EXTRA_DATA, heartRate.toString())
        } else {
            // For all other profiles, writes the data formatted in HEX.
            val data = characteristic.value
            if (data != null && data.isNotEmpty()) {
                val stringBuilder = StringBuilder(data.size)
                for (byteChar in data)
                    stringBuilder.append(String.format("%02X ", byteChar))
                intent.putExtra(EXTRA_DATA, String(data) + "\n" +
                        stringBuilder.toString())
            }
        }
        sendBroadcast(intent)
    }
    //
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.action_settings -> return true
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }
}
