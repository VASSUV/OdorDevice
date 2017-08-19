package ru.vassuv.fl.odordivice.ui.activity.main

import android.app.Instrumentation
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_main.*
import ru.vassuv.fl.odordivice.R
import ru.vassuv.fl.odordivice.fabric.IFragment
import ru.vassuv.fl.odordivice.presentation.presenter.main.MainPresenter
import ru.vassuv.fl.odordivice.presentation.view.main.MainView
import ru.vassuv.fl.odordivice.service.Bluetooth
import ru.vassuv.fl.odordivice.service.Gatt
import ru.vassuv.fl.odordivice.service.Statistics


class MainActivity : MvpAppCompatActivity(), MainView {
    @InjectPresenter
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.onCreate(supportFragmentManager, savedInstanceState ?: Bundle())

        Bluetooth.startActivityLambda = {
            Statistics.send("startActivityLambda " + it.toString())
            startActivityForResult(it, Bluetooth.REQUEST_ENABLE_BT)
        }

        Gatt.sendBroadcastLambda = {
            Statistics.send("sendBroadcastLambda " + it.toString())

            // Регистрируем BroadcastReceiver
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(it, filter)
        }

        Bluetooth.check()

    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        Gatt.close()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.content)
        if (fragment != null && fragment is IFragment) {
            (fragment as IFragment).onBackPressed()
        } else {
            super.onBackPressed()
        }

        if (supportFragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(Bluetooth.REQUEST_ENABLE_BT == requestCode){
            (supportFragmentManager.fragments.last() as? IFragment) ?. onResult(data?.extras ?: Bundle())
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        presenter.onSaveInstanceState(outState)
    }

    override fun showMessage(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG)
    }

    override fun setVisibilityPreloader(visibility: Int) {
        progressBar.visibility = visibility
    }
}
