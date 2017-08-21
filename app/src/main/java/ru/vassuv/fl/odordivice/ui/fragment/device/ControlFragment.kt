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
import android.support.design.widget.TabLayout
import kotlinx.android.synthetic.main.fragment_control.view.*
import ru.vassuv.fl.odordivice.ui.adapter.PagerAdapter
import ru.vassuv.fl.odordivice.utils.KeyboardUtils


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_control, container, false)
        val tabLayout = rootView.tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Установки"))
        tabLayout.addTab(tabLayout.newTab().setText("Об устройстве"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val viewPager = rootView.viewPager
        val adapter = PagerAdapter(childFragmentManager)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                KeyboardUtils.hideKeyboard(activity)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) = Unit
            override fun onTabReselected(tab: TabLayout.Tab) = Unit
        })
        return rootView
    }
}
