package ru.vassuv.fl.odordivice.presentation.presenter.device

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothStatus
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import ru.vassuv.fl.odordivice.App
import ru.vassuv.fl.odordivice.fabric.FrmFabric
import ru.vassuv.fl.odordivice.presentation.view.device.PasswordView
import ru.vassuv.fl.odordivice.service.BLibrary
import ru.vassuv.fl.odordivice.service.Statistics

@InjectViewState
class PasswordPresenter : MvpPresenter<PasswordView>() {

    var pass: String = ""

    fun onCreate() {
        BLibrary.service.setOnEventCallback(object : BluetoothService.OnBluetoothEventCallback {
            override fun onDataRead(buffer: ByteArray, length: Int) {
                val message = "onDataRead - buffer:" + buffer.joinToString() + ";length:" + length
                Statistics.send(message)
                App.router.showSystemMessage(message, 0)
            }

            override fun onStatusChange(status: BluetoothStatus) {
                val message = "onStatusChange - status:" + status.name
                Statistics.send(message)
                App.router.showSystemMessage(message, 0)
            }

            override fun onDeviceName(deviceName: String) {
                val message = "onDeviceName - deviceName:" + deviceName
                Statistics.send(message)
                App.router.showSystemMessage(message, 0)
            }

            override fun onToast(message: String) {
                val message1 = "onToast - message:" + message
                Statistics.send(message1)
                App.router.showSystemMessage(message1, 0)
            }

            override fun onDataWrite(buffer: ByteArray) {
                val message = "onDataWrite - buffer:" + buffer.joinToString()
                Statistics.send(message)
                App.router.showSystemMessage(message, 0)
            }
        })
    }

    fun onStart() {
        BLibrary.connect()
    }

    fun onStop() {
        BLibrary.disconnect()
    }

    fun getOnClickListener(): View.OnClickListener? = View.OnClickListener {
        if (isPasswordValid()) {
            BLibrary.service.write(pass.toByteArray())
            App.router.replaceScreen(FrmFabric.CONTROL.name)
        } else {
            viewState.requestPasswordView()
            launch(CommonPool) {
                Thread.sleep(500)
                App.router.showSystemMessage("Некорректно введен пароль. Длина пароля от 4 до 8 цифр", 0)
            }
        }
    }

    private fun isPasswordValid(): Boolean {
        return pass.length in 4..8 && pass.toIntOrNull() != null
    }

    fun getTextWatcher(): TextWatcher? {
        return object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) = Unit

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                pass = p0.toString()
            }
        }
    }
}
