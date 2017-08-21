package ru.vassuv.fl.odordivice.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import java.util.*

class TimePickerDialog() : DialogFragment() {

    var listener: android.app.TimePickerDialog.OnTimeSetListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hourOfDay = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val timePickerDialog = android.app.TimePickerDialog(
                activity, // Context
                listener, // Listener
                hourOfDay, // hourOfDay
                minute, // Minute
                true // is24HourView
        )
        return timePickerDialog
    }
}
