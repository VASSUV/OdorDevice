package ru.vassuv.fl.odordivice.ui.activity.main

import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_main.*
import ru.vassuv.fl.odordivice.R
import ru.vassuv.fl.odordivice.fabric.IFragment
import ru.vassuv.fl.odordivice.presentation.presenter.main.MainPresenter
import ru.vassuv.fl.odordivice.presentation.view.main.MainView
import ru.vassuv.fl.odordivice.service.Bluetooth
import ru.vassuv.fl.odordivice.service.Statistics
import android.widget.TextView
import android.support.design.widget.Snackbar
import android.support.v7.app.ActionBar
import android.view.View
import android.widget.ImageView
import org.jetbrains.anko.act
import ru.vassuv.fl.odordivice.utils.KeyboardUtils


class MainActivity : MvpAppCompatActivity(), MainView {
    @InjectPresenter
    lateinit var presenter: MainPresenter

    private var snackbar: Snackbar? = null

    private var snackTextView: TextView? = null

    private var titleView: TextView? = null
    private var backButton: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.onCreate(supportFragmentManager, savedInstanceState ?: Bundle())

        Bluetooth.startActivityLambda = {
            Statistics.send("startActivityLambda " + it.toString() )
            startActivityForResult(it, Bluetooth.REQUEST_ENABLE_BT)
        }

//        Gatt.sendBroadcastLambda = {
//            Statistics.send("sendBroadcastLambda " + it.toString())
//
//            // Регистрируем BroadcastReceiver
//            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
//            registerReceiver(it, filter)
//        }

        Bluetooth.check()

        snackbar = Snackbar.make(findViewById<View>(android.R.id.content), "", Snackbar.LENGTH_LONG)
        val layout = snackbar?.view as Snackbar.SnackbarLayout
        layout.setPadding(0, 0, 0, 0)
        layout.findViewById<View>(android.support.design.R.id.snackbar_text).visibility = View.INVISIBLE
        layout.findViewById<View>(android.support.design.R.id.snackbar_action).visibility = View.INVISIBLE
        snackTextView = layoutInflater.inflate(R.layout.snackbar, null) as TextView
        layout.addView(snackTextView, 0)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM;
        supportActionBar?.setCustomView(R.layout.actionbar)

        titleView = findViewById<TextView>(resources.getIdentifier("action_bar_title", "id", packageName))
        backButton = findViewById<ImageView>(resources.getIdentifier("action_bar_back", "id", packageName))
        backButton?.setOnClickListener({ onBackPressed() })
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
//        Gatt.close()
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
        if (Bluetooth.REQUEST_ENABLE_BT == requestCode) {
            (supportFragmentManager.fragments.last() as? IFragment)?.onResult(data?.extras ?: Bundle())
        }
    }

    override fun setTitle(titleId: Int) {
        titleView?.text = applicationContext.getString(titleId)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        presenter.onSaveInstanceState(outState)
    }

    override fun unSetElevation() {
        supportActionBar?.elevation = 0f
    }

    override fun setElevation() {
        supportActionBar?.elevation = resources.getDimension(R.dimen.elevation)
    }

    override fun showMessage(text: String) {
        if (text.isEmpty()) return
        snackTextView?.text = text
        snackbar?.show()
    }

    override fun hideKeyBoard() {
        KeyboardUtils.hideKeyboard(act)
    }

    override fun setVisibilityPreloader(visibility: Int) {
        preloader.visibility = visibility
        loader.visibility = visibility
        if (visibility == View.VISIBLE)
            loader.show()
        else
            loader.hide()
    }

    override fun showBackButton() {
        backButton?.visibility = View.VISIBLE
    }

    override fun hideBackButton() {
        backButton?.visibility = View.GONE
    }

}
