package ru.vassuv.fl.odordivice.log

import android.util.Log

class Logger(var debugMode: Boolean = false, private var tag: String = "MyInstagram") {

    fun trace(vararg args: Any) {
        if (debugMode) Log.d(tag, varargToString(args))
    }

    fun traceException(text: String, exception: Throwable) {
        if (debugMode) {
            Log.d(tag, "$text: $exception")
            exception.stackTrace.forEach { Log.d(tag, ("|   " + it.toString())) }
        }
    }

    private fun varargToString(args: Array<*>): String {
        return args.joinToString(separator = " ") {
            when (it) {
                is Array<*> -> varargToString(it)
                else -> it.toString()
            }
        }
    }
}
