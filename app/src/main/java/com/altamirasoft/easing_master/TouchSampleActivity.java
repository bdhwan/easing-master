package com.altamirasoft.easing_master;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.altamirasoft.easing_helper.EasingHelper;
import com.altamirasoft.easing_helper.EasingUpdateListener;

public class TouchSampleActivity extends AppCompatActivity {


    EasingHelper helperX;
    EasingHelper helperY;

    View object;
    View cover;
    View objectNone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_sample);
        object = findViewById(R.id.object);
        cover = findViewById(R.id.cover);
        objectNone = findViewById(R.id.objectNone);


        helperY = new EasingHelper().setEasing(0.1f);


        helperX = new EasingHelper().setEasing(0.1f);
        helperX.addUpdateListener(new EasingUpdateListener() {

            @Override
            public void onUpdateCurrentValue(float value) {
                object.setTranslationX(value);
            }

            @Override
            public void onFinishUpdateValue(float value) {

            }
        });
        helperX.start();



        helperY.addUpdateListener(new EasingUpdateListener() {
            @Override
            public void onUpdateCurrentValue(float value) {
                object.setTranslationY(value);
            }

            @Override
            public void onFinishUpdateValue(float value) {

            }
        });


        helperY.start();


        cover.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float rawX = event.getRawX();
                float rawY = event.getRawY();

                int action = event.getActionMasked();

                if(action==MotionEvent.ACTION_DOWN){


                    //without easing
                    objectNone.setTranslationX(rawX);
                    objectNone.setTranslationY(rawY);

                    //with easing
                    helperX.setTargetValue(rawX);
                    helperY.setTargetValue(rawY);


                    return true;
                }
                else if(action==MotionEvent.ACTION_MOVE){

                    //without easing
                    objectNone.setTranslationX(rawX);
                    objectNone.setTranslationY(rawY);

                    //with easing
                    helperX.setTargetValue(rawX);
                    helperY.setTargetValue(rawY);


                    return true;
                }
                else if(action==MotionEvent.ACTION_UP){

                    return true;
                }
                return false;
            }
        });

    }
}
