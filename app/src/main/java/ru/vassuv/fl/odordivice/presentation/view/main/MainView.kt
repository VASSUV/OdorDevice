package ru.vassuv.fl.odordivice.presentation.view.main

import com.arellomobile.mvp.MvpView

interface MainView : MvpView {
    fun showMessage(text: String)

    fun setVisibilityPreloader(visibility: Int)

    fun finish()

    fun setTitle(titleId: Int)

    fun showBackButton()

    fun hideBackButton()

    fun hideKeyBoard()

    fun setElevation()

    fun unSetElevation()
}
