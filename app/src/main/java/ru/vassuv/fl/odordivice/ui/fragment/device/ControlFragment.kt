package ru.vassuv.fl.odordivice.ui.fragment.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.fl.odordivice.R
import ru.vassuv.fl.odordivice.presentation.view.device.ControlView
import ru.vassuv.fl.odordivice.presentation.presenter.device.ControlPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import ru.vassuv.fl.odordivice.fabric.FrmFabric
import ru.vassuv.fl.odordivice.fabric.IFragment

class ControlFragment : MvpAppCompatFragment(), ControlView, IFragment {
    override val type: FrmFabric
        get() = FrmFabric.CONTROL

    companion object {
        fun newInstance(args: Bundle): ControlFragment {
            val fragment: ControlFragment = ControlFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: ControlPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_control, container, false)
    }
}
