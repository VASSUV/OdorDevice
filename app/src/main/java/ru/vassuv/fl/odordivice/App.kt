package ru.vassuv.fl.odordivice

import android.app.Application
import android.content.Context
import ru.terrakok.cicerone.Cicerone
import ru.vassuv.fl.odordivice.log.Logger
import ru.vassuv.fl.odordivice.router.CustomNavigator
import ru.vassuv.fl.odordivice.router.CustomRouter

class App(
        val cicerone: Cicerone<CustomRouter> = Cicerone.create(CustomRouter()),
        val logger: Logger = Logger()) : Application() {

    companion object {
        private lateinit var app: App

        val router: CustomRouter
            get() = app.cicerone.router

        val context: Context
            get() = app.applicationContext

        fun log(vararg args: Any?) = app.logger.trace(args)
        fun logExc(text: String, exception: Throwable) = app.logger.traceException(text, exception)
        fun setNavigationHolder(navigator: CustomNavigator) = app.cicerone.navigatorHolder.setNavigator(navigator)
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        logger.debugMode = BuildConfig.DEBUG
    }
}
//
//fun Throwable.parse(): String {
//    App.logExc("ThrowableConverter", this)
//    return when (this) {
//        is UnknownHostException -> getMsg()
//        is HttpException -> getMsg()
//        else -> ""
//    }
//}
//
//class HttpException {
//
//}
//
//fun UnknownHostException.getMsg() = ""
//
//fun HttpException.getMsg() = ""
