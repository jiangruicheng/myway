package com.jiangruicheng.myway.ble;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.jiangruicheng.myway.RXbus.RxBus;
import com.jiangruicheng.myway.data.Command;
import com.jiangruicheng.myway.eventtype.ConnSucc;
import com.jiangruicheng.myway.eventtype.DisBleConn;
import com.jiangruicheng.myway.eventtype.ReciveCmd;
import com.jiangruicheng.myway.util.Quee;

import java.util.List;
import java.util.UUID;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * XIU GAI WAN CHENG
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class ConnBle {
    private UUID                        uuid_service;
    private UUID                        uuid_characteristic;
    private UUID                        uuid_getdata;
    //private BluetoothDevice device;
    // private Context context;
    private BluetoothGattCharacteristic characteristic;
    private BluetoothGattCharacteristic getdata;
    private BluetoothGattService        gattService;
    BluetoothGatt gatt;
    private ReciveCmd reciveCmd = new ReciveCmd();
    private Subscription SendCmd;
    private Subscription disconn;

    public ConnBle() {
        disconn = RxBus.getDefault().toObservable(DisBleConn.class).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DisBleConn>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(DisBleConn disBleConn) {
                if (gatt != null) {
                    gatt.disconnect();
                    Quee.getDefault().onstop();
                }
            }
        });
        if (SendCmd == null) {
            SendCmd = RxBus.getDefault().toObservable(com.jiangruicheng.myway.eventtype.SendCmd.class).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<com.jiangruicheng.myway.eventtype.SendCmd>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(com.jiangruicheng.myway.eventtype.SendCmd sendCmd) {
                    try {
                        sendCmd.getCmd();
                        write(sendCmd.getCmd());
                        Log.i("send", "onNext: " + sendCmd.getCmd().toString());

                    } catch (Exception e) {
                        Log.d("send", "onNext: " + e.getMessage());
                    }
                }
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public BluetoothGatt connble(BluetoothDevice device, Context context, UUID service, UUID characteristic) {
        uuid_service = service;
        uuid_characteristic = characteristic;
        gatt = device.connectGatt(context, false, gattCallback);
        //getserver(gatt);
        return gatt;

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void getserver(BluetoothGatt gatt) {
        /*BluetoothGattService service = gatt.getService(uuid_service);
        characteristic = service.getCharacteristic(uuid_characteristic);
        getdata = service.getCharacteristic(uuid_getdata);
        gatt.setCharacteristicNotification(getdata, true);*/
        List<BluetoothGattService> list;
        list = gatt.getServices();
        if (uuid_service == null || uuid_characteristic == null) {
            return;
        }
        for (BluetoothGattService gattService : list) {
            if (gattService.getUuid().equals(uuid_service)) {
                this.gattService = gattService;
                characteristic = gattService.getCharacteristic(uuid_characteristic);
                for (BluetoothGattCharacteristic characteristic : gattService.getCharacteristics()) {
                    gatt.setCharacteristicNotification(characteristic, true);
                }
                gatt.setCharacteristicNotification(characteristic, true);
                RxBus.getDefault().post(new ConnSucc());
                Log.d("ble", "getserver: success");
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Quee.getDefault().onstart();
                    }
                }.start();
                // characteristic.setValue(new byte[]{0x42, 0x54, 0x02, 0x17, 0x01, 0x51});//41 54 02 17 00 52
                // gatt.writeCharacteristic(characteristic);
            }
            /*Log.d("TAG", "getserver: " + Integer.toHexStIntegerring(Integer.valueOf(gattService.getUuid().toString())));*/
        }

    }


    public void write(byte[] cmd) {
        characteristic.setValue(cmd);//41 54 02 17 00 52
        gatt.writeCharacteristic(characteristic);
        for (byte b : cmd) {
            String a = Integer.toHexString(b);
            Log.d("cmd", "cmd: " + Integer.toHexString(b));
        }
    }

    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.i("TAG", "onCharacteristicRead: " + characteristic.getValue());
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.i("TAG", "onCharacteristicWrite: " + characteristic.getValue());
        }

        byte[] by = new byte[54];

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
/*
            handlerCmd.handler(characteristic.getValue());
*/
            if (characteristic.getValue()[3] < 20 && characteristic.getValue()[0] == Command.HEAD_LOW && characteristic.getValue()[1] == Command.HEAD_HEIGHT) {
                reciveCmd.setCmd(characteristic.getValue());
                RxBus.getDefault().post(reciveCmd);
            }
            for (int i = 0; i < characteristic.getValue().length; i++) {
                Log.d("data" + i, Integer.toHexString(characteristic.getValue()[i]));
            }
            //reciveCmd.setCmd(characteristic.getValue());
            if (characteristic.getValue()[3] > 20 && characteristic.getValue()[0] == Command.HEAD_LOW && characteristic.getValue()[1] == Command.HEAD_HEIGHT) {
                System.arraycopy(characteristic.getValue(), 0, by, 0, 20);
            }

            if (characteristic.getValue().length < 20 && characteristic.getValue()[0] != Command.HEAD_LOW && characteristic.getValue()[1] != Command.HEAD_HEIGHT) {
                System.arraycopy(characteristic.getValue(), 0, by, 40, characteristic.getValue().length);
                reciveCmd.setCmd(by);
                RxBus.getDefault().post(reciveCmd);
                /*byte[] type = new byte[4];
                System.arraycopy(by, 4, type, 0, 4);
                String s = new String(type);
                Log.d("chartype", "onCharacteristicChanged: " + new String(type));
                byte[] classic = new byte[8];
                String a       = new String(type);
                Log.d("classic", "onCharacteristicChanged: " + new String(classic));
                System.arraycopy(by, 7, classic, 0, 8);*/
            } else if (characteristic.getValue().length == 20 && characteristic.getValue()[0] != Command.HEAD_LOW && characteristic.getValue()[1] != Command.HEAD_HEIGHT) {
                System.arraycopy(characteristic.getValue(), 0, by, 20, 20);
            }


        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            getserver(gatt);
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                Log.d("BLE", "CONNECT");
                gatt.discoverServices();
                //RxBus.getDefault().post(new ConnSucc());
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                Log.d("BLE", "DISCONNECT");
                if (SendCmd.isUnsubscribed()) {
                    SendCmd.unsubscribe();
                    SendCmd = null;
                }
                if (disconn.isUnsubscribed()) {
                    disconn.unsubscribe();
                    disconn = null;
                }
            }
        }
    };
}
