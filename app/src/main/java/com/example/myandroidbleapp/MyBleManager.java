package com.example.myandroidbleapp;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.UUID;

import no.nordicsemi.android.ble.BleManager;
import no.nordicsemi.android.ble.PhyRequest;
import no.nordicsemi.android.ble.data.Data;

public class MyBleManager extends BleManager {
    final static UUID SERVICE_UUID = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
    final static UUID FIRST_CHAR   = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");

    private BluetoothGattCharacteristic firstCharacteristic;

    public MyBleManager(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected BleManagerGattCallback getGattCallback() {
        return new MyManagerGattCallback();
    }

    private HeartRateDataCallback heartRateDataCallback = new HeartRateDataCallback() {
        @Override
        public void onHeartRateChanged(@NonNull BluetoothDevice device, Data data) {
            Log.i("Callback", "onHeartRateChanged: " + data.getIntValue(Data.FORMAT_UINT16,0));
            Log.i("Callback", "onHeartRateChanged: " + data.getIntValue(Data.FORMAT_UINT8,1));
        }
    };

    /**
     * BluetoothGatt callbacks object.
     */
    private class MyManagerGattCallback extends BleManagerGattCallback {

        @Override
        protected boolean isRequiredServiceSupported(@NonNull BluetoothGatt gatt) {

            BluetoothGattService service = gatt.getService(SERVICE_UUID);
            if(service!=null){
                firstCharacteristic = service.getCharacteristic(FIRST_CHAR);
            }
            if(service!=null&firstCharacteristic!=null){
                return true;
            }
            return false;
        }

        @Override
        protected void initialize() {
            setNotificationCallback(firstCharacteristic).with(heartRateDataCallback);
            readCharacteristic(firstCharacteristic).with(heartRateDataCallback).enqueue();
            enableNotifications(firstCharacteristic).enqueue();
        }

        @Override
        protected void onDeviceDisconnected() {
            firstCharacteristic=null;
        }
    }
}
