package ru.vassuv.fl.odordivice.presentation.presenter.device

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newSingleThreadContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.vassuv.fl.odordivice.App
import ru.vassuv.fl.odordivice.eventbus.FindNewDeviceEvent
import ru.vassuv.fl.odordivice.eventbus.PreloaderVisibilityEvent
import ru.vassuv.fl.odordivice.fabric.FrmFabric
import ru.vassuv.fl.odordivice.presentation.view.device.DevicesListView
import ru.vassuv.fl.odordivice.service.BLibrary
import ru.vassuv.fl.odordivice.service.Bluetooth
import ru.vassuv.fl.odordivice.ui.adapter.LeDeviceListAdapter
import ru.vassuv.fl.odordivice.ui.adapter.LeDeviceListAdapter.IClickListener

@InjectViewState
class DevicesListPresenter : MvpPresenter<DevicesListView>() {

    private val adapter = LeDeviceListAdapter()

    var visibilityLoader = false
    var isLoader = true
    var isAbortScanning = false

    fun onCreate() {
        visibilityLoader = true
        adapter.iClickListener = object : IClickListener {
            override fun itemClick(device: BluetoothDevice?) {
                if (device != null) {
                    BLibrary.device = device
                    App.router.navigateTo(FrmFabric.PASSWORD.name)
                } else {
                    App.router.showSystemMessage("Не удается подключиться к этому устройству", 0)
                }
            }
        }

        BLibrary.service.setOnScanCallback(object : BluetoothService.OnBluetoothScanCallback {
            override fun onStopScan() {
                if (isLoader && !isAbortScanning || isAbortScanning) {
                    EventBus.getDefault().post(PreloaderVisibilityEvent(View.GONE))
                } else if (!isAbortScanning){
                    viewState.setRefreshVisibility(false)
                }
                isAbortScanning = false
            }

            override fun onStartScan() {
                if (isLoader && adapter.getDeviceCount() == 0)
                    EventBus.getDefault().post(PreloaderVisibilityEvent(View.VISIBLE))
            }

            override fun onDeviceDiscovered(device: BluetoothDevice?, p1: Int) {
                if (device == null) return

                EventBus.getDefault().post(FindNewDeviceEvent(device))
                if (visibilityLoader) {
                    visibilityLoader = false
                    EventBus.getDefault().post(PreloaderVisibilityEvent(View.GONE))
                }
            }
        })
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
        println("--------------------------------------")
    }

    fun onStop() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }

    private fun refreshDeviceList(isLoader: Boolean = false) {
        Bluetooth.check()

        this.isLoader = isLoader

        adapter.clear()

        isAbortScanning = true
        BLibrary.service.stopScan()

        async(newSingleThreadContext("scanDevice")) {
            BLibrary.service.startScan()
        }
    }

    fun getAdapter() = adapter

    fun getOnRefreshListener() = SwipeRefreshLayout.OnRefreshListener {
        refreshDeviceList()
    }

    fun onResult(data: Bundle) {
        refreshDeviceList(true)
    }

    fun onPause() {
        BLibrary.service.stopScan()
    }
}