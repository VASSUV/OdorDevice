package ru.vassuv.fl.odordivice.ui.fragment.device

import android.bluetooth.BluetoothGattCharacteristic
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.fl.odordivice.R
import ru.vassuv.fl.odordivice.presentation.view.device.PasswordView
import ru.vassuv.fl.odordivice.presentation.presenter.device.PasswordPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_password.*
import kotlinx.android.synthetic.main.fragment_password.view.*
import ru.vassuv.fl.odordivice.fabric.FrmFabric
import ru.vassuv.fl.odordivice.fabric.IFragment
import ru.vassuv.fl.odordivice.utils.KeyboardUtils

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

    override fun onBackPressed() {
        KeyboardUtils.hideKeyboard(activity)
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate()
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onPause() {
        super.onPause()
        KeyboardUtils.hideKeyboard(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_password, container, false)
        rootView.button.setOnClickListener(presenter.getOnClickListener())
        rootView.editText.addTextChangedListener(presenter.getTextWatcher())
        return rootView
    }

    override fun requestPasswordView() {
        editText.requestFocusFromTouch()
        KeyboardUtils.showKeyboard(activity)
    }
}
