package ru.vassuv.fl.odordivice.ui.adapter

import android.bluetooth.BluetoothDevice
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.vassuv.fl.odordivice.R
import ru.vassuv.fl.odordivice.service.Statistics
import ru.vassuv.fl.odordivice.ui.component.EazyHolder

class LeDeviceListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val BLANK_TYPE = 0
    val ITEM_TYPE = 1

    internal var iClickListener: IClickListener? = null
        set(value) {
            field = value
        }

    private val mLeDevices: ArrayList<BluetoothDevice> = ArrayList<BluetoothDevice>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == BLANK_TYPE)
            return EazyHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_refresh_blank, parent, false))
        return Holder(LayoutInflater.from(parent?.context).inflate(R.layout.item_device, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && mLeDevices.size == 0) BLANK_TYPE else ITEM_TYPE
    }

    override fun getItemCount() = if (mLeDevices.size == 0) 1 else mLeDevices.size

    fun getDeviceCount() = mLeDevices.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (position == 0 && mLeDevices.size == 0 || holder !is Holder?) return

        val device = mLeDevices[position]
        val deviceName = device.name
        if (deviceName != null && deviceName.isNotEmpty())
            holder?.deviceName!!.text = deviceName
        else
            holder?.deviceName!!.setText(R.string.unknown_device)
        holder?.deviceAddress!!.text = device.address

    }

    fun addDevice(device: BluetoothDevice) {
        if (!mLeDevices.contains(device)) {
            Statistics.send("Найден: ", device)
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
        fun itemClick(device: BluetoothDevice?)

    }
}