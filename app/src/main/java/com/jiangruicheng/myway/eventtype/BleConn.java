package com.jiangruicheng.myway.eventtype;

import android.bluetooth.BluetoothDevice;

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
}
