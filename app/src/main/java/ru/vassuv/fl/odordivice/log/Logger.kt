package ru.vassuv.fl.odordivice.log

import android.util.Log
import ru.vassuv.fl.odordivice.service.Statistics

class Logger(var debugMode: Boolean = false, private var tag: String = "MyInstagram") {

    fun trace(vararg args: Any) {
        if (debugMode) {
            val varargToString = varargToString(args)
            Log.d(tag, varargToString)
            Statistics.send(varargToString)
        }
    }

    fun traceException(text: String, exception: Throwable) {
        if (debugMode) {
            val logExc = "$text: $exception"
            Log.d(tag, logExc)
            Statistics.send(logExc)
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
