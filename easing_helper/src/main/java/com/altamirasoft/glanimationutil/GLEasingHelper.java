package com.altamirasoft.glanimationutil;

import android.animation.ValueAnimator;

import java.util.ArrayList;

/**
 * Created by bdhwan on 2016. 10. 14..
 */

public class GLEasingHelper  implements GLAnimatorFrameListener {


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
    boolean isPaused = false;


    ArrayList<GLEasingUpdateListener> listeners;


    public GLEasingHelper() {
        targetValue = 0;
        currentValue = 0;
    }





    public GLEasingHelper addUpdateListener(GLEasingUpdateListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<GLEasingUpdateListener>();
        }

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
        return this;
    }

    public void removeUpdateListener(GLEasingUpdateListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    public void removeAllUpdateListener() {
        if (listeners != null) {
            listeners.clear();
        }
    }


    public GLEasingHelper setEasing(float value) {
        easing = value;
        return this;
    }

    public GLEasingHelper setMinDiffer(float value){
        minDiffer = value;
        return this;
    }


    public GLEasingHelper setCurrentValue(float value){
        currentValue = value;

        return this;
    }

    public GLEasingHelper setSpring(float value){
        this.spring = value;
        return this;
    }

    public GLEasingHelper setFriction(float value){
        this.friction = value;
        return this;
    }



    public GLEasingHelper setTargetValue(float value) {
        this.targetValue = value;
        return this;
    }

    public GLEasingHelper setTargetValue(float value, boolean forceStart) {
        this.targetValue = value;
        if(forceStart&&isPaused){
            start();
        }
        return this;
    }

    public GLEasingHelper setEasingMode(int target){
        this.easingMode = target;
        return this;
    }

    public boolean isStarted(){
        return isStarted;
    }

    public void start() {
        isStarted = true;
        isPaused = false;
    }


    public void pause() {
        isPaused = true;
    }


    @Override
    public void doFrame() {


        if(isPaused||!isStarted)return;

        invalidateData();

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
//                pause();
            }
        }

    }


}
