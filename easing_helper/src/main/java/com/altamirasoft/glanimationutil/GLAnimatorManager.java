package com.altamirasoft.glanimationutil;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bdhwan on 2017. 1. 20..
 */

public class GLAnimatorManager implements GLAnimatorFrameListener,  GLEasingHelper.EasingListener,GLCalculateHelper.CalculateListener, GLValueAnimator.AnimatorListener {

    static GLAnimatorManager instance;
    ArrayList<GLValueAnimator> valueAnimatorList;
    ArrayList<GLEasingHelper> easingAnimatorList;
    ArrayList<GLCalculateHelper> calculateAnimatorList;



    boolean isPendingStopEasing = true;

    boolean needKeepUpdate = false;

    GLSurfaceView surfaceView;

    public void setSurfaceView(GLSurfaceView surfaceView) {
        this.surfaceView = surfaceView;
    }


    public static GLAnimatorManager getInstance() {
        if (instance == null) {
            instance = new GLAnimatorManager();
            instance.valueAnimatorList = new ArrayList<GLValueAnimator>();
            instance.easingAnimatorList = new ArrayList<GLEasingHelper>();
            instance.calculateAnimatorList = new ArrayList<GLCalculateHelper>();
        }
        return instance;
    }


    public void addValueAnimFrameListener(GLValueAnimator listener) {
        if (this.valueAnimatorList != null) {
            this.valueAnimatorList.add(listener);
        }
    }


    public void removeValueAnimFrameListener(GLValueAnimator listener) {
        if (this.valueAnimatorList != null) {
            this.valueAnimatorList.remove(listener);
        }
    }



    public GLValueAnimator createValueAnimator(float from, float to) {
        GLValueAnimator animator = GLValueAnimator.ofFloat(from, to);
        addValueAnimFrameListener(animator);
        animator.addListener(this);
        return animator;
    }


    public void addCalculateFrameListener(GLCalculateHelper listener) {
        if (this.calculateAnimatorList != null) {
            this.calculateAnimatorList.add(listener);
        }
    }




    public void addEasingFrameListener(GLEasingHelper listener) {
        if (this.easingAnimatorList != null) {
            this.easingAnimatorList.add(listener);
        }
    }


    public void removeEasingFrameListener(GLEasingHelper listener) {
        if (this.easingAnimatorList != null) {
            this.easingAnimatorList.remove(listener);
        }
    }


    public GLEasingHelper createEasingHelper() {


        if (this.easingAnimatorList != null) {
            this.easingAnimatorList.clear();
        }

        GLEasingHelper helper = new GLEasingHelper();
        addEasingFrameListener(helper);
        helper.addListener(this);
        return helper;

    }



    public GLCalculateHelper createCalculateHelper() {


        if (this.calculateAnimatorList != null) {
            this.calculateAnimatorList.clear();
        }






        GLCalculateHelper helper = new GLCalculateHelper();
        addCalculateFrameListener(helper);
        helper.addListener(this);
        return helper;
    }






    ValueAnimator anim;


    public boolean isPlaying() {
        if (anim == null) {
            return false;
        } else {
            return anim.isRunning();
        }
    }

    public void startAnim() {

        if (anim == null) {


            anim = ValueAnimator.ofFloat(0f, 1f);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {

                    doFrame();
                }
            });

            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            anim.setDuration(1000);
            anim.setRepeatMode(ValueAnimator.REVERSE);
            anim.setRepeatCount(ValueAnimator.INFINITE);
        }

        if (anim.isPaused()) {
            anim.resume();
        } else {
            anim.start();
        }
    }

    public void resumeAnim(){
        if (anim != null) {
            if (anim.isPaused()) {
                anim.resume();
                if (isPendingStopEasing) {
                    stopIfNoNeedUpdate();
                }
            }
        }
    }

    public void pauseAnim() {
        if (anim != null) {
            anim.pause();
        }
    }


    public void resetAll(){

        if (anim != null) {
            anim.pause();
            anim = null;
        }

        if(valueAnimatorList!=null){
            valueAnimatorList.clear();
        }

        if(easingAnimatorList!=null){
            easingAnimatorList.clear();
        }

        if(calculateAnimatorList!=null){
            calculateAnimatorList.clear();
        }

        GLAnimatorManager.instance = null;

    }



    @Override
    public void doFrame() {

//        long now = new Date().getTime();
//        long differ = now - beforeFrame;

//        Log.d("log","differ:"+differ+", valueAnimatorList ="+valueAnimatorList.size()+", easingAnimatorList ="+easingAnimatorList.size()+", calculateAnimatorList ="+calculateAnimatorList.size());

//        beforeFrame = now;

        for (int i = 0; i < valueAnimatorList.size(); i++) {
            valueAnimatorList.get(i).doFrame();
        }
        for (int i = 0; i < easingAnimatorList.size(); i++) {
            easingAnimatorList.get(i).doFrame();
        }
        for (int i = 0; i < calculateAnimatorList.size(); i++) {
            calculateAnimatorList.get(i).doFrame();
        }

        if (surfaceView != null) {
            surfaceView.requestRender();
        }
    }


    @Override
    public void onAnimationStart(GLValueAnimator animation) {
        startAnim();
    }

    @Override
    public void onAnimationEnd(GLValueAnimator animation) {
        removeValueAnimFrameListener(animation);
        stopIfNoNeedUpdate();
    }

    @Override
    public void onAnimationCancel(GLValueAnimator animation) {

    }

    @Override
    public void onAnimationRepeat(GLValueAnimator animation) {

    }


    @Override
    public void onEasingStart(GLEasingHelper animation) {

    }

    @Override
    public void onEasingEnd(GLEasingHelper animation) {
        if (isPendingStopEasing) {
            stopIfNoNeedUpdate();
        }
    }


    public void setPendingStopAnimationWithEasingEnd(boolean isPending) {
        this.isPendingStopEasing = isPending;
    }


    public void stopIfNoNeedUpdate() {
        if (anim != null) {

            //force update
            if(needKeepUpdate)return;

            //check value animator is working
            if (valueAnimatorList.size() > 0) {
                return;
            }

            //check easing animator is working
            boolean needStop = true;
            for (int i = 0; i < easingAnimatorList.size(); i++) {
                if (!easingAnimatorList.get(i).isPaused) {
                    needStop = false;
                    break;
                }
            }
            if (needStop) {
                anim.pause();
            }
        }
    }


    @Override
    public void onCalculateStart(GLCalculateHelper animation) {
        needKeepUpdate = true;
    }

    @Override
    public void onCalculateEnd(GLCalculateHelper animation) {
        needKeepUpdate = false;
        stopIfNoNeedUpdate();
    }
}
