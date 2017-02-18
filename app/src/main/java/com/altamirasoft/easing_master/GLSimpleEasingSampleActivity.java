package com.altamirasoft.easing_master;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.altamirasoft.glanimationutil.GLAnimatorManager;
import com.altamirasoft.glanimationutil.GLEasingUpdateListener;
import com.altamirasoft.glanimationutil.GLSimpleEasingHelper;

public class GLSimpleEasingSampleActivity extends AppCompatActivity {



    GLSimpleEasingHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glsimple_easing_sample);




        helper = GLAnimatorManager.getInstance().createSimpleEasingHelper();

        helper.addUpdateListener(new GLEasingUpdateListener() {
            @Override
            public void onUpdateCurrentValue(float value) {
                Log.d("log","calculate");
            }

            @Override
            public void onFinishUpdateValue(float value) {
                Log.d("log","onFinishUpdateValue");
            }
        });

    }
    public void clickStart(View view){

        Log.d("log","clickStart");
        helper.start();
        helper.testListener();
    }

    public void clickStop(View view){
        Log.d("log","clickStop");
        helper.end();
    }
}
