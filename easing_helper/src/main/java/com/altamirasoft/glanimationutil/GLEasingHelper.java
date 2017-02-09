package com.altamirasoft.glanimationutil;

import android.os.SystemClock;

import java.util.ArrayList;

/**
 * Created by bdhwan on 2016. 10. 14..
 */

public class GLEasingHelper  implements GLAnimatorFrameListener {


    public static final int EASING_MODE_SIMPLE_EASING = 0;
    public static final int EASING_MODE_SIMPLE_SPRING = 1;

    int easingMode = EASING_MODE_SIMPLE_EASING;


    float targetValue;
    float currentValue;


    float easing = 0.1f;
    float friction = 0.95f;
    float spring = 0.1f;


    float minDiffer = 1f;
    boolean isStarted = false;
    boolean isPaused = false;


    ArrayList<GLEasingUpdateListener> listeners;
    ArrayList<EasingListener> mListeners = null;



    public interface EasingListener {
        void onEasingStart(GLEasingHelper animation);
        void onEasingEnd(GLEasingHelper animation);
    }





    public GLEasingHelper() {
        targetValue = 0;
        currentValue = 0;
    }



    public GLEasingHelper addListener(EasingListener listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<EasingListener>();
        }
        mListeners.add(listener);
        return this;
    }

    public void removeListener(GLValueAnimator.AnimatorListener listener) {
        if (mListeners != null) {
            mListeners.remove(listener);
        }
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
        if(mListeners!=null && isPaused){
            for (int i = 0; i < mListeners.size(); i++) {
                mListeners.get(i).onEasingStart(this);
            }
        }
    }



    public void pause() {
        isPaused = true;
    }


    @Override
    public void doFrame() {
        now = SystemClock.uptimeMillis();
        invalidateData();
        beforeTime = now;

    }


    long now;
    long beforeTime;
    float frameRate = 20;

    public void setFrameRate(float value){
        this.frameRate = value;
    }

    private void invalidateData() {

        if(beforeTime==0){
            return;
        }

        float d = targetValue - currentValue;

        if (Math.abs(d) < minDiffer) {
            currentValue = targetValue;
        } else {
            float term = (now - beforeTime)/frameRate;
            d = (targetValue - currentValue);
            if(d==0)return;
            if(term>1f){
                term = 1f;
            }
            currentValue = currentValue+d*term * easing;
        }


        if (listeners != null) {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onUpdateCurrentValue(currentValue);
            }
        }



        //notify finish easing
        if(currentValue==targetValue){
            pause();
        }

        if(mListeners!=null && isPaused){
            for (int i = 0; i < mListeners.size(); i++) {
                mListeners.get(i).onEasingEnd(this);
            }
        }

    }


}
