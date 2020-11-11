package com.example.myandroidbleapp;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Base64;

import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback;
import no.nordicsemi.android.ble.data.Data;

public abstract class HeartRateDataCallback implements ProfileDataCallback, HeartRateCallback {

    @Override
    public void onDataReceived(@NonNull BluetoothDevice device, @NonNull Data data) {
        if (data == null) {
            onInvalidDataReceived(device, data);
            return;
        }

        onHeartRateChanged(device, data);
    }
}
