package com.altamirasoft.glanimationutil;

import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by bdhwan on 2017. 2. 18..
 */

public class GLCalculateHelper implements GLAnimatorFrameListener{


    ArrayList<GLCalculateUpdateListener> listeners;
    ArrayList<CalculateListener> mListeners = null;

    @Override
    public void doFrame() {

        if(listeners!=null){
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onUpdateCurrentValue(0);
            }
        }
    }




    public interface CalculateListener {
        void onCalculateStart(GLCalculateHelper animation);
        void onCalculateEnd(GLCalculateHelper animation);
    }


    public GLCalculateHelper addListener(CalculateListener listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<CalculateListener>();
        }
        mListeners.add(listener);
        return this;
    }

    public void removeListener(CalculateListener listener) {
        if (mListeners != null) {
            mListeners.remove(listener);
        }
    }

    public GLCalculateHelper addUpdateListener(GLCalculateUpdateListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<GLCalculateUpdateListener>();
        }

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
        return this;
    }

    public void removeUpdateListener(GLCalculateUpdateListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }



    public void start() {
        if(mListeners!=null){
            for (int i = 0; i < mListeners.size(); i++) {
                mListeners.get(i).onCalculateStart(this);
            }
        }
    }

    public void stop(){
        if(mListeners!=null){
            for (int i = 0; i < mListeners.size(); i++) {
                mListeners.get(i).onCalculateEnd(this);
            }
        }
    }

















}
