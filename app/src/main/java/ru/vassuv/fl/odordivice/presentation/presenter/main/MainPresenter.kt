package ru.vassuv.fl.odordivice.presentation.presenter.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.terrakok.cicerone.commands.Command
import ru.vassuv.fl.odordivice.App
import ru.vassuv.fl.odordivice.R
import ru.vassuv.fl.odordivice.eventbus.PreloaderVisibilityEvent
import ru.vassuv.fl.odordivice.fabric.FrmFabric
import ru.vassuv.fl.odordivice.fabric.IFragment
import ru.vassuv.fl.odordivice.presentation.view.main.MainView
import ru.vassuv.fl.odordivice.router.CustomNavigator
import java.io.Serializable

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {
    private val STATE_SCREEN_NAMES = "state_screen_names"
    private lateinit var navigator: CustomNavigator
    private var currentType = FrmFabric.EMPTY

    fun onCreate(fragmentManager: FragmentManager, savedInstanceState: Bundle) {

        App.router.setOnNewRootScreenListener {
            fun onChangeRootScreen(screenKey: String) = Unit
        }
        App.router.setOnBackScreenListener {
            fun onBackScreen() = Unit
        }

        navigator = object : CustomNavigator(fragmentManager, R.id.content, OnChangeFragmentListener {

        }) {

            override fun createFragment(screenKey: String?, data: Any?): Fragment {
                viewState.setVisibilityPreloader(View.GONE)
                return FrmFabric.valueOf(screenKey as String).create(data) as Fragment
            }

            override fun showSystemMessage(message: String?, type: Int) = viewState.showMessage(message.toString())

//            override fun openFragment(name: String) {
//                currentType = FrmFabric.valueOf(name)
//            }

            override fun exit() {
                viewState.finish()
                viewState.setVisibilityPreloader(View.GONE)
            }

            override fun  getEnterAnimation(s : String, s1: String): Int = 0
            override fun  getExitAnimation(s : String, s1: String): Int = 0
            override fun  getPopEnterAnimation(s : String, s1: String): Int = 0
            override fun  getPopExitAnimation(s : String, s1: String): Int = 0
            }
        App.setNavigationHolder(navigator)

        if (savedInstanceState.isEmpty) {
            startFirstFragment()
        } else {
            navigator.screenNames = savedInstanceState.getSerializable(STATE_SCREEN_NAMES) as MutableList<String>
        }
    }

    fun onStart() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PreloaderVisibilityEvent) {
        viewState.setVisibilityPreloader(event.visibility)
    }

    fun onStop() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }

    fun onChangeFragment() {
        when (currentType) {
            FrmFabric.DEVICE_LIST -> {
                viewState.setTitle(R.string.title_device_list)
            }
            else -> {
            }
        }
    }

    private fun startFirstFragment() {
        App.router.newRootScreen(FrmFabric.DEVICE_LIST.name)
    }

    fun onSaveInstanceState(outState: Bundle?) {
        outState?.putSerializable(STATE_SCREEN_NAMES, navigator.screenNames as Serializable)
    }
}