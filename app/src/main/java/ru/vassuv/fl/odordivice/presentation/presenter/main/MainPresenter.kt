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
import org.jetbrains.anko.bundleOf
import ru.vassuv.fl.odordivice.App
import ru.vassuv.fl.odordivice.R
import ru.vassuv.fl.odordivice.eventbus.PreloaderVisibilityEvent
import ru.vassuv.fl.odordivice.fabric.FrmFabric
import ru.vassuv.fl.odordivice.fabric.IFragment
import ru.vassuv.fl.odordivice.presentation.view.main.MainView
import ru.vassuv.fl.odordivice.repository.constant.Field
import ru.vassuv.fl.odordivice.router.CustomNavigator
import ru.vassuv.fl.odordivice.utils.KeyboardUtils
import java.io.Serializable


@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {
    private val STATE_SCREEN_NAMES = "state_screen_names"
    private lateinit var navigator: CustomNavigator
    private var currentType = FrmFabric.EMPTY

    private lateinit var fragmentManager: FragmentManager

    fun onCreate(fragmentManager: FragmentManager, savedInstanceState: Bundle) {

        this.fragmentManager = fragmentManager

        App.router.setOnNewRootScreenListener {
            fun onChangeRootScreen(screenKey: String) = Unit
        }
        App.router.setOnBackScreenListener {
            fun onBackScreen() = Unit
        }

        navigator = object : CustomNavigator(fragmentManager, R.id.content, OnChangeFragmentListener(this::onChangeFragment)) {
            override fun createFragment(screenKey: String?, data: Any?): Fragment {
                viewState.setVisibilityPreloader(View.GONE)
                return FrmFabric.valueOf(screenKey as String)
                        .create(data as? Bundle ?: bundleOf(Field.DATA to data.toString())) as Fragment
            }

            override fun exit() {
                viewState.finish()
                viewState.setVisibilityPreloader(View.GONE)
            }

//            override fun openFragment(name: String) {
//                currentType = FrmFabric.valueOf(name)
//            }

            override fun showSystemMessage(message: String?, type: Int) = viewState.showMessage(message.toString())
            override fun getEnterAnimation(s: String, s1: String): Int = 0
            override fun getExitAnimation(s: String, s1: String): Int = 0
            override fun getPopEnterAnimation(s: String, s1: String): Int = 0
            override fun getPopExitAnimation(s: String, s1: String): Int = 0
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

        val newFragmentType = getCurrentFragment()?.type
        if (currentType == newFragmentType || newFragmentType == null) return
        currentType = newFragmentType

        when (currentType) {
            FrmFabric.DEVICE_LIST -> {
                viewState.setTitle(R.string.title_device_list)
                viewState.hideBackButton()
                viewState.setElevation()
            }
            FrmFabric.PASSWORD -> {
                viewState.setTitle(R.string.title_password)
                viewState.showBackButton()
                viewState.setElevation()
            }
            FrmFabric.CHANGE_PASSWORD -> {
                viewState.setTitle(R.string.title_change_password)
                viewState.showBackButton()
                viewState.setElevation()
            }
            FrmFabric.TIME_WORK -> {
                viewState.setTitle(R.string.title_time_work)
                viewState.showBackButton()
                viewState.setElevation()
            }
            FrmFabric.CONTROL -> {
                viewState.setTitle(R.string.title_control)
                viewState.showBackButton()
                viewState.unSetElevation()
            }
            else -> {
            }
        }
    }
    fun getCurrentFragment() : IFragment?  {
        return fragmentManager.findFragmentById(R.id.content) as IFragment?
    }

    private fun startFirstFragment() {
        App.router.newRootScreen(FrmFabric.DEVICE_LIST.name)
    }

    fun onSaveInstanceState(outState: Bundle?) {
        outState?.putSerializable(STATE_SCREEN_NAMES, navigator.screenNames as Serializable)
    }
}