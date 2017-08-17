package ru.vassuv.fl.odordivice.ui.fragment.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_devices_list.view.*
import ru.vassuv.fl.odordivice.R
import ru.vassuv.fl.odordivice.fabric.FrmFabric
import ru.vassuv.fl.odordivice.fabric.IFragment
import ru.vassuv.fl.odordivice.presentation.presenter.device.DevicesListPresenter
import ru.vassuv.fl.odordivice.presentation.view.device.DevicesListView
import ru.vassuv.fl.odordivice.ui.component.DividerDecoration
import android.support.v7.widget.LinearLayoutManager

class DevicesListFragment : MvpAppCompatFragment(), DevicesListView, IFragment {
    override val type: FrmFabric
        get() = FrmFabric.DEVICE_LIST

    companion object {
        fun newInstance(args: Bundle): DevicesListFragment {
            val fragment: DevicesListFragment = DevicesListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: DevicesListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_devices_list, container, false)
        val recyclerView = rootView.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = presenter.getAdapter()
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.addItemDecoration(DividerDecoration(context, R.drawable.divider, 32))
        return rootView
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }
}
