package com.example.myandroidbleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import no.nordicsemi.android.ble.BleManager;
import no.nordicsemi.android.ble.observer.ConnectionObserver;

public class MainActivity extends AppCompatActivity implements ConnectionObserver {

    private MyBleManager myBleManager;

    private BluetoothLeScanner bluetoothLeScanner =
            BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
    private boolean mScanning;
    private Handler handler = new Handler();

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 100000;
    private static final String TAG = "BLE";

    private Button btnScan;
    private Button btnConnect;
    private TextView txtList;
    private TextView txtHeartRate;

    private BluetoothDevice device;
    private BluetoothDevice deviceAct;

    private ScanCallback leScanCallback =
            new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    device = result.getDevice();
                    if(device.getName()!=null){
                        Log.d("DEVICE BLE", "name: " + device.getName());
                        if(device.getName().equals("NX4393_4E3F")) {
                            deviceAct = device;
                            txtList.setText("NX4393_4E3F found. Ready to connect");
                        }
                    }
                    //
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE not supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnScan = findViewById(R.id.btnScan);
        btnConnect = findViewById(R.id.btnConnect);
        txtList = findViewById(R.id.txtList);
        txtHeartRate = findViewById(R.id.txtHeartRate);

        btnScan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                scanLeDevice();
                btnScan.setText("Scanning...");
            }
        });

        btnConnect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(deviceAct!=null){
                    bluetoothLeScanner.stopScan(leScanCallback);
                    connect(deviceAct);
                }
            }
        });
    }

    public void scanLeDevice() {
        if (!mScanning) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    bluetoothLeScanner.stopScan(leScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            bluetoothLeScanner.startScan(leScanCallback);
        } else {
            mScanning = false;
            bluetoothLeScanner.stopScan(leScanCallback);
        }
    }

    void connect(@NonNull final BluetoothDevice device) {
        myBleManager = new MyBleManager(MainActivity.this);
        myBleManager.setConnectionObserver(this);
        myBleManager.connect(device)
                .timeout(100000)
                .retry(3, 100)
                .done(device2 -> Log.i("Connection", "Device initiated"))
                .enqueue();
    }

    @Override
    public void onDeviceConnecting(@NonNull BluetoothDevice device) {
        Log.i(TAG, "onDeviceConnecting: ");
    }

    @Override
    public void onDeviceConnected(@NonNull BluetoothDevice device) {
        Log.i(TAG, "onDeviceConnected: ");
    }

    @Override
    public void onDeviceFailedToConnect(@NonNull BluetoothDevice device, int reason) {
        Log.i(TAG, "onDeviceFailedToConnect: ");
    }

    @Override
    public void onDeviceReady(@NonNull BluetoothDevice device) {
        Log.i(TAG, "onDeviceReady: ");
    }

    @Override
    public void onDeviceDisconnecting(@NonNull BluetoothDevice device) {
        Log.i(TAG, "onDeviceDisconnecting: ");
    }

    @Override
    public void onDeviceDisconnected(@NonNull BluetoothDevice device, int reason) {
        Log.i(TAG, "onDeviceDisconnected: ");
    }
}
