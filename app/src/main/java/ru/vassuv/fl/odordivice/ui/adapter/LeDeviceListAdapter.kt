package ru.vassuv.fl.odordivice.ui.adapter

import android.bluetooth.BluetoothDevice
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.vassuv.fl.odordivice.R

class LeDeviceListAdapter : RecyclerView.Adapter<LeDeviceListAdapter.Holder>() {

    internal var iClickListener: IClickListener? = null
        set(value) {
            field = value
        }

    private val mLeDevices: ArrayList<BluetoothDevice> = ArrayList<BluetoothDevice>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent?.context).inflate(R.layout.item_device, parent, false))
    }

    override fun getItemCount() = mLeDevices.size

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        val device = mLeDevices[position]
        val deviceName = device.name
        if (deviceName != null && deviceName.isNotEmpty())
            holder?.deviceName!!.text = deviceName
        else
            holder?.deviceName!!.setText(R.string.unknown_device)
        holder.deviceAddress!!.text = device.address

    }

    fun addDevice(device: BluetoothDevice) {
        if (!mLeDevices.contains(device)) {
            mLeDevices.add(device)
        }
    }

    fun getDevice(position: Int): BluetoothDevice? {
        return mLeDevices[position]
    }

    fun clear() {
        mLeDevices.clear()
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var deviceName: TextView? = null
        var deviceAddress: TextView? = null

        init {
            deviceName = itemView?.findViewById(R.id.deviceName)
            deviceAddress = itemView?.findViewById(R.id.deviceAddress)
            itemView?.findViewById<View>(R.id.list_item)?.setOnClickListener {
                iClickListener?.itemClick(mLeDevices[layoutPosition])
            }
        }

    }

    interface IClickListener {
        fun itemClick(device: BluetoothDevice)
    }
}