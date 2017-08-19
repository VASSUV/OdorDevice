package ru.vassuv.fl.odordivice.ui.fragment.device

import android.bluetooth.BluetoothGattCharacteristic
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.fl.odordivice.R
import ru.vassuv.fl.odordivice.presentation.view.device.PasswordView
import ru.vassuv.fl.odordivice.presentation.presenter.device.PasswordPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import ru.vassuv.fl.odordivice.fabric.FrmFabric
import ru.vassuv.fl.odordivice.fabric.IFragment

class PasswordFragment : MvpAppCompatFragment(), PasswordView, IFragment {
    override val type: FrmFabric
        get() = FrmFabric.PASSWORD

    companion object {
        fun newInstance(args: Bundle): PasswordFragment {
            val fragment: PasswordFragment = PasswordFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: PasswordPresenter

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_password, container, false)
    }
}
