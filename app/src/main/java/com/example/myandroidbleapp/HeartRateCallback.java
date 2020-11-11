package com.example.myandroidbleapp;

import android.bluetooth.BluetoothDevice;

import androidx.annotation.NonNull;

import no.nordicsemi.android.ble.data.Data;

public interface HeartRateCallback {
    void onHeartRateChanged(@NonNull final BluetoothDevice device, final Data data);
}
