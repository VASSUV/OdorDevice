package ru.vassuv.fl.odordivice.ui.fragment.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.vassuv.fl.odordivice.R
import ru.vassuv.fl.odordivice.presentation.view.device.ChangePasswordView
import ru.vassuv.fl.odordivice.presentation.presenter.device.ChangePasswordPresenter

import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import ru.vassuv.fl.odordivice.fabric.FrmFabric
import ru.vassuv.fl.odordivice.fabric.IFragment

class ChangePasswordFragment : MvpAppCompatFragment(), ChangePasswordView, IFragment {
    override val type: FrmFabric
        get() = FrmFabric.CHANGE_PASSWORD

    companion object {
        fun newInstance(args: Bundle): ChangePasswordFragment {
            val fragment: ChangePasswordFragment = ChangePasswordFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: ChangePasswordPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }
}
