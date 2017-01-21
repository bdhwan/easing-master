package com.altamirasoft.glanimationutil;

import android.animation.TimeInterpolator;
import android.os.SystemClock;

import java.util.ArrayList;

/**
 * Created by bdhwan on 2017. 1. 19..
 */

public class GLValueAnimator implements GLAnimatorFrameListener {




    public static interface AnimatorListener {
        /**
         * <p>Notifies the start of the animation.</p>
         *
         * @param animation The started animation.
         */
        void onAnimationStart(GLValueAnimator animation);

        /**
         * <p>Notifies the end of the animation. This callback is not invoked
         * for animations with repeat count set to INFINITE.</p>
         *
         * @param animation The animation which reached its end.
         */
        void onAnimationEnd(GLValueAnimator animation);

        /**
         * <p>Notifies the cancellation of the animation. This callback is not invoked
         * for animations with repeat count set to INFINITE.</p>
         *
         * @param animation The animation which was canceled.
         */
        void onAnimationCancel(GLValueAnimator animation);

        /**
         * <p>Notifies the repetition of the animation.</p>
         *
         * @param animation The animation which was repeated.
         */
        void onAnimationRepeat(GLValueAnimator animation);

    }


    public static interface AnimatorUpdateListener {
        /**
         * <p>Notifies the occurrence of another frame of the animation.</p>
         *
         * @param animation The animation which was repeated.
         */
        void onAnimationUpdate(GLValueAnimator animation);

    }



    ArrayList<AnimatorListener> mListeners = null;
    ArrayList<AnimatorUpdateListener> mUpdateListeners = null;



    long mStartTime;
    boolean isStarted = false;
    boolean isFinished = false;
    boolean isCanceled = false;

    float fromValue;
    float toValue;
    float currentValue;



    long duration;
    long startDelay;




    public static GLValueAnimator ofFloat(float fromValue, float toValue) {
        GLValueAnimator anim = new GLValueAnimator();
        anim.fromValue = fromValue;
        anim.toValue = toValue;
        return anim;
    }




    public GLValueAnimator addListener(AnimatorListener listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<AnimatorListener>();
        }
        mListeners.add(listener);
        return this;
    }



    public GLValueAnimator addUpdateListener(AnimatorUpdateListener listener) {
        if (mUpdateListeners == null) {
            mUpdateListeners = new ArrayList<AnimatorUpdateListener>();
        }
        mUpdateListeners.add(listener);
        return this;
    }






    public void start(){

        mStartTime = SystemClock.uptimeMillis();
        isStarted = true;
        isFinished = false;
        isCanceled = false;

        if (mListeners != null) {
            for (int i = 0; i < mListeners.size(); i++) {
                mListeners.get(i).onAnimationStart(GLValueAnimator.this);
            }
        }
    }


    public void end(){

        isFinished = true;
        isStarted = false;

        if (mListeners != null) {
            for (int i = 0; i < mListeners.size(); i++) {
                mListeners.get(i).onAnimationEnd(GLValueAnimator.this);
            }
        }
    }






    TimeInterpolator interpolator;

    public TimeInterpolator getInterpolator() {
        return interpolator;
    }

    public GLValueAnimator setInterpolator(TimeInterpolator interpolator) {
        this.interpolator = interpolator;
        return this;
    }



    private float interpolator(float startPos, float endPos, float totalTime, float currTime){
        float currentPercent = currTime/totalTime;
        if(totalTime<=currTime){
            currentPercent = 1f;
        }
        float result = 0f;
        if(interpolator==null){
            result = startPos+(endPos - startPos)*currentPercent;
        }
        else{
            result = startPos+(endPos - startPos)*interpolator.getInterpolation(currentPercent);
        }
        return result;
    }

    @Override
    public void doFrame() {


        if(isFinished){
           return;
        }
        if(!isStarted){
            return;
        }
        long mTime = SystemClock.uptimeMillis()-mStartTime;
        if(mTime>startDelay){

            boolean isWillFinish = false;
            mTime = mTime - startDelay;
            currentValue = interpolator(fromValue,toValue,duration,mTime);

            if(currentValue==toValue){
                isWillFinish = true;
            }


            if (mUpdateListeners != null) {
                int numListeners = mUpdateListeners.size();
                for (int i = 0; i < numListeners; ++i) {
                    mUpdateListeners.get(i).onAnimationUpdate(GLValueAnimator.this);
                }
            }

            if(isWillFinish){
                end();
            }

        }
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }



    public long getDuration() {
        return duration;
    }

    public GLValueAnimator setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public long getStartDelay() {
        return startDelay;
    }

    public GLValueAnimator setStartDelay(long startDelay) {
        this.startDelay = startDelay;
        return this;
    }



    public void cancel(){
        isCanceled = true;
        end();

    }


}
