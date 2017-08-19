package ru.vassuv.fl.odordivice.presentation.presenter.device

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.experimental.Deferred
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.vassuv.fl.odordivice.App
import ru.vassuv.fl.odordivice.eventbus.FindNewDeviceEvent
import ru.vassuv.fl.odordivice.eventbus.PreloaderVisibilityEvent
import ru.vassuv.fl.odordivice.fabric.FrmFabric
import ru.vassuv.fl.odordivice.presentation.view.device.DevicesListView
import ru.vassuv.fl.odordivice.service.Bluetooth
import ru.vassuv.fl.odordivice.service.Gatt
import ru.vassuv.fl.odordivice.ui.adapter.LeDeviceListAdapter
import ru.vassuv.fl.odordivice.ui.adapter.LeDeviceListAdapter.IClickListener

@InjectViewState
class DevicesListPresenter : MvpPresenter<DevicesListView>() {

    private val adapter = LeDeviceListAdapter()
    var visibilityLoader = false

    fun onCreate() {
        visibilityLoader = true
        adapter.iClickListener = object : IClickListener {
            override fun itemClick(device: BluetoothDevice?) {
//                Gatt.take(device)
                App.router.navigateTo(FrmFabric.PASSWORD.name)
            }
        }

        Bluetooth.sendStat()
        Bluetooth.scanCallBack = BluetoothAdapter.LeScanCallback { device, _, bytes ->
            EventBus.getDefault().post(FindNewDeviceEvent(device))
            if (visibilityLoader) {
                visibilityLoader = false
                EventBus.getDefault().post(PreloaderVisibilityEvent(View.GONE))
            }
        }
    }

    fun onStart() {
        refreshDeviceList(true)
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: FindNewDeviceEvent) {
        adapter.addDevice(event.device)
        adapter.notifyDataSetChanged()
    }

    fun onStop() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
        Gatt.close()
    }

    private var disposeScan: Deferred<Unit>? = null

    private fun refreshDeviceList(isLoader: Boolean = false) {
        Bluetooth.check()

        if (isLoader && adapter.itemCount == 0) EventBus.getDefault().post(PreloaderVisibilityEvent(View.VISIBLE))

        adapter.clear()
        disposeScan = Bluetooth.scanLeDevice(true) {
            if (isLoader) {
                EventBus.getDefault().post(PreloaderVisibilityEvent(View.GONE))
            } else {
                viewState.setRefreshVisibility(false)
            }
        }
    }

    fun getAdapter() = adapter

    fun getOnRefreshListener() = SwipeRefreshLayout.OnRefreshListener {
        this.refreshDeviceList()
    }

    fun onResult(data: Bundle) {
        refreshDeviceList()
    }

    fun onResume() {

    }

    fun onPause() {
        disposeScan?.cancel()
    }
}