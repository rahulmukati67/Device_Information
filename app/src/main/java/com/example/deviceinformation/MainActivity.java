package com.example.deviceinformation;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView model , manufacturer ,ram ,storage, battery , camera,androidVersion , processor , modelNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        model = findViewById(R.id.model);
        ram = findViewById(R.id.ram);
        battery = findViewById(R.id.battery);
        storage = findViewById(R.id.storage);
        modelNumber = findViewById(R.id.modelNumber);
        processor = findViewById(R.id.processor);
        camera = findViewById(R.id.camera);
        androidVersion = findViewById(R.id.androidVersion);
        manufacturer = findViewById(R.id.manufacturer);


        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean camera = result.getOrDefault(
                                    Manifest.permission.CAMERA ,false);


                            if (!(camera != null && camera)){
                                Toast.makeText(this, "CAMERA PERMISSION MAY NOT  GRANTED", Toast.LENGTH_SHORT).show();

                            }
                        }
                );

        locationPermissionRequest.launch(new String[] {
                Manifest.permission.CAMERA,
        });
        

        manufacturer.setText(Build.MANUFACTURER);
        model.setText(Build.MODEL);
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        String r = String.valueOf(memoryInfo.totalMem);
        ram.setText(r);
        storage.setText(Storage());
        battery.setText(Battery());
        String version = String.valueOf(Build.VERSION.SDK_INT);
        androidVersion.setText(version);
        String proc = String.valueOf(Build.HARDWARE);
        processor.setText(proc);
        String model_number = String.valueOf(Build.DEVICE);
        modelNumber.setText(model_number);
        camera.setText(Camera());




    }

    String Storage(){
        File path = Environment.getExternalStorageDirectory();
        long storageBytes = path.getTotalSpace();
        String storageGB = String.valueOf(storageBytes / Math.pow(1024, 3));
        return storageGB;
    }
    String Battery(){
        Context context = getApplicationContext();
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        return String.valueOf(level * 100 / (float)scale);
    }
    String Camera(){
        Camera camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
        Camera.Size pictureSize = pictureSizes.get(0);
        String megapixels =String.valueOf ((pictureSize.width * pictureSize.height) / 1000000f);
//        Log.d("Camera", "Megapixels: " + megapixels);
        camera.release();
        return megapixels;
    }


}
