package com.altamirasoft.easing_master;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        object = findViewById(R.id.object);
        cover = findViewById(R.id.cover);

        helperX = new EasingHelper();
        helperY = new EasingHelper();

        helperX.addUpdateListener(new EasingUpdateListener() {
            @Override
            public void onUpdateCurrentValue(float value) {
                Log.d("log","currentX = "+value);

                object.setTranslationX(value);

            }

            @Override
            public void onFinishUpdateValue(float value) {

            }
        });

        helperY.addUpdateListener(new EasingUpdateListener() {
            @Override
            public void onUpdateCurrentValue(float value) {
                object.setTranslationY(value);
            }

            @Override
            public void onFinishUpdateValue(float value) {

            }
        });

        helperX.start();
        helperY.start();






        cover.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float rawX = event.getRawX();
                float rawY = event.getRawY();

                int action = event.getActionMasked();

                if(action==MotionEvent.ACTION_DOWN){
                    helperX.setTargetValue(rawX);
                    helperY.setTargetValue(rawY);

                    return true;
                }
                else if(action==MotionEvent.ACTION_MOVE){

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
