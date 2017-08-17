package ru.vassuv.fl.odordivice.presentation.presenter.device

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.support.v7.widget.RecyclerView
import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.greenrobot.eventbus.EventBus
import ru.vassuv.fl.odordivice.eventbus.PreloaderVisibilityEvent
import ru.vassuv.fl.odordivice.presentation.view.device.DevicesListView
import ru.vassuv.fl.odordivice.service.Bluetooth
import ru.vassuv.fl.odordivice.service.Gatt
import ru.vassuv.fl.odordivice.ui.adapter.LeDeviceListAdapter
import ru.vassuv.fl.odordivice.ui.adapter.LeDeviceListAdapter.IClickListener

@InjectViewState
class DevicesListPresenter : MvpPresenter<DevicesListView>() {

    val adapter = LeDeviceListAdapter()
    var visibilityLoader = false

    fun onCreate() {
        EventBus.getDefault().post(PreloaderVisibilityEvent(View.VISIBLE))
        visibilityLoader = true
        adapter.iClickListener = object : IClickListener {
            override fun itemClick(device: BluetoothDevice) {
                Gatt.connectDevice(device)
            }
        }

        Bluetooth.check()
        Bluetooth.scanCallBack = BluetoothAdapter.LeScanCallback { device, p1, p2 ->
            adapter.addDevice(device)
            adapter.notifyDataSetChanged()
            if (visibilityLoader) {
                visibilityLoader = false
                EventBus.getDefault().post(PreloaderVisibilityEvent(View.GONE))
            }
        }
        Bluetooth.scanLeDevice(true)
        Bluetooth.sendStat()
    }

    fun onStart() {
    }

    fun onStop() {
        Gatt.close()
    }

    fun getAdapter(): RecyclerView.Adapter<*>? {
        return adapter
    }
}