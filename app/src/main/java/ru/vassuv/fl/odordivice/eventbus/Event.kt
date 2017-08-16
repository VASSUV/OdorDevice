package ru.vassuv.fl.odordivice.eventbus

import android.view.View

data class LoadProfileEvent(var status: Boolean = true, var msg: String = "")

class UpdateTokenEvent

data class PreloaderVisibilityEvent(var visibility: Int = View.GONE)
