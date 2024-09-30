package com.soarespt.bluetoothlescanner

data class BluetoothDevice(
    var name: String,
    var macAddress: String,
    var rssi: Int,
    var isConnectable: Boolean
)