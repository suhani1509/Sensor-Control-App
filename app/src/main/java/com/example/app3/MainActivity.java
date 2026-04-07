package com.example.app3;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;

    private Sensor accelerometer;
    private Sensor lightSensor;
    private Sensor proximitySensor;

    private TextView tvAccelX, tvAccelY, tvAccelZ;
    private TextView tvLight;
    private TextView tvProximity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvAccelX   = findViewById(R.id.tvAccelX);
        tvAccelY   = findViewById(R.id.tvAccelY);
        tvAccelZ   = findViewById(R.id.tvAccelZ);
        tvLight    = findViewById(R.id.tvLight);
        tvProximity = findViewById(R.id.tvProximity);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            accelerometer  = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            lightSensor    = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }

        if (accelerometer == null) {
            tvAccelX.setText("X: Not available");
            tvAccelY.setText("Y: Not available");
            tvAccelZ.setText("Z: Not available");
        }
        if (lightSensor == null)    tvLight.setText("Ambient Light: Not available");
        if (proximitySensor == null) tvProximity.setText("Sensor not available");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {
            if (accelerometer != null)
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
            if (lightSensor != null)
                sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_UI);
            if (proximitySensor != null)
                sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {

            case Sensor.TYPE_ACCELEROMETER:
                tvAccelX.setText(String.format("X: %.2f m/s²", event.values[0]));
                tvAccelY.setText(String.format("Y: %.2f m/s²", event.values[1]));
                tvAccelZ.setText(String.format("Z: %.2f m/s²", event.values[2]));
                break;

            case Sensor.TYPE_LIGHT:
                tvLight.setText(String.format("Ambient Light: %.2f lx", event.values[0]));
                break;

            case Sensor.TYPE_PROXIMITY:
                float distance = event.values[0];
                float maxRange = proximitySensor.getMaximumRange();
                if (distance < maxRange) {
                    tvProximity.setText(String.format("Near: %.1f cm", distance));
                } else {
                    tvProximity.setText(String.format("Far: %.1f cm", distance));
                }
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}