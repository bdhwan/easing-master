package com.altamirasoft.easing_master;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.altamirasoft.easing_helper.EasingHelper;
import com.altamirasoft.easing_helper.EasingUpdateListener;

public class AccelerometerSampleActivity extends AppCompatActivity implements SensorEventListener,EasingUpdateListener {


    EasingHelper helper;

    View objectNone;
    View objectEasing;


    private SensorManager mSensorManager;
    private Sensor mSensor;
    float target[] = new float[3];

    TextView debug;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_sample);
        objectNone = findViewById(R.id.objectNone);
        objectEasing = findViewById(R.id.objectEasing);
        debug = (TextView)findViewById(R.id.debug);
        helper = new EasingHelper();


        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        //add listener for smooth view update
        helper.addUpdateListener(this);

        //start easing
        helper.start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            target[0] = event.values[0];
            target[1] = event.values[1];
            target[2] = event.values[2];


            debug.setText(String.format("ACCELEROMETER X : %.2f",target[0]));



            //change value
            float xTranslationValue  = -(target[0]/10f)*700;

            //with easing just set target value
            helper.setTargetValue(xTranslationValue);

            //without easing
            objectNone.setTranslationX(xTranslationValue);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    //smooth move easing
    @Override
    public void onUpdateCurrentValue(float value) {
        objectEasing.setTranslationX(value);
    }


    //finish move animation
    @Override
    public void onFinishUpdateValue(float value) {

    }
}
