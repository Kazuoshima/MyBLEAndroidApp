package com.example.myandroidbleapp;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import no.nordicsemi.android.ble.BleServerManager;

public class MyBleServer extends BleServerManager {

    final static UUID SERVICE_UUID = UUID.fromString("00003e01-0000-1000-8000-00805f9b34fb");
    final static UUID CHAR_UUID   = UUID.fromString("00003e03-0000-1000-8000-00805f9b34fb");


    public MyBleServer(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected List<BluetoothGattService> initializeServer() {
        return Collections.singletonList(
                service(SERVICE_UUID,
                        characteristic(CHAR_UUID,
                                BluetoothGattCharacteristic.PROPERTY_WRITE // properties
                                        | BluetoothGattCharacteristic.PROPERTY_NOTIFY
                                        | BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS,
                                BluetoothGattCharacteristic.PERMISSION_WRITE, // permissions
                                (BluetoothGattDescriptor) null, // initial data
                                cccd(), reliableWrite(), description("Some description", false) // descriptors
                        ))
        );
    }
}
