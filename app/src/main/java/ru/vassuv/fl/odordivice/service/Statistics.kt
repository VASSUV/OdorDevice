package ru.vassuv.fl.odordivice.service

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.net.Network
import android.os.Build.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import org.json.JSONObject
import ru.vassuv.fl.odordivice.App
import ru.vassuv.fl.odordivice.repository.Retrofit
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager



object Statistics {
    val deviceId: String = "$ID.$DEVICE.$MANUFACTURER.$MODEL"
    val list = arrayListOf<String>()
    var isSendingLogList = false

    fun send(message: String) {
        launch(CommonPool) {
            val json = JSONObject(mapOf("device_id" to deviceId, "message" to message)).toString()
            try {
                Retrofit.get(json).execute().body()
            } catch (e: Exception) {
                list.add("Error " + System.currentTimeMillis() + " " + json)
                App.router.showSystemMessage("Не удалось отправить отладочные данные. Возможно у вас отключен интернет", 0)
            }
            logListCheck()
        }
    }

    fun send(message: String, device: BluetoothDevice) {
        send (message + "name:" + device.name + ",address:" + device.address + ",type:" + device.type)
    }

    private fun logListCheck() {
        if (!isSendingLogList && isNetworkConnected()) {
            isSendingLogList = true
            val list = this.list.clone() as ArrayList<*>
            list.forEach {
                try {
                    Retrofit.get(this.list.first()).execute().body()
                } catch (e: Exception) {
                    return
                }
                this.list.removeAt(0)
            }
            isSendingLogList = false
        }
    }

    private fun isNetworkConnected(): Boolean {
        val cm = App.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }
}