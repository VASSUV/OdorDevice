package ru.vassuv.fl.odordivice.service

import android.os.Build.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import org.json.JSONObject
import ru.vassuv.fl.odordivice.repository.Retrofit

object Statistics {
    val deviceId: String = "$ID.$DEVICE.$MANUFACTURER.$MODEL"

    fun send(message: String) {
        launch(CommonPool) {
            Retrofit.get(JSONObject()
                    .accumulate("device_id", deviceId)
                    .accumulate("message", message)
                    .toString()).execute().body()
        }
    }
}