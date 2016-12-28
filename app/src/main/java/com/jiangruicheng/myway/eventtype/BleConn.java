package com.jiangruicheng.myway.eventtype;

import android.bluetooth.BluetoothDevice;

import java.util.UUID;

/**
 * Created by jiang_ruicheng on 16/12/20.
 */
public class BleConn {
    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public BleConn setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
        return this;
    }

    private BluetoothDevice bluetoothDevice;
    private UUID service;

    public UUID getCharacteristic() {
        return characteristic;
    }

    public BleConn setCharacteristic(UUID characteristic) {
        this.characteristic = characteristic;
        return this;
    }

    public UUID getService() {
        return service;
    }

    public BleConn setService(UUID service) {
        this.service = service;
        return this;
    }

    private UUID characteristic;
}
