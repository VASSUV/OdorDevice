package ru.vassuv.fl.odordivice.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.vassuv.fl.odordivice.R

class DeviceListAdapter : RecyclerView.Adapter<DeviceListAdapter.Holder>() {

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: Holder?, position: Int) {
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent?.context).inflate(R.layout.item_device, parent, false))
    }

    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        init {

        }
    }
}

