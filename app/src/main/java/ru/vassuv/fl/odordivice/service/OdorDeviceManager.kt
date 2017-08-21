package ru.vassuv.fl.odordivice.service

object OdorDeviceManager {
    val READ = 0b10000000
    val WRITE = 0b01111111

    val NAME = 0x01
    val TIME = 0x02
    val REMOTE = 0x03
    val INTENSITY = 0x04
    val USER_TAG = 0x05
    val PCB_V = 0x06
    val MAC = 0x07
    val DEVICE_V = 0x08
    val DEVICE_N = 0x09
    val DIAGNOSTIC = 0x0A
    val FAN = 0x0B
    val LEVEL = 0x0D
    val FIRMWARE = 0x0E
    val PASS = 0x0F

    fun readName(x: ByteArray) = if (x.size > 2 && x[0] == (READ or NAME).toByte()) {
        val name: CharArray = kotlin.CharArray(x[1].toInt())
        (0..x[1]).forEach { name[it] = x[it].toChar() }
        name.toString()
    } else null
}