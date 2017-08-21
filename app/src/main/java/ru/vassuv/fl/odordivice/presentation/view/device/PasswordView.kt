package ru.vassuv.fl.odordivice.presentation.view.device

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SingleStateStrategy::class)
interface PasswordView : MvpView {
    fun requestPasswordView()
}
