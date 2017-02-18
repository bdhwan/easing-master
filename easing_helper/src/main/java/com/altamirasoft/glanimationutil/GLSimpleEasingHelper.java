package com.altamirasoft.glanimationutil;

import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by bdhwan on 2016. 10. 14..
 */

public class GLSimpleEasingHelper implements GLAnimatorFrameListener {


    boolean isStarted = false;
    boolean isPaused = false;


    ArrayList<GLEasingUpdateListener> listeners;
    ArrayList<EasingListener> mListeners = null;



    public interface EasingListener {
        void onEasingStart(GLSimpleEasingHelper animation);
        void onEasingEnd(GLSimpleEasingHelper animation);
    }



    public GLSimpleEasingHelper() {

    }



    public GLSimpleEasingHelper addListener(EasingListener listener) {
        if (mListeners == null) {
            Log.d("log","alloc listener");
            mListeners = new ArrayList<EasingListener>();
        }
        Log.d("log","addListener");
        mListeners.add(listener);
        testListener();
        return this;
    }

    public void removeListener(GLValueAnimator.AnimatorListener listener) {
        if (mListeners != null) {
            mListeners.remove(listener);
        }
    }


    public void testListener(){
        Log.d("log","testListener = "+mListeners.size());
    }



    public GLSimpleEasingHelper addUpdateListener(GLEasingUpdateListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<GLEasingUpdateListener>();
        }

        Log.d("log","ok will add listener");

        if (!listeners.contains(listener)) {
            Log.d("log","ok will add listener good");
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


    public boolean isStarted(){
        return isStarted;
    }

    public void start() {
        isStarted = true;
        isPaused = false;

        testListener();
        if(mListeners!=null){

            for (int i = 0; i < mListeners.size(); i++) {
                mListeners.get(i).onEasingStart(this);
            }
        }

    }



    public void end() {
        isPaused = true;

        if(mListeners!=null){
            for (int i = 0; i < mListeners.size(); i++) {
                mListeners.get(i).onEasingEnd(this);
            }
        }

    }


    @Override
    public void doFrame() {
         invalidateData();
    }

    private void invalidateData() {
        if (listeners != null) {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onUpdateCurrentValue(0);
            }
        }
    }


}
