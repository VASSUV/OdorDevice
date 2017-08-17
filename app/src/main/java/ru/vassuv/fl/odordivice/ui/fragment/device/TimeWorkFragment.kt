package ru.vassuv.fl.odordivice.ui.fragment.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.fl.odordivice.R
import ru.vassuv.fl.odordivice.presentation.view.device.TimeWorkView
import ru.vassuv.fl.odordivice.presentation.presenter.device.TimeWorkPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import ru.vassuv.fl.odordivice.fabric.FrmFabric
import ru.vassuv.fl.odordivice.fabric.IFragment

class TimeWorkFragment : MvpAppCompatFragment(), TimeWorkView, IFragment {
    override val type: FrmFabric
        get() = FrmFabric.TIME_WORK

    companion object {
        fun newInstance(args: Bundle): TimeWorkFragment {
            val fragment: TimeWorkFragment = TimeWorkFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: TimeWorkPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_time_work, container, false)
    }
}
