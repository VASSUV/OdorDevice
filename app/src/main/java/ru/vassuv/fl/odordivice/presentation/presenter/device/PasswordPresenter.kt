package ru.vassuv.fl.odordivice.presentation.presenter.device

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.vassuv.fl.odordivice.presentation.view.device.PasswordView
import ru.vassuv.fl.odordivice.service.Gatt

@InjectViewState
class PasswordPresenter : MvpPresenter<PasswordView>() {
    fun onStart() {
        Gatt.gattStateChangeLambda = {
            if (it) {
                Gatt.readDevice(byteArrayOf(0x01))
            } else {
                Gatt.connectDevice()
            }
        }

//        Gatt.connectDevice()
    }

    fun onStop() {
        Gatt.close()
    }
}
