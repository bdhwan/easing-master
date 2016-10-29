package com.altamirasoft.easing_master;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.Touch;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.altamirasoft.easing_helper.EasingHelper;
import com.altamirasoft.easing_helper.EasingUpdateListener;

public class SampleActivity extends AppCompatActivity {


    EasingHelper helperX;
    EasingHelper helperY;

    View object;
    View cover;



    public void clickTouch(View v){

        Intent i = new Intent(this, TouchSampleActivity.class);
        startActivity(i);

    }

    public void clickAcc(View v){

        Intent i = new Intent(this, AccelerometerSampleActivity.class);
        startActivity(i);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);


    }
}
