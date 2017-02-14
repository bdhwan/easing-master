package com.altamirasoft.glanimationutil;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by bdhwan on 2017. 1. 20..
 */

public class GLAnimatorManager implements GLAnimatorFrameListener, GLEasingHelper.EasingListener,GLValueAnimator.AnimatorListener {

    static GLAnimatorManager instance;
    ArrayList<GLValueAnimator> valueAnimatorList;
    ArrayList<GLEasingHelper> easingAnimatorList;
    boolean isPendingStopEasing = false;



    GLSurfaceView surfaceView;

    public void setSurfaceView(GLSurfaceView surfaceView) {
        this.surfaceView = surfaceView;
    }



    public static GLAnimatorManager getInstance() {
        if (instance == null) {
            instance = new GLAnimatorManager();
            instance.valueAnimatorList = new ArrayList<GLValueAnimator>();
            instance.easingAnimatorList = new ArrayList<GLEasingHelper>();

        }
        return instance;
    }


    public void addValueAnimFrameListener(GLValueAnimator listener) {
        if(this.valueAnimatorList!=null){
            this.valueAnimatorList.add(listener);
        }
    }


    public void removeValueAnimFrameListener(GLValueAnimator listener) {
        if(this.valueAnimatorList!=null){
            this.valueAnimatorList.remove(listener);
        }

    }

    public GLValueAnimator createValueAnimator(float from, float to) {
        GLValueAnimator animator = GLValueAnimator.ofFloat(from, to);
        addValueAnimFrameListener(animator);
        animator.addListener(this);
        return animator;
    }


    public void addEasingFrameListener(GLEasingHelper listener){
        if(this.easingAnimatorList!=null){
            this.easingAnimatorList.add(listener);
        }

    }



    public void removeEasingFrameListener(GLEasingHelper listener){
        if(this.easingAnimatorList!=null){
            this.easingAnimatorList.remove(listener);
        }

    }


    public GLEasingHelper createEasingHelper(){

        GLEasingHelper helper = new GLEasingHelper();
        addEasingFrameListener(helper);
        helper.addListener(this);
        return helper;

    }





    ValueAnimator anim;

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


    public void pauseAnim() {
        if(anim!=null){
            anim.pause();
        }

    }



    @Override
    public void doFrame() {

        for (int i = 0; i < valueAnimatorList.size(); i++) {
            valueAnimatorList.get(i).doFrame();
        }

        for(int i =0;i<easingAnimatorList.size();i++){
            easingAnimatorList.get(i).doFrame();
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
        if(isPendingStopEasing){

            stopIfNoNeedUpdate();
        }
    }


    public void setPendingStopAnimationWithEasingEnd(boolean isPending){
        this.isPendingStopEasing = isPending;
    }


    public void stopIfNoNeedUpdate(){
        if(anim!=null){

            if(valueAnimatorList.size()>0){
                return;
            }

            boolean needStop = true;
            for(int i =0;i<easingAnimatorList.size();i++){
                if(!easingAnimatorList.get(i).isPaused){
                    needStop = false;
                    break;
                }
            }

            if(needStop){
                anim.pause();
            }
        }
    }



}
