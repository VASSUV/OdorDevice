package ru.vassuv.fl.odordivice.ui.fragment.control

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import ru.vassuv.fl.odordivice.App
import ru.vassuv.fl.odordivice.R
import ru.vassuv.fl.odordivice.fabric.FrmFabric

class AboutFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_about, container, false)
        rootView?.findViewById<Button>(R.id.button)?.setOnClickListener({
            App.router.navigateTo(FrmFabric.CHANGE_PASSWORD.name)
        })
        return rootView
    }
}