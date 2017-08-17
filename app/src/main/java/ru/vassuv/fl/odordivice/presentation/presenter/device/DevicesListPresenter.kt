package ru.vassuv.fl.odordivice.presentation.presenter.device

import android.support.v7.widget.RecyclerView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.vassuv.fl.odordivice.App
import ru.vassuv.fl.odordivice.presentation.view.device.DevicesListView
import ru.vassuv.fl.odordivice.service.Bluetooth
import ru.vassuv.fl.odordivice.service.Statistics
import ru.vassuv.fl.odordivice.ui.adapter.DeviceListAdapter

@InjectViewState
class DevicesListPresenter : MvpPresenter<DevicesListView>() {

    val adapter = DeviceListAdapter()

    fun onStart() {
        App.log("Открыт список устройств")
        Bluetooth.check()
        adapter.notifyDataSetChanged()
    }

    fun onStop() {

    }

    fun getAdapter(): RecyclerView.Adapter<*>? {
        return adapter
    }

}


//class MainActivity : AppCompatActivity() {
//
//    private var mBluetoothAdapter: BluetoothAdapter? = null
//    private var mBluetoothLeScanner: BluetoothLeScanner? = null
//
//    private var mScanning: Boolean = false
//
//
//    internal var listBluetoothDevice: MutableList<BluetoothDevice>
//    internal var adapterLeScanResult: ListAdapter
//
//    private var mHandler: Handler? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        // Check if BLE is supported on the device.
//        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
//            Toast.makeText(this,
//                    "BLUETOOTH_LE not supported in this device!",
//                    Toast.LENGTH_SHORT).show()
//            finish()
//        }
//
//        getBluetoothAdapterAndLeScanner()
//
//        // Checks if Bluetooth is supported on the device.
//        if (mBluetoothAdapter == null) {
//            Toast.makeText(this,
//                    "bluetoothManager.getAdapter()==null",
//                    Toast.LENGTH_SHORT).show()
//            finish()
//            return
//        }
//
//        btnScan = findViewById(R.id.scan) as Button
//        btnScan.setOnClickListener(object : View.OnClickListener() {
//            fun onClick(v: View) {
//                scanLeDevice(true)
//            }
//        })
//        listViewLE = findViewById(R.id.lelist) as ListView
//
//        listBluetoothDevice = ArrayList()
//        adapterLeScanResult = ArrayAdapter(
//                this, android.R.layout.simple_list_item_1, listBluetoothDevice)
//        listViewLE.setAdapter(adapterLeScanResult)
//        listViewLE.setOnItemClickListener(scanResultOnItemClickListener)
//
//        mHandler = Handler()
//
//    }
//
//    internal var scanResultOnItemClickListener: AdapterView.OnItemClickListener = object : AdapterView.OnItemClickListener {
//
//        fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//            val device = parent.getItemAtPosition(position) as BluetoothDevice
//
//            val msg = device.address + "\n"
//            +device.bluetoothClass.toString() + "\n"
//            +getBTDevieType(device)
//
//            AlertDialog.Builder(this@MainActivity)
//                    .setTitle(device.name)
//                    .setMessage(msg)
//                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which -> })
//                    .show()
//
//        }
//    }
//
//    private fun getBTDevieType(d: BluetoothDevice): String {
//        var type = ""
//
//        when (d.type) {
//            BluetoothDevice.DEVICE_TYPE_CLASSIC -> type = "DEVICE_TYPE_CLASSIC"
//            BluetoothDevice.DEVICE_TYPE_DUAL -> type = "DEVICE_TYPE_DUAL"
//            BluetoothDevice.DEVICE_TYPE_LE -> type = "DEVICE_TYPE_LE"
//            BluetoothDevice.DEVICE_TYPE_UNKNOWN -> type = "DEVICE_TYPE_UNKNOWN"
//            else -> type = "unknown..."
//        }
//
//        return type
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//        if (!mBluetoothAdapter!!.isEnabled) {
//            if (!mBluetoothAdapter!!.isEnabled) {
//                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//                startActivityForResult(enableBtIntent, RQS_ENABLE_BLUETOOTH)
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//
//        if (requestCode == RQS_ENABLE_BLUETOOTH && resultCode == Activity.RESULT_CANCELED) {
//            finish()
//            return
//        }
//
//        getBluetoothAdapterAndLeScanner()
//
//        // Checks if Bluetooth is supported on the device.
//        if (mBluetoothAdapter == null) {
//            Toast.makeText(this,
//                    "bluetoothManager.getAdapter()==null",
//                    Toast.LENGTH_SHORT).show()
//            finish()
//            return
//        }
//
//        super.onActivityResult(requestCode, resultCode, data)
//    }
//
//    private fun getBluetoothAdapterAndLeScanner() {
//        // Get BluetoothAdapter and BluetoothLeScanner.
//        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
//        mBluetoothAdapter = bluetoothManager.adapter
//        mBluetoothLeScanner = mBluetoothAdapter!!.bluetoothLeScanner
//
//        mScanning = false
//    }
//
//    /*
//    to call startScan (ScanCallback callback),
//    Requires BLUETOOTH_ADMIN permission.
//    Must hold ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION permission to get results.
//     */
//    private fun scanLeDevice(enable: Boolean) {
//        if (enable) {
//            listBluetoothDevice.clear()
//            listViewLE.invalidateViews()
//
//            // Stops scanning after a pre-defined scan period.
//            mHandler!!.postDelayed(Runnable {
//                mBluetoothLeScanner!!.stopScan(scanCallback)
//                listViewLE.invalidateViews()
//
//                Toast.makeText(this@MainActivity,
//                        "Scan timeout",
//                        Toast.LENGTH_LONG).show()
//
//                mScanning = false
//                btnScan.setEnabled(true)
//            }, SCAN_PERIOD)
//
//            //mBluetoothLeScanner.startScan(scanCallback);
//
//            //scan specified devices only with ScanFilter
//            val scanFilter = ScanFilter.Builder()
//                    .setServiceUuid(BluetoothLeService.ParcelUuid_GENUINO101_ledService)
//                    .build()
//            val scanFilters = ArrayList<ScanFilter>()
//            scanFilters.add(scanFilter)
//
//            val scanSettings = ScanSettings.Builder().build()
//
//            mBluetoothLeScanner!!.startScan(scanFilters, scanSettings, scanCallback)
//
//            mScanning = true
//            btnScan.setEnabled(false)
//        } else {
//            mBluetoothLeScanner!!.stopScan(scanCallback)
//            mScanning = false
//            btnScan.setEnabled(true)
//        }
//    }
//
//    private val scanCallback = object : ScanCallback() {
//        fun onScanResult(callbackType: Int, result: ScanResult) {
//            super.onScanResult(callbackType, result)
//
//            addBluetoothDevice(result.getDevice())
//        }
//
//        fun onBatchScanResults(results: List<ScanResult>) {
//            super.onBatchScanResults(results)
//            for (result in results) {
//                addBluetoothDevice(result.getDevice())
//            }
//        }
//
//        override fun onScanFailed(errorCode: Int) {
//            super.onScanFailed(errorCode)
//            Toast.makeText(this@MainActivity,
//                    "onScanFailed: " + errorCode.toString(),
//                    Toast.LENGTH_LONG).show()
//        }
//
//        private fun addBluetoothDevice(device: BluetoothDevice) {
//            if (!listBluetoothDevice.contains(device)) {
//                listBluetoothDevice.add(device)
//                listViewLE.invalidateViews()
//            }
//        }
//    }
//
//    companion object {
//
//        private val RQS_ENABLE_BLUETOOTH = 1
//        private val SCAN_PERIOD: Long = 10000
//    }
//}