package ru.vassuv.fl.odordivice.presentation.presenter.device

import android.bluetooth.*
import android.support.v7.widget.RecyclerView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.vassuv.fl.odordivice.App
import ru.vassuv.fl.odordivice.presentation.view.device.DevicesListView
import ru.vassuv.fl.odordivice.service.Bluetooth
import ru.vassuv.fl.odordivice.ui.adapter.LeDeviceListAdapter
import ru.vassuv.fl.odordivice.ui.adapter.LeDeviceListAdapter.IClickListener
import ru.vassuv.fl.odordivice.service.Gatt


@InjectViewState
class DevicesListPresenter : MvpPresenter<DevicesListView>() {

    val adapter = LeDeviceListAdapter()

    fun onCreate() {
        adapter.iClickListener = object : IClickListener {
            override fun itemClick(device: BluetoothDevice) {
                Gatt.connectDevice(device)
            }
        }

        Bluetooth.check()
        Bluetooth.scanCallBack = BluetoothAdapter.LeScanCallback { device, p1, p2 ->
            adapter.addDevice(device)
            adapter.notifyDataSetChanged()
        }
        Bluetooth.scanLeDevice(true)
        Bluetooth.sendStat()
    }

    fun onStart() {
    }

    fun onStop() {
    }

    fun getAdapter(): RecyclerView.Adapter<*>? {
        return adapter
    }
}


//class DeviceScanActivity : ListActivity() {
////    private var mLeDeviceListAdapter: LeDeviceListAdapter? = null
////    private var mBluetoothAdapter: BluetoothAdapter? = null
////    private var mScanning: Boolean = false
////    private var mHandler: Handler? = null
//
//    public override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        actionBar!!.setTitle(R.string.title_devices)
////        mHandler = Handler()
//
//        // Use this check to determine whether BLE is supported on the device.  Then you can
//        // selectively disable BLE-related features.
//        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
//            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show()
//            finish()
//        }
//
////        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
////        // BluetoothAdapter through BluetoothManager.
////        val bluetoothManager = getSystemService<Any>(Context.BLUETOOTH_SERVICE) as BluetoothManager
////        mBluetoothAdapter = bluetoothManager.adapter
//
//        // Checks if Bluetooth is supported on the device.
//        if (mBluetoothAdapter == null) {
//            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show()
//            finish()
//            return
//        }
//    }

//    fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.main, menu)
//        if (!mScanning) {
//            menu.findItem(R.id.menu_stop).setVisible(false)
//            menu.findItem(R.id.menu_scan).setVisible(true)
//            menu.findItem(R.id.menu_refresh).setActionView(null)
//        } else {
//            menu.findItem(R.id.menu_stop).setVisible(true)
//            menu.findItem(R.id.menu_scan).setVisible(false)
//            menu.findItem(R.id.menu_refresh).setActionView(
//                    R.layout.actionbar_indeterminate_progress)
//        }
//        return true
//    }

//    fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.getItemId()) {
//            R.id.menu_scan -> {
//                mLeDeviceListAdapter!!.clear()
//                scanLeDevice(true)
//            }
//            R.id.menu_stop -> scanLeDevice(false)
//        }
//        return true
//    }

//    override fun onResume() {
//        super.onResume()
//
//        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
//        // fire an intent to display a dialog asking the user to grant permission to enable it.
//        if (!mBluetoothAdapter!!.isEnabled) {
//            if (!mBluetoothAdapter!!.isEnabled) {
//                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
//            }
//        }
//
//        // Initializes list view adapter.
//        mLeDeviceListAdapter = LeDeviceListAdapter()
//        listAdapter = mLeDeviceListAdapter
//        scanLeDevice(true)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        // User chose not to enable Bluetooth.
//        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
//            finish()
//            return
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        scanLeDevice(false)
//        mLeDeviceListAdapter!!.clear()
//    }
//
//    protected fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
//        val device = mLeDeviceListAdapter!!.getDevice(position) ?: return
//        val intent = Intent(this, DeviceControlActivity::class.java)
//        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.name)
//        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.address)
//        if (mScanning) {
//            mBluetoothAdapter!!.stopLeScan(mLeScanCallback)
//            mScanning = false
//        }
//        startActivity(intent)
//    }
//
//    private fun scanLeDevice(enable: Boolean) {
//        if (enable) {
//            // Stops scanning after a pre-defined scan period.
//            mHandler!!.postDelayed(Runnable {
//                mScanning = false
//                mBluetoothAdapter!!.stopLeScan(mLeScanCallback)
//                invalidateOptionsMenu()
//            }, SCAN_PERIOD)
//
//            mScanning = true
//            mBluetoothAdapter!!.startLeScan(mLeScanCallback)
//        } else {
//            mScanning = false
//            mBluetoothAdapter!!.stopLeScan(mLeScanCallback)
//        }
//        invalidateOptionsMenu()
//    }
//}