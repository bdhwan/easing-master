package com.altamirasoft.easing_helper;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bdhwan on 2016. 10. 14..
 */

public class EasingHelper {


    public static final int EASING_MODE_SIMPLE_EASING = 0;
    public static final int EASING_MODE_SIMPLE_SPRING = 1;

    int easingMode = EASING_MODE_SIMPLE_EASING;


    ValueAnimator updateAnimator;

    float targetValue;
    float currentValue;


    float easing = 0.1f;
    float friction = 0.95f;
    float spring = 0.1f;



    float minDiffer = easing * 10;

    boolean isStarted = false;


    ArrayList<EasingUpdateListener> listeners;


    public EasingHelper() {
        updateAnimator = ValueAnimator.ofFloat(0, 100);
        updateAnimator.setRepeatCount(ValueAnimator.INFINITE);
        updateAnimator.setRepeatMode(ValueAnimator.RESTART);
        updateAnimator.setDuration(5000);
        updateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                Log.d("log","update ="+animation.getAnimatedValue());
                invalidateData();
            }
        });
        targetValue = 0;
        currentValue = 0;
    }


    public EasingHelper addUpdateListener(EasingUpdateListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<EasingUpdateListener>();
        }

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
        return this;
    }

    public void removeUpdateListener(EasingUpdateListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    public void removeAllUpdateListener() {
        if (listeners != null) {
            listeners.clear();
        }
    }


    public EasingHelper setEasing(float value) {
        easing = value;
        return this;
    }

    public EasingHelper setMinDiffer(float value){
        minDiffer = value;
        return this;
    }


    public EasingHelper setCurrentValue(float value){
        currentValue = value;
        resume();
        return this;
    }

    public EasingHelper setSpring(float value){
        this.spring = value;
        return this;
    }

    public EasingHelper setFriction(float value){
        this.friction = value;
        return this;
    }



    public EasingHelper setTargetValue(float value) {
        this.targetValue = value;
        resume();
        return this;
    }

    public EasingHelper setEasingMode(int target){
        this.easingMode = target;
        return this;
    }

    public boolean isStarted(){
        return isStarted;
    }

    public void start() {

        isStarted = true;
        if(!updateAnimator.isRunning()){
            updateAnimator.start();
        }
    }

    public void resume() {

        if(isStarted){
            if(!updateAnimator.isRunning()){
                updateAnimator.start();
            }
        }
    }


    public void pause() {

        if(updateAnimator.isRunning()){
            updateAnimator.cancel();
        }
    }

    float vx = 0;
    private void invalidateData() {


        if(easingMode==EASING_MODE_SIMPLE_EASING){
            float d = targetValue-currentValue;
            if (Math.abs(d) < minDiffer) {
                currentValue = targetValue;
            } else {
                currentValue = currentValue+d * easing;
            }
        }
        else if(easingMode==EASING_MODE_SIMPLE_SPRING){
            float d = targetValue-currentValue;
            if (Math.abs(d) < minDiffer) {
                currentValue = targetValue;
            } else {

                float ax = d*spring;
                vx += ax;
                vx *=friction;
                currentValue = currentValue+vx;
            }



        }





        if (listeners != null) {

            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onUpdateCurrentValue(currentValue);
            }

            //notify finish easing
            if(currentValue==targetValue){
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).onFinishUpdateValue(currentValue);
                }
                pause();
            }
        }

    }


}
